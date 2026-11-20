package com.example.commerce.category;

import com.example.commerce.product.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Category {
    private String categoryName;
    private List<Product> products;

    public Category(String categoryName, List<Product> products) {
        setCategoryName(categoryName);
        setProducts(products);
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("카테고리명은 null이거나 공백일 수 없습니다.");
        }
        this.categoryName = categoryName;
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    public void setProducts(List<Product> products) {
        if (products == null) {
            throw new IllegalArgumentException("상품 목록은 null일 수 없습니다.");
        }
        this.products = new ArrayList<>(products);
    }

    public boolean containsProductName(String name) {
        if (name == null) {
            return false;
        }
        String trimmedName = name.trim();
        for (Product product : products) {
            if (product.getName().trim().equalsIgnoreCase(trimmedName)) {
                return true;
            }
        }
        return false;
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("추가할 상품은 null일 수 없습니다.");
        }
        if (containsProductName(product.getName())) {
            throw new IllegalArgumentException("이미 존재하는 상품명입니다: " + product.getName());
        }
        this.products.add(product);
    }

    public Product findProductByName(String name) {
        if (name == null) {
            return null;
        }
        String trimmedName = name.trim();
        for (Product product : products) {
            if (product.getName().trim().equalsIgnoreCase(trimmedName)) {
                return product;
            }
        }
        return null;
    }

    public boolean removeProduct(Product product) {
        if (product == null) {
            return false;
        }
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            if (p == product || p.getName().trim().equalsIgnoreCase(product.getName().trim())) {
                products.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<Product> filterProductsUpToPrice(int maxPrice) {
        return products.stream()
                .filter(p -> p.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public List<Product> filterProductsAbovePrice(int minPrice) {
        return products.stream()
                .filter(p -> p.getPrice() > minPrice)
                .collect(Collectors.toList());
    }
}
