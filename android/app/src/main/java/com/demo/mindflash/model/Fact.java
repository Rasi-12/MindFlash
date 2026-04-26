package com.demo.mindflash.model;

public class Fact {
    private String id;
    private String text;
    private String category;
    private String date;

    public Fact() {}

    public String getId() { return id; }
    public String getText() { return text; }
    public String getCategory() { return category; }
    public String getDate() { return date; }

    public void setId(String id) { this.id = id; }
    public void setText(String text) { this.text = text; }
    public void setCategory(String category) { this.category = category; }
    public void setDate(String date) { this.date = date; }
}