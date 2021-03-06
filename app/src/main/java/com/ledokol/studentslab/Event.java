package com.ledokol.studentslab;

public class Event {

    private String title; // название
    private String text;  // столица
    private int logo; // ресурс флага

    public Event(String title, String text, int logo){
        this.title=title;
        this.text=text;
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

    public int getLogo() {
        return this.logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }
}