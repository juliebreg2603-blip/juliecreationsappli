package com.example.juliecreations.model;

import com.google.gson.annotations.SerializedName;

public class Product {
    private int id;
    private String title;
    private String description;
    private float price;
    private String category;

    @SerializedName("isPersonalizable")
    private boolean isPersonalizable;

    private String imageName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isPersonalizable() {
        return isPersonalizable;
    }

    public void setPersonalizable(boolean personalizable) {
        isPersonalizable = personalizable;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}