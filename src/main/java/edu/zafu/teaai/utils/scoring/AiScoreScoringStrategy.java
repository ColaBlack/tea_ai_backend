package edu.zafu.teaai.utils.scoring;

import edu.zafu.teaai.common.ErrorCode;
import edu.zafu.teaai.common.exception.BusinessException;
import edu.zafu.teaai.model.po.QuestionBank;
import edu.zafu.teaai.model.po.UserAnswer;

import java.util.List;

/**
 * 得分类题库AI评分策略
 *
 * @author ColaBlack
 */
@ScoringStrategyConfig(bankType = 0, scoringStrategy = 1)
public class AiScoreScoringStrategy implements ScoringStrategy {

    @Override
    public UserAnswer doScore(List<String> choices, QuestionBank questionBank) {
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "暂不支持AI评分");
    }
}
