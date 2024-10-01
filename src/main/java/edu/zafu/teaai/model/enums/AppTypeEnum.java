package edu.zafu.teaai.model.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 题库类型枚举
 *
 * @author ColaBlack
 */
@AllArgsConstructor
@Getter
public enum AppTypeEnum {

    /**
     * 得分类题库
     */
    SCORE("得分类", 0),

    /**
     * 测评类题库
     */
    TEST("测评类", 1);

    private final String text;

    private final int value;

    /**
     * 根据 value 获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static AppTypeEnum getEnumByValue(Integer value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (AppTypeEnum anEnum : AppTypeEnum.values()) {
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
