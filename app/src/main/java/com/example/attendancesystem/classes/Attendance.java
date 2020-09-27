package com.example.attendancesystem.classes;

public class Attendance {

    private String teacherName, teacherSubject, classTiming, attendanceMarkedTime, name, rollNo, percent;

    public Attendance() {
    }

    public Attendance(String teacherName, String teacherSubject, String classTiming, String attendanceMarkedTime) {
        this.teacherName = teacherName;
        this.teacherSubject = teacherSubject;
        this.classTiming = classTiming;
        this.attendanceMarkedTime = attendanceMarkedTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherSubject() {
        return teacherSubject;
    }

    public void setTeacherSubject(String teacherSubject) {
        this.teacherSubject = teacherSubject;
    }

    public String getClassTiming() {
        return classTiming;
    }

    public void setClassTiming(String classTiming) {
        this.classTiming = classTiming;
    }

    public String getAttendanceMarkedTime() {
        return attendanceMarkedTime;
    }

    public void setAttendanceMarkedTime(String attendanceMarkedTime) {
        this.attendanceMarkedTime = attendanceMarkedTime;
    }
}

