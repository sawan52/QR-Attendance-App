package com.example.attendancesystem.classes;

public class Teacher {

    private String tName, tId, tSubject, tPassword;

    public Teacher(String tName, String tId, String tSubject, String tPassword) {
        this.tName = tName;
        this.tId = tId;
        this.tSubject = tSubject;
        this.tPassword = tPassword;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String gettSubject() {
        return tSubject;
    }

    public void settSubject(String tSubject) {
        this.tSubject = tSubject;
    }

    public String gettPassword() {
        return tPassword;
    }

    public void settPassword(String tPassword) {
        this.tPassword = tPassword;
    }

}

