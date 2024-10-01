package edu.zafu.teaai.model.enums;

import cn.hutool.core.util.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 审核状态枚举
 *
 * @author ColaBlack
 */
@Getter
@AllArgsConstructor
public enum ReviewStatusEnum {

    /**
     * 待审核
     */
    REVIEWING("待审核", 0),

    /**
     * 通过审核
     */
    PASS("通过", 1),

    /**
     * 审核不通过
     */
    REJECT("拒绝", 2);

    private final String text;

    private final int value;

    /**
     * 根据 value 获取枚举
     *
     * @param value 值
     * @return 枚举
     */
    public static ReviewStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtil.isEmpty(value)) {
            return null;
        }
        for (ReviewStatusEnum anEnum : ReviewStatusEnum.values()) {
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
