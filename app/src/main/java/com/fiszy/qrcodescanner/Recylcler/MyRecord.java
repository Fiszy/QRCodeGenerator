package com.fiszy.qrcodescanner.Recylcler;

public class MyRecord {

    int id;
    String text;
    String date;

    public MyRecord(int id, String text, String date) {
        this.id = id;
        this.text = text;
        this.date = date;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
