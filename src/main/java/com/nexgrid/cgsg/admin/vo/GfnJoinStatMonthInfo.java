package com.nexgrid.cgsg.admin.vo;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GfnJoinStatMonthInfo {
    private String joinType;
    private int prevMonthData;
    private int lastMonthData;
    private float lastMonthRate;
    private int lastYearData;
    private int currentMonthData;
    private float lastYearRate;

    public void doChangeRate() {
        this.lastMonthRate = calculateChangeRate(lastMonthData, prevMonthData);
        this.lastYearRate = calculateChangeRate(currentMonthData, lastYearData);
    }

    private float calculateChangeRate(float oriData, float compareData) {
        float result = ((oriData - compareData) / compareData) * 100;
        if(Double.isNaN(result)) return 0;
        if(Double.isInfinite(result)) return 100;

        return result;
    }

    public String getLastMonthRate() {
        return String.format("%.2f%%", lastMonthRate);
    }

    public String getLastYearRate() {
        return String.format("%.2f%%", lastYearRate);
    }
}
