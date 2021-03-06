package com.ledokol.studentslab;

public class Event {

    private String title;
    private String text,teacher,teacherName,classroom;
    private int logo;

    public Event(String title, String text, String teacher, String teacherName, String classroom, int logo){
        this.title=title;
        this.text=text;
        this.teacher=teacher;
        this.teacherName=teacherName;
        this.classroom=classroom;
        this.logo =logo;
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

    public String getClassroom() {
        return this.classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public int getLogo() {
        return this.logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }
}