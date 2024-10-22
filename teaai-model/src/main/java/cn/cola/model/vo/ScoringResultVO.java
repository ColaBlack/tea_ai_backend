package cn.cola.model.vo;


import cn.cola.model.po.ScoringResult;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 评分结果视图
 *
 * @author ColaBlack
 */
@Data
public class ScoringResultVO implements Serializable {

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
     * 题库 id
     */
    private Long bankId;

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
     * 创建用户信息
     */
    private UserVO user;

    /**
     * VO转PO
     *
     * @param scoringResultVO 答题结果VO
     * @return 答题结果PO
     */
    public static ScoringResult voToPo(ScoringResultVO scoringResultVO) {
        if (scoringResultVO == null) {
            return null;
        }
        ScoringResult scoringResult = new ScoringResult();
        BeanUtils.copyProperties(scoringResultVO, scoringResult);
        scoringResult.setResultProp(JSONUtil.toJsonStr(scoringResultVO.getResultProp()));
        return scoringResult;
    }

    /**
     * PO转VO
     *
     * @param scoringResult 答题结果PO
     * @return 答题结果VO
     */
    public static ScoringResultVO objToVo(ScoringResult scoringResult) {
        if (scoringResult == null) {
            return null;
        }
        ScoringResultVO scoringResultVO = new ScoringResultVO();
        BeanUtils.copyProperties(scoringResult, scoringResultVO);
        scoringResultVO.setResultProp(JSONUtil.toList(scoringResult.getResultProp(), String.class));
        return scoringResultVO;
    }
}
