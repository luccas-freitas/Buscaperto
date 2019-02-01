package com.luccas.buscaperto.model.offer;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ServerValue;
import com.luccas.buscaperto.model.product.Product;
import com.luccas.buscaperto.model.store.Store;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Offer POJO.
 */
@IgnoreExtraProperties
public class Offer implements Serializable {
    private int id;
    private String name;
    private Product product;
    private Store store;
    private String thumbnail;
    private Object dateCreated;

    public Offer() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Object getDateCreated() {
        if (dateCreated != null) {
            return dateCreated;
        }

        Object dateCreatedObj = ServerValue.TIMESTAMP;
        return dateCreatedObj;
    }

    public void setDateCreated(Object dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", getId());
        result.put("name", getName());
        result.put("product", getProduct());
        result.put("store", getStore());
        result.put("thumbnail", getThumbnail());
        result.put("date", getDateCreated());

        return result;
    }
}
