package edu.zafu.teaai.model.dto.ai;

import lombok.Data;

/**
 * 用户回答DTO
 *
 * @author ColaBlack
 */
@Data
public class QuestionAnswerDTO {

    /**
     * 题目
     */
    private String title;

    /**
     * 用户答案
     */
    private String userAnswer;
}
