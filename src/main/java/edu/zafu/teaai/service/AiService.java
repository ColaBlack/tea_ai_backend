package edu.zafu.teaai.service;

import edu.zafu.teaai.model.dto.ai.AiGenerateQuestionRequest;
import edu.zafu.teaai.model.po.QuestionBank;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

/**
 * AI服务
 *
 * @author ColaBlack
 */
public interface AiService {

    /**
     * AI生成题目(SSE)
     *
     * @param request AI生成题目请求
     * @return SSE Emitter
     */
    SseEmitter generateQuestionSSE(AiGenerateQuestionRequest request);

    /**
     * AI判断测评类答案
     *
     * @param choices      选项列表
     * @param questionBank 题库
     * @return AI判题结果
     * @throws Exception 异常
     */
    String aiJudgeTest(List<String> choices, QuestionBank questionBank) throws Exception;

    /**
     * AI判断得分类答案
     *
     * @param score        得分
     * @param questionBank 题库
     * @return AI判题结果
     * @throws Exception 异常
     */
    String aiJudgeScore(int score , QuestionBank questionBank) throws Exception;

}
