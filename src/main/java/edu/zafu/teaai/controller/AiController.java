package edu.zafu.teaai.controller;

import edu.zafu.teaai.common.BaseResponse;
import edu.zafu.teaai.common.ErrorCode;
import edu.zafu.teaai.common.ResultUtils;
import edu.zafu.teaai.common.exception.ThrowUtils;
import edu.zafu.teaai.model.dto.ai.AiGenerateQuestionRequest;
import edu.zafu.teaai.model.dto.question.QuestionContentDTO;
import edu.zafu.teaai.service.AiService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.util.List;

/**
 * AI接口
 *
 * @author ColaBlack
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private AiService aiService;

    /**
     * AI 生成题目(同步)
     *
     * @param request AI 生成题目请求
     * @return 生成的题目列表
     */
    @PostMapping("/generate/question")
    public BaseResponse<List<QuestionContentDTO>> generateQuestion(@RequestBody AiGenerateQuestionRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        List<QuestionContentDTO> questionList = aiService.generateQuestion(request);
        return ResultUtils.success(questionList);
    }

    /**
     * AI 生成题目(SSE)
     *
     * @param request AI 生成题目请求
     * @return 生成的题目列表
     */
    @GetMapping("/generate/question/sse")
    public SseEmitter aiGenerateQuestionSSE(AiGenerateQuestionRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        return aiService.generateQuestionSSE(request);
    }

}
