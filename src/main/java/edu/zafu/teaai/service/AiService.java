package edu.zafu.teaai.service;

import edu.zafu.teaai.model.dto.ai.AiGenerateQuestionRequest;
import edu.zafu.teaai.model.dto.question.QuestionContentDTO;
import edu.zafu.teaai.model.po.QuestionBank;
import edu.zafu.teaai.model.po.UserAnswer;

import java.util.List;

/**
 * AI服务
 *
 * @author ColaBlack
 */
public interface AiService {

    /**
     * 生成题目
     * @param request AI生成题目请求
     * @return 题目内容DTO列表
     */
    List<QuestionContentDTO> generateQuestion(AiGenerateQuestionRequest request);

    /**
     * AI判断答案
     * @param choices 选项列表
     * @param questionBank 题库
     * @return 用户答案
     * @throws Exception 异常
     */
    UserAnswer aiJudge(List<String> choices, QuestionBank questionBank) throws Exception;
}
