package edu.zafu.teaai.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评分结果
 *
 * @author ColaBlack
 * @TableName scoring_result
 */
@TableName(value = "scoring_result")
@Data
public class ScoringResult implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 结果名称
     */
    private String resultName;
    /**
     * 结果描述
     */
    private String resultDesc;
    /**
     * 结果图片
     */
    private String resultPicture;
    /**
     * 结果属性集合 JSON
     */
    private String resultProp;
    /**
     * 结果得分范围，如 60，表示 60分及以上的分数命中此结果
     */
    private Integer resultScoreRange;
    /**
     * 题库 id
     */
    private Long bankid;
    /**
     * 创建用户 id
     */
    private Long userid;
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