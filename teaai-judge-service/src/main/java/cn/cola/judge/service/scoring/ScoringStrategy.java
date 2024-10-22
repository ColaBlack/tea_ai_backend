package cn.cola.judge.service.scoring;


import cn.cola.model.po.QuestionBank;
import cn.cola.model.po.UserAnswer;

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