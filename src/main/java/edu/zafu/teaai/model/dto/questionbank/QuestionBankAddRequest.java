package edu.zafu.teaai.model.dto.questionbank;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建题库请求
 *
 * @author ColaBlack
 */
@Data
public class QuestionBankAddRequest implements Serializable {

    /**
     * 题库名称
     */
    private String bankName;

    /**
     * 题库描述
     */
    private String bankDesc;

    /**
     * 题库图标
     */
    private String bankIcon;

    /**
     * 题库类型（0-得分类，1-测评类）
     */
    private Integer bankType;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    private Integer scoringStrategy;

    private static final long serialVersionUID = 1L;
}