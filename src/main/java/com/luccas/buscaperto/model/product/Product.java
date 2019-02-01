package com.luccas.buscaperto.model.product;

import com.luccas.buscaperto.model.category.Category;
import com.luccas.buscaperto.model.thumbnail.Thumbnail;

import java.io.Serializable;

/**
 * Product POJO.
 */

public class Product implements Serializable {
    private int id;
    private String name;
    private String shortName;
    private Double priceMin;
    private Thumbnail thumbnail;
    private Category category;

    public Product() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Double getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(Double priceMin) {
        this.priceMin = priceMin;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
