package edu.zafu.teaai.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 题库表
 *
 * @author ColaBlack
 * @TableName question_bank
 */
@TableName(value = "question_bank")
@Data
public class QuestionBank implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
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
    /**
     * 创建用户 id
     */
    private Long userId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 是否删除
     */
    private Integer isDelete;
}