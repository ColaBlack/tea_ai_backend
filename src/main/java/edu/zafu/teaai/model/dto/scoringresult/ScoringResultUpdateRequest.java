package edu.zafu.teaai.model.dto.scoringresult;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新评分结果请求
 *
 * @author ColaBlack
 */
@Data
public class ScoringResultUpdateRequest implements Serializable {

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
     * 结果得分范围，如 60，表示 60分及以上的分数命中此结果
     */
    private Integer resultScoreRange;
    /**
     * 创建用户 id
     */
    private Long userid;
}