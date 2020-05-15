package com.example.anamelesscur;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Curriculum extends BaseObservable {
    public String cur_Name;
    public ScheduleArragement Schedule;
    public String teacherName;
    public String Location;

    @Bindable
    public void setCur_Name(String cur_Name) {
        this.cur_Name = cur_Name;
        notifyPropertyChanged(BR.cur_Name);
    }
    @Bindable
    public void setSchedule(ScheduleArragement schedule) {
        Schedule = schedule;
        notifyPropertyChanged(BR.schedule);
    }
    @Bindable
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
        notifyPropertyChanged(BR.teacherName);
    }
    @Bindable
    public void setLocation(String location) {
        Location = location;
        notifyPropertyChanged(BR.location);
    }

    public Curriculum() {
        cur_Name = "测试用课程名";
        Schedule=new ScheduleArragement();
        teacherName="Teacher";
        Location="Location";
    }
}
