package com.codeTeam_3.model;

public class Category {
    private int id;
    private String name;
    private long productCount;

    public Category(int id, String name, long productCount) {
        this.id = id;
        this.name = name;
        this.productCount = productCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getProductCount() {
        return productCount;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", productCount=" + productCount +
                '}';
    }
}
