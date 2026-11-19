package com.example.commerce;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Galaxy S25", 1200000, "최신 안드로이드 스마트폰", 50));
        products.add(new Product("iPhone 16", 1350000, "Apple의 최신 스마트폰", 30));
        products.add(new Product("MacBook Pro", 2400000, "M3 칩셋이 탑재된 노트북", 20));
        products.add(new Product("AirPods Pro", 350000, "노이즈 캔슬링 무선 이어폰", 100));

        System.out.println("[ 실시간 커머스 플랫폼 - 전자제품 ]");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.printf("%d. %-14s | %,9d원 | %s%n",
                    i + 1, product.getName(), product.getPrice(), product.getDescription());
        }
        System.out.printf("0. %-12s | %s%n", "종료", "프로그램 종료");

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("번호를 입력하세요: ");
            if (!scanner.hasNextLine()) {
                System.out.println("입력이 없습니다. 프로그램을 종료합니다.");
                return;
            }

            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다. 0~4 사이의 숫자를 입력해 주세요.");
                return;
            }

            if (choice == 0) {
                System.out.println("커머스 플랫폼을 종료합니다.");
            } else if (choice >= 1 && choice <= products.size()) {
                Product selected = products.get(choice - 1);
                System.out.printf("선택한 상품: %s | %,d원 | %s | 재고: %d개%n",
                        selected.getName(), selected.getPrice(), selected.getDescription(), selected.getStock());
            } else {
                System.out.println("잘못된 입력입니다. 0~4 사이의 숫자를 입력해 주세요.");
            }
        }
    }
}
