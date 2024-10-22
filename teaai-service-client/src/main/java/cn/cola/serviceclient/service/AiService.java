package cn.cola.serviceclient.service;


import cn.cola.model.ai.AiGenerateQuestionRequest;
import cn.cola.model.po.QuestionBank;
import cn.cola.model.question.QuestionContentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * AI服务
 *
 * @author ColaBlack
 */
@FeignClient(name = "ai", url = "/api/ai")
public interface AiService {

    /**
     * AI生成题目(同步)
     *
     * @param request AI生成题目请求
     * @return 题目内容DTO列表
     */
    @PostMapping("/generateQuestion")
    List<QuestionContentDTO> generateQuestion(@RequestBody AiGenerateQuestionRequest request);

    /**
     * AI生成题目(SSE)
     *
     * @param request AI生成题目请求
     * @return SSE Emitter
     */
    @PostMapping("/generateQuestionSSE")
    SseEmitter generateQuestionSSE(@RequestBody AiGenerateQuestionRequest request);

    /**
     * AI判断测评类答案
     *
     * @param choices      选项列表
     * @param questionBank 题库
     * @return AI判题结果
     * @throws Exception 异常
     */
    @PostMapping("/judgeTest")
    String aiJudgeTest(@RequestBody List<String> choices, @RequestBody QuestionBank questionBank) throws Exception;

    /**
     * AI判断得分类答案
     *
     * @param score        得分
     * @param questionBank 题库
     * @return AI判题结果
     * @throws Exception 异常
     */
    @PostMapping("/judgeScore")
    String aiJudgeScore(@RequestBody int score, @RequestBody QuestionBank questionBank) throws Exception;

}