package cn.cola.model.ai;

import lombok.Data;

import java.io.Serializable;

/**
 * AI生成题目DTO
 *
 * @author ColaBlack
 */
@Data
public class AiGenerateQuestionRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 生成的题目数
     */
    int questionNumber = 5;

    /**
     * 每题的选项数
     */
    int optionNumber = 4;
    /**
     * 题库id
     */
    private Long bankId;
}
