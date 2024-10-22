package cn.cola.model.questionbank;

import lombok.Data;

import java.io.Serializable;

/**
 * 审核请求
 *
 * @author ColaBlack
 */
@Data
public class ReviewRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    private Integer reviewStatus;
    /**
     * 审核信息
     */
    private String reviewMessage;
}