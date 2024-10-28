package edu.zafu.teaai.utils.scoring;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import edu.zafu.teaai.model.dto.question.QuestionContentDTO;
import edu.zafu.teaai.model.po.Question;
import edu.zafu.teaai.model.po.QuestionBank;
import edu.zafu.teaai.model.po.ScoringResult;
import edu.zafu.teaai.model.po.UserAnswer;
import edu.zafu.teaai.model.vo.QuestionVO;
import edu.zafu.teaai.service.AiService;
import edu.zafu.teaai.service.QuestionService;
import edu.zafu.teaai.service.ScoringResultService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 得分类题库AI评分策略
 *
 * @author ColaBlack
 */
@ScoringStrategyConfig(bankType = 0, scoringStrategy = 1)
public class AiScoreScoringStrategy implements ScoringStrategy {

    @Resource
    private QuestionService questionService;

    @Resource
    private ScoringResultService scoringResultService;

    private final Cache<String, String> answerCacheMap = Caffeine.newBuilder()
            .initialCapacity(1024)
            // 缓存有效期
            .expireAfterAccess(1L, TimeUnit.DAYS)
            .build();
    @Resource
    private AiService aiService;

    @Override
    public UserAnswer doScore(List<String> choices, QuestionBank questionBank) throws Exception {
        //得分类题库根本不需要AI打分，直接计算得分的总和即可
        //此处复用了自定义题库的打分代码

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

        // 得到缓存的key
        String cacheKey = DigestUtil.md5Hex("teaAI:" + bankId + ":" + "totalScore:" + totalScore);
        // 得到缓存的json
        String json = answerCacheMap.getIfPresent(cacheKey);
        if (StringUtils.isEmpty(json)) {
            // 未命中缓存，调用AI接口进行评分并缓存结果
            json = aiService.aiJudgeScore(totalScore, questionBank);
            answerCacheMap.put(cacheKey, json);
        }
        UserAnswer userAnswer = JSONUtil.toBean(json, UserAnswer.class);
        userAnswer.setBankid(bankId);
        userAnswer.setBanktype(questionBank.getBankType());
        userAnswer.setScoringStrategy(questionBank.getScoringStrategy());
        userAnswer.setChoices(JSONUtil.toJsonStr(choices));
        return userAnswer;
    }
}