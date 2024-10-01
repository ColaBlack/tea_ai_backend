package edu.zafu.teaai.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * AI 生成题目请求
 *
 * @author ColaBlack
 */
@Data
public class AiGenerateQuestionRequest implements Serializable {

    /**
     * 题库id
     */
    private Long bankId;

    /**
     * 生成题目数(默认10个)
     */
    int questionNumber = 10;

    /**
     * 选项数量(默认2个)
     */
    int optionNumber = 2;

    private static final long serialVersionUID = 1L;
}
