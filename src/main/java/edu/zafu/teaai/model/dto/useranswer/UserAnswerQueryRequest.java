package edu.zafu.teaai.model.dto.useranswer;

import edu.zafu.teaai.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询用户答案请求
 *
 * @author ColaBlack
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAnswerQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 题库 id
     */
    private Long bankid;

    /**
     * 题库类型（0-得分类，1-测评类）
     */
    private Integer banktype;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    private Integer scoringStrategy;

    /**
     * 用户答案（JSON 数组）
     */
    private String choices;

    /**
     * 评分结果 id
     */
    private Long resultId;

    /**
     * 结果名称
     */
    private String resultName;

    /**
     * 结果描述
     */
    private String resultDesc;

    /**
     * 结果图标
     */
    private String resultPicture;

    /**
     * 得分
     */
    private Integer resultScore;

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 搜索词
     */
    private String searchText;

    private static final long serialVersionUID = 1L;
}