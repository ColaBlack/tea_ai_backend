package cn.cola.judge.service.scoring;


import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import cn.cola.model.po.QuestionBank;
import cn.cola.model.po.UserAnswer;
import org.apache.commons.lang3.StringUtils;
import cn.cola.serviceclient.service.AiService;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 测评类题库的AI评分策略
 * 利用caffeine缓存来提高效率
 *
 * @author ColaBlack
 */
@ScoringStrategyConfig(bankType = 1, scoringStrategy = 1)
public class AiTestScoringStrategy implements ScoringStrategy {

    private final Cache<String, String> answerCacheMap = Caffeine.newBuilder().initialCapacity(1024)
            // 缓存有效期
            .expireAfterAccess(1L, TimeUnit.DAYS).build();
    @Resource
    private AiService aiService;

    @Override
    public UserAnswer doScore(List<String> choices, QuestionBank questionBank) throws Exception {
        Long bankId = questionBank.getId();
        String choicesStr = JSONUtil.toJsonStr(choices);
        // 得到缓存的key
        String cacheKey = DigestUtil.md5Hex("teaAI:" + bankId + ":" + choicesStr);
        // 得到缓存的json
        String json = answerCacheMap.getIfPresent(cacheKey);
        if (StringUtils.isEmpty(json)) {
            // 未命中缓存，调用AI接口进行评分并缓存结果
            json = aiService.aiJudgeTest(choices, questionBank);
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
