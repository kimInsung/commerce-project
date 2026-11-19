package com.example.commerce;

public class Product {
    private String name;
    private int price;
    private String description;
    private int stock;

    public Product(String name, int price, String description, int stock) {
        setName(name);
        setPrice(price);
        setDescription(description);
        setStock(stock);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("상품명은 null이거나 공백일 수 없습니다.");
        }
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        }
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("상품 설명은 null이거나 공백일 수 없습니다.");
        }
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("재고 수량은 0 이상이어야 합니다.");
        }
        this.stock = stock;
    }

    public void decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("차감할 수량은 1 이상이어야 합니다.");
        }
        if (quantity > this.stock) {
            throw new IllegalArgumentException("재고가 부족합니다. (현재 재고: " + this.stock + "개)");
        }
        this.stock -= quantity;
    }
}
