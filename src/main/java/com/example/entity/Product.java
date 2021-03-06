package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotBlank(message = "Tên không thể để trống.")
    @Size(min = 8, message = "Tên phải dài hơn 8 ký tự.")
    private String name;
    private String imgUrl;
    @Min(1)
    private String price;
    private long createdTime;
    private int status; // 1. Đang bán 2. Dừng bán 0. Đã xoá.

    public Product() {
        this.status = 1;
    }

    public Product(@NotNull @Size(min = 8) String name, String imgUrl, @Min(1) String price) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.price = price;
        this.createdTime = System.currentTimeMillis();
        this.status = 1;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}
