package com.example.builder;

public class ProductBuilder {

    private int id;
    private String name;
    private String imgUrl;
    private String price;
    private long createdTime;
    private int status; // 1. Đang bán 2. Dừng bán 0. Đã xoá.


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getPrice() {
        return price;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public int getStatus() {
        return status;
    }

    public ProductBuilder(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.imgUrl = builder.imgUrl;
        this.price = builder.price;
        this.createdTime = builder.createdTime;
        this.status = builder.status;
    }

    public static class Builder {
        private int id;
        private String name;
        private String imgUrl;
        private String price;
        private long createdTime;
        private int status; // 1. Đang bán 2. Dừng bán 0. Đã xoá.

        public Builder() {
        }

        public int getId() {
            return id;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public Builder setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
            return this;
        }

        public String getPrice() {
            return price;
        }

        public Builder setPrice(String price) {
            this.price = price;
            return this;
        }

        public long getCreatedTime() {
            return createdTime;
        }

        public Builder setCreatedTime(long createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public int getStatus() {
            return status;
        }

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public ProductBuilder build() {
            return new ProductBuilder(this);
        }
    }
}
