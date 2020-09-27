package com.example.attendancesystem.classes;

public class Student {

    private String sName, sId, sPassword, sClasses, sCurrentAttendance;

    public Student(String sName, String sId, String sPassword, String sClasses, String sCurrentAttendance) {
        this.sName = sName;
        this.sId = sId;
        this.sPassword = sPassword;
        this.sClasses = sClasses;
        this.sCurrentAttendance = sCurrentAttendance;
    }

    public Student(){

    }

    public String getsCurrentAttendance() {
        return sCurrentAttendance;
    }

    public void setsCurrentAttendance(String sCurrentAttendance) {
        this.sCurrentAttendance = sCurrentAttendance;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getsPassword() {
        return sPassword;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword;
    }

    public String getsClasses() {
        return sClasses;
    }

    public void setsClasses(String sClasses) {
        this.sClasses = sClasses;
    }
}

