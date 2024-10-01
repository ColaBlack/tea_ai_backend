package edu.zafu.teaai.model.dto.questionbank;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 更新题库请求
 *
 * @author ColaBlack
 */
@Data
public class QuestionBankUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

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

    /**
     * 审核状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;

    /**
     * 审核人 id
     */
    private Long reviewerid;

    /**
     * 审核时间
     */
    private Date reviewTime;

    private static final long serialVersionUID = 1L;
}