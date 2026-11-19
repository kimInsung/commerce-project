package com.example.commerce;

import java.util.List;
import java.util.Scanner;

public class CommerceSystem {
    private List<Category> categories;

    public CommerceSystem(List<Category> categories) {
        this.categories = categories;
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("[ 실시간 커머스 플랫폼 메인 ]");
                for (int i = 0; i < categories.size(); i++) {
                    System.out.printf("%d. %s%n", i + 1, categories.get(i).getCategoryName());
                }
                System.out.println("0. 종료           | 프로그램 종료");

                System.out.print("번호를 입력하세요: ");
                if (!scanner.hasNextLine()) {
                    break;
                }

                String input = scanner.nextLine().trim();
                int choice;
                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.printf("잘못된 입력입니다. 0~%d 사이의 숫자를 입력해 주세요.%n", categories.size());
                    continue;
                }

                if (choice == 0) {
                    System.out.println("커머스 플랫폼을 종료합니다.");
                    break;
                } else if (choice >= 1 && choice <= categories.size()) {
                    handleCategoryMenu(scanner, categories.get(choice - 1));
                } else {
                    System.out.printf("잘못된 입력입니다. 0~%d 사이의 숫자를 입력해 주세요.%n", categories.size());
                }
            }
        }
    }

    private void handleCategoryMenu(Scanner scanner, Category category) {
        List<Product> products = category.getProducts();
        while (true) {
            System.out.printf("[ %s 카테고리 ]%n", category.getCategoryName());
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                System.out.printf("%d. %-14s | %,9d원 | %s%n",
                        i + 1, product.getName(), product.getPrice(), product.getDescription());
            }
            System.out.println("0. 뒤로가기");

            System.out.print("번호를 입력하세요: ");
            if (!scanner.hasNextLine()) {
                return;
            }

            String input = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.printf("잘못된 입력입니다. 0~%d 사이의 숫자를 입력해 주세요.%n", products.size());
                continue;
            }

            if (choice == 0) {
                return;
            } else if (choice >= 1 && choice <= products.size()) {
                Product selected = products.get(choice - 1);
                System.out.printf("선택한 상품: %s | %,d원 | %s | 재고: %d개%n",
                        selected.getName(), selected.getPrice(), selected.getDescription(), selected.getStock());
                return;
            } else {
                System.out.printf("잘못된 입력입니다. 0~%d 사이의 숫자를 입력해 주세요.%n", products.size());
            }
        }
    }
}
