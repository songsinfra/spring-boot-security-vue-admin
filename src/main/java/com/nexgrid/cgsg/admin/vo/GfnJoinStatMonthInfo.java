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
        return ((oriData-compareData)/compareData)*100;
    }

    public String getLastMonthRate() {
        return String.format("%.2f%%", lastMonthRate);
    }

    public String getLastYearRate() {
        return String.format("%.2f%%", lastYearRate);
    }
}
