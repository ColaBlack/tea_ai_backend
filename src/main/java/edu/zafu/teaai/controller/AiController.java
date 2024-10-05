package edu.zafu.teaai.controller;

import edu.zafu.teaai.common.BaseResponse;
import edu.zafu.teaai.common.ErrorCode;
import edu.zafu.teaai.common.ResultUtils;
import edu.zafu.teaai.common.exception.ThrowUtils;
import edu.zafu.teaai.model.dto.ai.AiGenerateQuestionRequest;
import edu.zafu.teaai.model.dto.question.QuestionContentDTO;
import edu.zafu.teaai.service.AiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/generate/question")
    public BaseResponse<List<QuestionContentDTO>> generateQuestion(@RequestBody AiGenerateQuestionRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR, "请求参数不能为空");
        List<QuestionContentDTO> questionList = aiService.generateQuestion(request);
        return ResultUtils.success(questionList);
    }
}
