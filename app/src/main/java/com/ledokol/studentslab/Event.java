package com.ledokol.studentslab;

import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.List;

public class Event {

    private String title,token;
    private String text,teacher,teacherName,address,time;
    private int logo;
    private List<String> viewers;
    private Timestamp start,end;

    public Event(String token, String title, String text, String teacher, String teacherName, String classroom, String time, List<String> viewers, int logo, Timestamp start, Timestamp end){
        this.token=token;
        this.title=title;
        this.text=text;
        this.teacher=teacher;
        this.teacherName=teacherName;
        this.address=classroom;
        this.time=time;
        this.viewers=viewers;
        this.logo =logo;
        this.start = start;
        this.end = end;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTeacher() {
        return this.teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTeacherName() {
        return this.teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String classroom) {
        this.address = classroom;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getViewers() {
        return this.viewers;
    }

    public void setViewers(List<String> viewers) {
        this.viewers = viewers;
    }

    public int getLogo() {
        return this.logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }


    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }
}