package cn.cola.model.scoringresult;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建评分结果请求
 *
 * @author ColaBlack
 */
@Data
public class ScoringResultAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
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
    private List<String> resultProp;
    /**
     * 结果得分范围，如 60，表示 60及以上的分数命中此结果
     */
    private Integer resultScoreRange;
    /**
     * 题库 id
     */
    private Long bankid;
}