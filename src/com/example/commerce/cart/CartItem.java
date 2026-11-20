package com.example.commerce.cart;

import com.example.commerce.product.Product;

public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        setProduct(product);
        setQuantity(quantity);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("상품은 null일 수 없습니다.");
        }
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
        this.quantity = quantity;
    }

    public void addQuantity(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("추가할 수량은 1 이상이어야 합니다.");
        }
        this.quantity += amount;
    }

    public int getTotalPrice() {
        return product.getPrice() * quantity;
    }
}
