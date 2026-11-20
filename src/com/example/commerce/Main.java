package com.example.commerce;

import com.example.commerce.category.Category;
import com.example.commerce.customer.Customer;
import com.example.commerce.customer.Grade;
import com.example.commerce.product.Product;
import com.example.commerce.system.CommerceSystem;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Customer customer = new Customer("김인성", "kim@example.com", Grade.BRONZE);

        List<Product> electronicsProducts = new ArrayList<>();
        electronicsProducts.add(new Product("Galaxy S25", 1200000, "최신 안드로이드 스마트폰", 50));
        electronicsProducts.add(new Product("iPhone 16", 1350000, "Apple의 최신 스마트폰", 30));
        electronicsProducts.add(new Product("MacBook Pro", 2400000, "M3 칩셋이 탑재된 노트북", 20));
        electronicsProducts.add(new Product("AirPods Pro", 350000, "노이즈 캔슬링 무선 이어폰", 100));

        List<Product> clothingProducts = new ArrayList<>();
        clothingProducts.add(new Product("유니클X 반팔티", 19900, "코튼 100% 베이직 반팔 티셔츠", 80));
        clothingProducts.add(new Product("유니클X 반바지", 29900, "시원한 린넨 블렌드 쇼츠", 60));
        clothingProducts.add(new Product("유니클X 긴팔티", 24900, "부드러운 촉감의 크루넥 긴팔 티셔츠", 70));
        clothingProducts.add(new Product("유니클X 긴바지", 49900, "스트레치 슬림핏 치노 팬츠", 50));

        List<Product> foodProducts = new ArrayList<>();
        foodProducts.add(new Product("한우 1++ 등심 500g", 65000, "횡성 축협 인증 프리미엄 구이용 등심", 25));
        foodProducts.add(new Product("유기농 제주 감귤 3kg", 18000, "산지 직송 고당도 노지 감귤", 100));
        foodProducts.add(new Product("스페셜티 원두 1kg", 32000, "에티오피아 예가체프 홀빈 로스팅 원두", 40));
        foodProducts.add(new Product("프로틴 초코바 12개입", 24000, "개당 단백질 20g 함유 에너지바", 90));

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("전자제품", electronicsProducts));
        categories.add(new Category("의류", clothingProducts));
        categories.add(new Category("식품", foodProducts));

        CommerceSystem commerceSystem = new CommerceSystem(categories);
        commerceSystem.start();
    }
}
