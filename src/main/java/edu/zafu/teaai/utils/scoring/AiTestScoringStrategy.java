package edu.zafu.teaai.utils.scoring;

import edu.zafu.teaai.model.po.QuestionBank;
import edu.zafu.teaai.model.po.UserAnswer;
import edu.zafu.teaai.service.AiService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测评类题库的AI评分策略
 *
 * @author ColaBlack
 */
@ScoringStrategyConfig(bankType = 1, scoringStrategy = 1)
public class AiTestScoringStrategy implements ScoringStrategy {

    @Resource
    private AiService aiService;

    @Override
    public UserAnswer doScore(List<String> choices, QuestionBank questionBank) throws Exception {
        return aiService.aiJudge(choices, questionBank);
    }

}
