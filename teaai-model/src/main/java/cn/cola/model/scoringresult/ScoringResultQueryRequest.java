package cn.cola.model.scoringresult;


import cn.cola.model.PageRequest;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询评分结果请求
 *
 * @author ColaBlack
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ScoringResultQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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
     * 搜索词
     */
    private String searchText;
}