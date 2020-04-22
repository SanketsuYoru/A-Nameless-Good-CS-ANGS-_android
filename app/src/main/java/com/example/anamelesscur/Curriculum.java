package com.example.anamelesscur;

public class Curriculum {
    public String cur_Name;
    public ScheduleArragement Schedule;
    public String teacherName;
    public String Location;

    public Curriculum() {
        cur_Name = "";
        Schedule=new ScheduleArragement();
        teacherName="";
        Location="";
    }
}
