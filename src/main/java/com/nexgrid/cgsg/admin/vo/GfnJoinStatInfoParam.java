package com.nexgrid.cgsg.admin.vo;

import com.nexgrid.cgsg.admin.constants.SystemStatusCode;
import com.nexgrid.cgsg.admin.exception.AdminException;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
public class GfnJoinStatInfoParam {

    @Size(max = 8, message = "date 의 크기가 8보다 큽니다")
    @NotNull(message = "date 의 값이 없습니다.")
    @NotEmpty(message = "date 의 값이 없습니다.")
    private String date;
    private String currentStartDt;
    private String currentEndDt;
    private String lastYearStartDt;
    private String lastYearEndDt;
    private String lastYearField;
    private String prevMonthField;
    private String lastMonthField;
    private String currentMonthField;

    public void initDate() {
        try {
            LocalDate date = LocalDate.parse(this.date, DateTimeFormatter.ofPattern("yyyyMMdd"));

            this.currentStartDt = getPrevMonth(date);
            this.currentEndDt = convertString(date);
            this.lastYearStartDt = getLastYear(date);
            this.lastYearEndDt = getTodayOfLastYear(date);

            this.lastYearField = date.minusYears(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
            this.prevMonthField = date.minusMonths(2).format(DateTimeFormatter.ofPattern("yyyyMM"));
            this.lastMonthField = date.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
            this.currentMonthField = date.format(DateTimeFormatter.ofPattern("yyyyMM"));

        } catch (Exception e) {
            throw new AdminException(SystemStatusCode.INTERNAL_ERROR, e.getMessage());
        }
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String getLastYear(LocalDate now) {
        LocalDate lastYear = now.minusYears(1);
        return convertString(LocalDate.of(lastYear.getYear(), lastYear.getMonth(), 1));
    }

    private String getTodayOfLastYear(LocalDate now) {
        LocalDate lastYear = now.minusYears(1);
        return convertString(lastYear);
    }

    private String getPrevMonth(LocalDate today) {
        LocalDate prev = today.minusMonths(2);
        return convertString(LocalDate.of(prev.getYear(), prev.getMonth(), 1));
    }

    private String convertString(LocalDate date) {
        return DateTimeFormatter.ofPattern("yyyyMMdd").format(date);
    }

}
