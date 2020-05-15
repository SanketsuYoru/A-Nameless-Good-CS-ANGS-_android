package com.example.anamelesscur;

public class ScheduleArragement {
    public String day;
    public int dailyClassBegin;
    public int dailyClassEnd;
    public int weekBegin;
    public int weekEnd;
    public ScheduleArragement(){
        day="";
        dailyClassBegin=0;
        dailyClassEnd=0;
        weekBegin=0;
        weekEnd=0;
    }

    @Override
    public String toString() {
        return "ScheduleArragement{" +
                "day=" + day  +
                ", dailyClassBegin=" + dailyClassBegin +
                ", dailyClassEnd=" + dailyClassEnd +
                ", weekBegin=" + weekBegin +
                ", weekEnd=" + weekEnd +
                '}';
    }
}
