package com.fib.ecny.base.dto.wallet.trans;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Map;
import java.util.Set;

/**
 * 冲正标志
 */
@Getter
public enum ReversalFlagEnum {
    //正常交易
    NORMAL("N", "正常交易/未冲正/未调账"),
    PROCESSING("P", "处理中"),
    REVERSED("R", "已冲正"),
    ADJUSTED("A", "已调账");

    @EnumValue
    @JsonValue
    private final String code;
    private final String desc;

    ReversalFlagEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static ReversalFlagEnum getByCode(String code) {
        for (ReversalFlagEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        throw new IllegalArgumentException("非法冲正标志:" + code);
    }

    private static final Map<ReversalFlagEnum, Set<ReversalFlagEnum>> TRANSITION_MAP = Map.of(
            NORMAL, Set.of(PROCESSING),
            PROCESSING, Set.of(REVERSED, ADJUSTED, NORMAL),
            REVERSED, Set.of(),
            ADJUSTED, Set.of()
    );

    /**
     * 判断当前标记是否允许流转至目标状态
     * <p>流转规则：
     * <ul>
     *     <li>NORMAL：仅可流转到 PROCESSING（发起调整）</li>
     *     <li>PROCESSING：可流转到 REVERSED / ADJUSTED（调整成功）、NORMAL（调整失败撤回）</li>
     *     <li>REVERSED / ADJUSTED：终态，禁止任何转出</li>
     * </ul>
     *
     * @param target 目标状态
     * @return true=允许流转
     */
    public boolean canTransferTo(ReversalFlagEnum target) {
        if (this == target) {
            // 相同状态，不允许重复流转（避免重复回调、重复更新）
            return false;
        }
        return TRANSITION_MAP.get(this).contains(target);
    }

    /**
     * 是否调整终态：已冲正 / 已调账
     */
    public boolean isAdjustFinal() {
        return this == REVERSED || this == ADJUSTED;
    }

    /**
     * 是否处于调整处理中
     */
    public boolean isProcessing() {
        return this == PROCESSING;
    }

    /**
     * 是否允许发起冲正/调账申请
     */
    public boolean allowInitiateAdjust() {
        return this == NORMAL;
    }
}
