package edu.zafu.teaai.utils.scoring;


import edu.zafu.teaai.model.po.QuestionBank;
import edu.zafu.teaai.model.po.UserAnswer;

import java.util.List;

/**
 * 评分策略
 *
 * @author ColaBlack
 */
public interface ScoringStrategy {

    /**
     * 评分
     */
    UserAnswer doScore(List<String> choices, QuestionBank questionBank) throws Exception;
}