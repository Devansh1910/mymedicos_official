package com.medical.my_medicos.activities.publications.model;
import com.hishd.tinycart.model.Item;

import java.io.Serializable;
import java.math.BigDecimal;

public class Product implements Item, Serializable {

    private String Title, thumbnail, Author, Type, Category, id, Subject;
    private double Price;
    public Product(String title, String thumbnail, String author, double price, String type, String category, String id, String subject) {
        this.Title = title;
        this.thumbnail = thumbnail;
        this.Author = author;
        this.Price = price;
        this.Type = type;
        this.Category = category;
        this.Subject = subject;
        this.id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail (String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        this.Author = author;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        this.Price = price;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        this.Category = category;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        this.Subject = subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public BigDecimal getItemPrice() {
        return new BigDecimal(Price);
    }

    @Override
    public String getItemName() {
        return Title;
    }
}
