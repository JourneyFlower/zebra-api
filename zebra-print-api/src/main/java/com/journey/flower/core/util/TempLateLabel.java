package com.journey.flower.core.util;

import lombok.AllArgsConstructor;

/**
 * 模板
 */
@AllArgsConstructor
public enum TempLateLabel {
    OTHER(0, "未定义"),
    /**
     * 电解铅
     */
    ELECTROLYTIC_LEAD(1, "电解铅"),
    /**
     * 板栅
     */
    GRID_BOARD(2, "板栅"),
    /**
     * 生板
     */
    PRODUCE_PLATE(3, "生板"),
    /**
     * 极板
     */
    POLAR_PLATE(4, "极板"),
    /**
     * 铅带
     */
    LEAD_RIBBON(5, "铅带");

    private int code;
    private String desc;

    /**
     * 由code 获取枚举类型
     *
     * @param code
     * @return
     */
    public static TempLateLabel getByCode(int code) {
        for (TempLateLabel tempLateLabel : values()) {
            if (tempLateLabel.code == code) {
                return tempLateLabel;
            }
        }
        return OTHER;
    }
}
