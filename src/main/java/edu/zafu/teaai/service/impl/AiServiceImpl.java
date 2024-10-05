package edu.zafu.teaai.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import edu.zafu.teaai.common.ErrorCode;
import edu.zafu.teaai.common.exception.ThrowUtils;
import edu.zafu.teaai.model.dto.ai.AiGenerateQuestionRequest;
import edu.zafu.teaai.model.dto.ai.QuestionAnswerDTO;
import edu.zafu.teaai.model.dto.question.QuestionContentDTO;
import edu.zafu.teaai.model.enums.QuestionBankTypeEnum;
import edu.zafu.teaai.model.po.Question;
import edu.zafu.teaai.model.po.QuestionBank;
import edu.zafu.teaai.model.po.UserAnswer;
import edu.zafu.teaai.model.vo.QuestionVO;
import edu.zafu.teaai.service.AiService;
import edu.zafu.teaai.service.QuestionBankService;
import edu.zafu.teaai.service.QuestionService;
import edu.zafu.teaai.utils.AiUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public QuestionService questionService;

    @Override
    public List<QuestionContentDTO> generateQuestion(AiGenerateQuestionRequest request) {
        //生成用户prompt
        Long bankId = request.getBankId();
        QuestionBank questionBank = questionBankService.getById(bankId);
        ThrowUtils.throwIf(questionBank == null, ErrorCode.PARAMS_ERROR, "题库不存在");
        String prompt = questionBank.getBankName() + ",\n";
        prompt += "【【【" + questionBank.getBankDesc() + "】】】,\n";
        prompt += Objects.requireNonNull(QuestionBankTypeEnum.getEnumByValue(questionBank.getBankType())).getText() + ",\n";
        prompt += request.getQuestionNumber() + ",\n";
        prompt += request.getOptionNumber() + "\n";

        //调用AI接口
        String systemPrompt = "你是一位严谨的出题专家，我会给你如下信息：\n" + "```\n" + "应用名称，\n" + "【【【应用描述】】】，\n" + "应用类别，\n" + "要生成的题目数，\n" + "每个题目的选项数\n" + "```\n" + "\n" + "请你根据上述信息，按照以下步骤来出题：\n" + "1. 要求：题目和选项尽可能地短，题目不要包含序号，每题的选项数以我提供的为主，题目不能重复\n" + "2. 严格按照下面的 json 格式输出题目和选项\n" + "```\n" + "[{\"options\":[{\"value\":\"选项内容\",\"key\":\"A\"},{\"value\":\"\",\"key\":\"B\"}],\"title\":\"题目标题\"}]\n" + "```\n" + "title 是题目，options 是选项，每个选项的 key 按照英文字母序（比如 A、B、C、D）以此类推，value 是选项内容\n" + "3. 检查题目是否包含序号，若包含序号则去除序号\n" + "4. 返回的题目列表格式必须为 JSON 数组\n";
        String ret = AiUtils.aiCaller(systemPrompt + prompt);


        //处理返回结果
        int startIndex = ret.indexOf("[");
        int endIndex = ret.lastIndexOf("]");
        String jsonStr = ret.substring(startIndex, endIndex + 1);
        System.out.println(jsonStr);
        return JSONUtil.toList(jsonStr, QuestionContentDTO.class);
    }


    @Override
    public UserAnswer aiJudge(List<String> choices, QuestionBank questionBank) {
        Long bankId = questionBank.getId();
        Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class).eq(Question::getBankid, bankId));
        QuestionVO questionVO = QuestionVO.poToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();

        // 构造提示词
        StringBuilder userPrompt = new StringBuilder();
        userPrompt.append(questionBank.getBankName()).append("\n");
        userPrompt.append(questionBank.getBankDesc()).append("\n");
        List<QuestionAnswerDTO> questionAnswerDTOList = new ArrayList<>();
        for (int i = 0; i < questionContent.size(); i++) {
            QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
            questionAnswerDTO.setTitle(questionContent.get(i).getTitle());
            questionAnswerDTO.setUserAnswer(choices.get(i));
            questionAnswerDTOList.add(questionAnswerDTO);
        }
        userPrompt.append(JSONUtil.toJsonStr(questionAnswerDTOList));

        String systemPrompt = "你是一位严谨的判题专家，我会给你如下信息：\n" + "```\n" + "应用名称，\n" + "【【【应用描述】】】，\n" + "题目和用户回答的列表：格式为 [{\"title\": \"题目\",\"answer\": \"用户回答\"}]\n" + "```\n" + "\n" + "请你根据上述信息，按照以下步骤来对用户进行评价：\n" + "1. 要求：需要给出一个明确的评价结果，包括评价名称（尽量简短）和评价描述（尽量详细，大于 200 字）\n" + "2. 严格按照下面的 json 格式输出评价名称和评价描述\n" + "```\n" + "{\"resultName\": \"评价名称\", \"resultDesc\": \"评价描述\"}\n" + "```\n" + "3. 返回格式必须为 JSON 对象";

        String prompt = systemPrompt + userPrompt;
        // 调用 AI 接口
        String ret = AiUtils.aiCaller(prompt);

        // 结果处理
        int start = ret.indexOf("{");
        int end = ret.lastIndexOf("}");
        String json = ret.substring(start, end + 1);

        // 构造返回值，填充答案对象的属性
        UserAnswer userAnswer = JSONUtil.toBean(json, UserAnswer.class);
        userAnswer.setBankid(bankId);
        userAnswer.setBanktype(questionBank.getBankType());
        userAnswer.setScoringStrategy(questionBank.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        return userAnswer;
    }
}
