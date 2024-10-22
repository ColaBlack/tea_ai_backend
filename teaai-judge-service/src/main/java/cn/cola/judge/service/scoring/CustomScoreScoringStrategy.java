package cn.cola.judge.service.scoring;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import cn.cola.model.question.QuestionContentDTO;
import cn.cola.model.po.Question;
import cn.cola.model.po.QuestionBank;
import cn.cola.model.po.ScoringResult;
import cn.cola.model.po.UserAnswer;
import cn.cola.model.vo.QuestionVO;
import cn.cola.serviceclient.service.QuestionService;
import cn.cola.serviceclient.service.ScoringResultService;


import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * 自定义得分类题库评分策略
 *
 * @author ColaBlack
 */
@ScoringStrategyConfig(bankType = 0, scoringStrategy = 0)
public class CustomScoreScoringStrategy implements ScoringStrategy {

    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;

    @Override
    public UserAnswer doScore(List<String> choices, QuestionBank questionBank) {
        Long bankId = questionBank.getId();
        // 1. 根据 id 查询到题目和题目结果信息（按分数降序排序）
        Question question = questionService.getOne(Wrappers.lambdaQuery(Question.class)
                .eq(Question::getBankid, bankId)
                .orderByDesc(Question::getUpdateTime)
                .last("LIMIT 1"));
        List<ScoringResult> scoringResultList = scoringResultService.list(Wrappers.lambdaQuery(ScoringResult.class).eq(ScoringResult::getBankid, bankId).orderByDesc(ScoringResult::getResultScoreRange));

        // 2. 统计用户的总得分
        int totalScore = 0;
        QuestionVO questionVO = QuestionVO.poToVo(question);
        List<QuestionContentDTO> questionContent = questionVO.getQuestionContent();

        // 遍历题目列表
        for (int i = 0; i < questionContent.size(); i++) {
            QuestionContentDTO questionContentDTO = questionContent.get(i);
            String answer = choices.get(i);
            // 遍历题目中的选项
            for (QuestionContentDTO.Option option : questionContentDTO.getOptions()) {
                // 如果答案和选项的key匹配
                if (option.getKey().equals(answer)) {
                    int score = Optional.of(option.getScore()).orElse(0);
                    totalScore += score;
                }
            }
        }

        // 3. 遍历得分结果，找到第一个用户分数大于得分范围的结果，作为最终结果
        ScoringResult maxScoringResult = scoringResultList.get(0);
        for (ScoringResult scoringResult : scoringResultList) {
            if (totalScore >= scoringResult.getResultScoreRange()) {
                maxScoringResult = scoringResult;
                break;
            }
        }

        // 4. 构造返回值，填充答案对象的属性
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setBankid(bankId);
        userAnswer.setBanktype(questionBank.getBankType());
        userAnswer.setScoringStrategy(questionBank.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        userAnswer.setResultid(maxScoringResult.getId());
        userAnswer.setResultName(maxScoringResult.getResultName());
        userAnswer.setResultDesc(maxScoringResult.getResultDesc());
        userAnswer.setResultPicture(maxScoringResult.getResultPicture());
        userAnswer.setResultScore(totalScore);
        return userAnswer;
    }
}