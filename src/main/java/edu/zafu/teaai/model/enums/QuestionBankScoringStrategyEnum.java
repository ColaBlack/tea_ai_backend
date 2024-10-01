package edu.zafu.teaai.model.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题库评分策略枚举
 *
 * @author ColaBlack
 */
@Getter
@AllArgsConstructor
public enum QuestionBankScoringStrategyEnum {

    /**
     * 自定义评分策略
     */
    CUSTOM("自定义", 0),

    /**
     * AI评分策略
     */
    AI("AI", 1);

    private final String text;

    private final int value;

    /**
     * 根据 value 获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static QuestionBankScoringStrategyEnum getEnumByValue(Integer value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (QuestionBankScoringStrategyEnum anEnum : QuestionBankScoringStrategyEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 获取值列表
     *
     * @return 值列表
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }
}
