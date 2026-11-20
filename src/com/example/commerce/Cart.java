package com.example.commerce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public int getQuantityOf(Product product) {
        if (product == null) {
            return 0;
        }
        for (CartItem item : items) {
            if (item.getProduct().equals(product) || item.getProduct().getName().equals(product.getName())) {
                return item.getQuantity();
            }
        }
        return 0;
    }

    public void addProduct(Product product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("상품은 null일 수 없습니다.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("추가할 수량은 1 이상이어야 합니다.");
        }

        int currentQuantity = getQuantityOf(product);
        if (currentQuantity + quantity > product.getStock()) {
            throw new IllegalStateException("재고가 부족합니다.");
        }

        for (CartItem item : items) {
            if (item.getProduct().equals(product) || item.getProduct().getName().equals(product.getName())) {
                item.addQuantity(quantity);
                return;
            }
        }
        items.add(new CartItem(product, quantity));
    }

    public int calculateTotalPrice() {
        int total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public void clear() {
        items.clear();
    }

    public boolean removeProduct(Product product) {
        if (product == null) {
            return false;
        }
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            if (item.getProduct().equals(product) || item.getProduct().getName().trim().equalsIgnoreCase(product.getName().trim())) {
                items.remove(i);
                return true;
            }
        }
        return false;
    }

    public boolean removeProductByName(String productName) {
        if (productName == null) {
            return false;
        }
        String targetName = productName.trim();
        int beforeSize = items.size();
        this.items = items.stream()
                .filter(item -> !item.getProduct().getName().trim().equalsIgnoreCase(targetName))
                .collect(Collectors.toList());
        return items.size() < beforeSize;
    }
}
