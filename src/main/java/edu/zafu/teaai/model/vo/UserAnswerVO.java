package edu.zafu.teaai.model.vo;

import cn.hutool.json.JSONUtil;
import edu.zafu.teaai.model.po.UserAnswer;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户答案视图
 *
 * @author ColaBlack
 */
@Data
public class UserAnswerVO implements Serializable {
    /**
     *
     */
    private Long id;

    /**
     * 题库 id
     */
    private Long bankId;

    /**
     * 题库类型（0-得分类，1-测评类）
     */
    private Integer bankType;

    /**
     * 评分策略（0-自定义，1-AI）
     */
    private Integer scoringStrategy;

    /**
     * 用户答案（JSON 数组）
     */
    private List<String> choices;

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
     * @param userAnswerVO 用户答案VO
     * @return 用户答案PO
     */
    public static UserAnswer voToPo(UserAnswerVO userAnswerVO) {
        if (userAnswerVO == null) {
            return null;
        }
        UserAnswer userAnswer = new UserAnswer();
        BeanUtils.copyProperties(userAnswerVO, userAnswer);
        userAnswer.setChoices(JSONUtil.toJsonStr(userAnswerVO.getChoices()));
        return userAnswer;
    }

    /**
     * PO转VO
     *
     * @param userAnswer 用户答案PO
     * @return 用户答案VO
     */
    public static UserAnswerVO poToVo(UserAnswer userAnswer) {
        if (userAnswer == null) {
            return null;
        }
        UserAnswerVO userAnswerVO = new UserAnswerVO();
        BeanUtils.copyProperties(userAnswer, userAnswerVO);
        userAnswerVO.setBankId(userAnswer.getBankid());
        userAnswerVO.setBankType(userAnswer.getBanktype());
        userAnswerVO.setChoices(JSONUtil.toList(userAnswer.getChoices(), String.class));
        return userAnswerVO;
    }
}
