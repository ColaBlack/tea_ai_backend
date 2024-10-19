package scoring;



import common.ErrorCode;
import common.exception.BusinessException;
import model.po.QuestionBank;
import model.po.UserAnswer;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
/**
 * 评分策略执行器
 *
 * @author ColaBlack
 */
@Service
public class ScoringStrategyExecutor {

    @Resource
    private List<ScoringStrategy> scoringStrategyList;


    /**
     * 评分，策略模式
     */
    public UserAnswer doScore(List<String> choiceList, QuestionBank questionBank) throws Exception {
        Integer bankType = questionBank.getBankType();
        Integer scoringStrategy = questionBank.getScoringStrategy();
        if (bankType == null || scoringStrategy == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "未找到匹配的策略");
        }
        // 根据注解获取策略
        for (ScoringStrategy strategy : scoringStrategyList) {
            if (strategy.getClass().isAnnotationPresent(ScoringStrategyConfig.class)) {
                ScoringStrategyConfig scoringStrategyConfig = strategy.getClass().getAnnotation(ScoringStrategyConfig.class);
                if (scoringStrategyConfig.bankType() == bankType && scoringStrategyConfig.scoringStrategy() == scoringStrategy) {
                    return strategy.doScore(choiceList, questionBank);
                }
            }
        }
        throw new BusinessException(ErrorCode.SYSTEM_ERROR, "配置有误，未找到匹配的策略");
    }
}
