package com.isuo.yw2application.mode.bean.work;

/**
 * 观察统计
 * Created by zhangan on 2018/4/17.
 */

public class WorkMonitorState {

    private int dayFinishCount;
    private int dayAllCount;
    private int monthFinishCount;
    private int monthAllCount;
    private int weekFinishCount;
    private int weekAllCount;

    public int getDayFinishCount() {
        return dayFinishCount;
    }

    public void setDayFinishCount(int dayFinishCount) {
        this.dayFinishCount = dayFinishCount;
    }

    public int getDayAllCount() {
        return dayAllCount;
    }

    public void setDayAllCount(int dayAllCount) {
        this.dayAllCount = dayAllCount;
    }

    public int getMonthFinishCount() {
        return monthFinishCount;
    }

    public void setMonthFinishCount(int monthFinishCount) {
        this.monthFinishCount = monthFinishCount;
    }

    public int getMonthAllCount() {
        return monthAllCount;
    }

    public void setMonthAllCount(int monthAllCount) {
        this.monthAllCount = monthAllCount;
    }

    public int getWeekFinishCount() {
        return weekFinishCount;
    }

    public void setWeekFinishCount(int weekFinishCount) {
        this.weekFinishCount = weekFinishCount;
    }

    public int getWeekAllCount() {
        return weekAllCount;
    }

    public void setWeekAllCount(int weekAllCount) {
        this.weekAllCount = weekAllCount;
    }
}
