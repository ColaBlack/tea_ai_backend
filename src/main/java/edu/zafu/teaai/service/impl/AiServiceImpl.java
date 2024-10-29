package edu.zafu.teaai.service.impl;

import cn.hutool.json.JSONUtil;
import com.zhipu.oapi.service.v4.model.ModelData;
import edu.zafu.teaai.common.ErrorCode;
import edu.zafu.teaai.common.exception.ThrowUtils;
import edu.zafu.teaai.model.dto.ai.AiGenerateQuestionRequest;
import edu.zafu.teaai.model.po.QuestionBank;
import edu.zafu.teaai.service.AiService;
import edu.zafu.teaai.service.QuestionBankService;
import edu.zafu.teaai.utils.AiUtils;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * AI服务实现类
 *
 * @author ColaBlack
 */
@Service
public class AiServiceImpl implements AiService {

    @Resource
    private QuestionBankService questionBankService;

    @Resource
    private AiUtils aiUtils;

    @Override
    public SseEmitter generateQuestionSSE(AiGenerateQuestionRequest request) {
        // 获取参数
        Long bankId = request.getBankId();
        // 获取应用信息
        QuestionBank questionBank = questionBankService.getById(bankId);
        ThrowUtils.throwIf(questionBank == null, ErrorCode.NOT_FOUND_ERROR, "题库不存在");
        String prompt = "";
        //生成提示词
        if (questionBank.getBankType() == 1) {
            prompt = "\n" +
                    "你是AI出题助手。按以下要求出题:\n" +
                    "\n" +
                    "1. 题库信息:\n" +
                    "<题库名称>" + questionBank.getBankName() + "</题库名称>\n" +
                    "<题库描述>" + questionBank.getBankDesc() + "</题库描述>\n" +
                    "\n" +
                    "2. 出题规则:\n" +
                    "- 出" + request.getQuestionNumber() + "道题,每题" + request.getOptionNumber() + "个选项\n" +
                    "- 题目和选项力求简洁\n" +
                    "- 题目不加序号,不重复\n" +
                    "\n" +
                    "3. 输出格式:\n" +
                    "```json\n" +
                    "[\n" +
                    "  {\n" +
                    "    \"title\": \"题目\",\n" +
                    "    \"options\": [\n" +
                    "      {\"key\": \"A\", \"value\": \"选项内容\"},\n" +
                    "      {\"key\": \"B\", \"value\": \"选项内容\"}\n" +
                    "    ]\n" +
                    "  }\n" +
                    "]\n" +
                    "```\n" +
                    "\n" +
                    "4. 检查:\n" +
                    "- 确保题目无序号\n" +
                    "- 核对题目数量和选项数量\n" +
                    "\n" +
                    "直接输出JSON,不要其他说明。\n";
        }
        if (questionBank.getBankType() == 0) {
            prompt = "\n" +
                    "你是一个高效的题目生成AI。使用以下输入创建测试题目：\n" +
                    "\n" +
                    "名称：" + questionBank.getBankName() + "\n" +
                    "描述：" + questionBank.getBankDesc() + "\n" +
                    "\n" +
                    "生成要求：\n" +
                    "1. 题目数量：" + request.getQuestionNumber() + "\n" +
                    "2. 每题选项数：" + request.getOptionNumber() + "\n" +
                    "3. 题目和选项须简洁\n" +
                    "4. 题目不含序号\n" +
                    "5. 题目不重复\n" +
                    "6. 选项键值为大写字母（A、B、C...）\n" +
                    "\n" +
                    "输出格式：\n" +
                    "```json\n" +
                    "[\n" +
                    "  {\n" +
                    "    \"title\": \"题目文本\",\n" +
                    "    \"options\": [\n" +
                    "      {\n" +
                    "        \"key\": \"A\",\n" +
                    "        \"value\": \"选项内容\",\n" +
                    "        \"score\": 分数\n" +
                    "      },\n" +
                    "      ...\n" +
                    "    ]\n" +
                    "  },\n" +
                    "  ...\n" +
                    "]\n" +
                    "```\n" +
                    "\n" +
                    "注意事项：\n" +
                    "- 确保输出为有效的JSON数组\n" +
                    "- 分数应为整数\n" +
                    "- 仔细检查并移除任何题目中的序号\n" +
                    "\n" +
                    "立即开始生成题目，无需其他说明。\n" +
                    "\n";
        }
        // 建立 SSE 连接对象，0 表示不超时
        SseEmitter emitter = new SseEmitter(0L);
        // AI 生成，sse 流式返回
        Flowable<ModelData> modelDataFlowable = aiUtils.aiCallerFlow(prompt);

        StringBuilder contentBuilder = new StringBuilder();
        AtomicInteger flag = new AtomicInteger(0);
        modelDataFlowable.observeOn(Schedulers.io()).map(chunk -> chunk.getChoices().get(0).getDelta().getContent()).map(message -> message.replaceAll("\\s", "")).filter(StringUtils::isNotBlank).flatMap(message -> {
            // 将字符串转换为 List<Character>
            List<Character> charList = new ArrayList<>();
            for (char c : message.toCharArray()) {
                charList.add(c);
            }
            return Flowable.fromIterable(charList);
        }).doOnNext(c -> {
            {
                // 识别第一个 [ 表示开始 AI 传输 json 数据，打开 flag 开始拼接 json 数组
                if (c == '{') {
                    flag.addAndGet(1);
                }
                if (flag.get() > 0) {
                    contentBuilder.append(c);
                }
                if (c == '}') {
                    flag.addAndGet(-1);
                    if (flag.get() == 0) {
                        // 累积单套题目满足 json 格式后，sse 推送至前端
                        // sse 需要压缩成当行 json，sse 无法识别换行
                        emitter.send(JSONUtil.toJsonStr(contentBuilder.toString()));
                        // 清空 StringBuilder
                        contentBuilder.setLength(0);
                    }
                }
            }
        }).doOnComplete(emitter::complete).subscribe();
        return emitter;
    }


    @Override
    public String aiJudgeTest(List<String> choices, QuestionBank questionBank) {
        // 构造提示词
        String prompt = "\n" +
                "作为一名经验丰富的判题专家，请你根据以下信息对答题者的表现进行全面分析：\n" +
                "\n" +
                "输入信息：\n" +
                "1. 题库名称：" + questionBank.getBankName() + "\n" +
                "2. 题库描述：\n" +
                questionBank.getBankDesc() + "\n" +
                "3. 答题记录：\n" +
                choices.toString() + "\n" +
                "评估步骤：\n" +
                "1. 仔细研读题库描述和答题者的答题记录。\n" +
                "2. 分析答题者对题目的理解程度和答题准确性。\n" +
                "3. 总结答题者的强项和需要改进的地方。\n" +
                "4. 给出一个简明扼要的评价标题（不超过10个字）和一段详细的评价说明（至少200字）。\n" +
                "\n" +
                "请将你的评估结果按以下JSON格式输出：\n" +
                "\n" +
                "<output_format>\n" +
                "{\n" +
                "  \"resultName\": \"评价标题\",\n" +
                "  \"resultDesc\": \"评价说明\"\n" +
                "}\n" +
                "</output_format>\n" +
                "\n" +
                "评估注意事项：\n" +
                "1. 评价要客观公正，基于答题者的实际答题表现。\n" +
                "2. 评价说明应当具体详实，包括答题者的知识掌握情况、解题策略和潜在的学习方向。\n" +
                "3. 避免使用空泛或模糊的表述，尽量提供有针对性的反馈。\n" +
                "4. 如果可能，建议包含一些鼓励性的内容，以激发答题者的学习积极性。\n" +
                "\n";
        // 调用 AI 接口
        String ret = aiUtils.aiCaller(prompt);

        // 结果处理
        int start = ret.indexOf("{");
        int end = ret.lastIndexOf("}");
        return ret.substring(start, end + 1);

    }

    @Override
    public String aiJudgeScore(int score, QuestionBank questionBank) {
        String prompt = "\n" +
                "你是一个高效的AI评估系统。使用以下数据进行分析：\n" +
                "\n" +
                "题库：" + questionBank.getBankName() + "\n" +
                "描述：" + questionBank.getBankDesc() + "\n" +
                "得分：" + score + "\n" +
                "\n" +
                "任务：\n" +
                "1. 分析答题表现\n" +
                "2. 生成10字以内标题\n" +
                "3. 撰写200字以上详细评价\n" +
                "\n" +
                "以JSON格式输出：\n" +
                "\n" +
                "<output_format>\n" +
                "{\n" +
                "  \"resultName\": \"简短评价标题\",\n" +
                "  \"resultDesc\": \"详细分析内容\"\n" +
                "}\n" +
                "</output_format>\n" +
                "\n" +
                "注意：评价应客观、具体，并包含改进建议。\n";

        // 调用 AI 接口
        String ret = aiUtils.aiCaller(prompt);

        // 结果处理
        int start = ret.indexOf("{");
        int end = ret.lastIndexOf("}");
        return ret.substring(start, end + 1);
    }
}
