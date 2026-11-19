package com.example.commerce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class CommerceSystem {
    private List<Category> categories;
    private Cart cart = new Cart();
    private AdminManager adminManager;

    public CommerceSystem(List<Category> categories) {
        setCategories(categories);
    }

    public Cart getCart() {
        return cart;
    }

    public AdminManager getAdminManager() {
        return adminManager;
    }

    public List<Category> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public void setCategories(List<Category> categories) {
        if (categories == null) {
            throw new IllegalArgumentException("카테고리 목록은 null일 수 없습니다.");
        }
        this.categories = new ArrayList<>(categories);
        this.adminManager = new AdminManager(this.categories, this.cart);
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("[ 실시간 커머스 플랫폼 메인 ]");
                for (int i = 0; i < categories.size(); i++) {
                    System.out.printf("%d. %s%n", i + 1, categories.get(i).getCategoryName());
                }
                System.out.println("0. 종료           | 프로그램 종료");

                if (!cart.isEmpty()) {
                    System.out.println();
                    System.out.println("[ 주문 관리 ]");
                    System.out.println("4. 장바구니 확인    | 장바구니를 확인 후 주문합니다.");
                    System.out.println("5. 주문 취소       | 진행중인 주문을 취소합니다.");
                }

                System.out.println("6. 관리자 모드");

                System.out.print("번호를 입력하세요: ");
                if (!scanner.hasNextLine()) {
                    break;
                }

                String input = scanner.nextLine().trim();
                int choice;
                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("잘못된 입력입니다. 올바른 번호를 입력해 주세요.");
                    continue;
                }

                if (choice == 0) {
                    System.out.println("커머스 플랫폼을 종료합니다.");
                    break;
                } else if (choice >= 1 && choice <= categories.size()) {
                    handleCategoryMenu(scanner, categories.get(choice - 1));
                } else if (choice == 4) {
                    if (cart.isEmpty()) {
                        System.out.println("장바구니가 비어 있어 주문 관리 기능을 사용할 수 없습니다.");
                    } else {
                        handleCartMenu(scanner);
                    }
                } else if (choice == 5) {
                    if (cart.isEmpty()) {
                        System.out.println("장바구니가 비어 있어 주문 관리 기능을 사용할 수 없습니다.");
                    } else {
                        handleCancelOrderMenu(scanner);
                    }
                } else if (choice == 6) {
                    adminManager.start(scanner);
                } else {
                    System.out.println("잘못된 입력입니다. 올바른 번호를 입력해 주세요.");
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
                System.out.printf("\"%s | %,d원 | %s\"%n",
                        selected.getName(), selected.getPrice(), selected.getDescription());
                System.out.println("위 상품을 장바구니에 추가하시겠습니까?");
                System.out.println("1. 확인        2. 취소");

                while (true) {
                    System.out.print("번호를 입력하세요: ");
                    if (!scanner.hasNextLine()) {
                        return;
                    }
                    String confirmInput = scanner.nextLine().trim();
                    if ("1".equals(confirmInput)) {
                        try {
                            cart.addProduct(selected, 1);
                            System.out.printf("%s가 장바구니에 추가되었습니다.%n", selected.getName());
                        } catch (IllegalStateException | IllegalArgumentException e) {
                            System.out.printf("재고가 부족하여 장바구니에 담을 수 없습니다. (현재 재고: %d개)%n", selected.getStock());
                        }
                        return;
                    } else if ("2".equals(confirmInput)) {
                        System.out.println("장바구니 추가가 취소되었습니다.");
                        return;
                    } else {
                        System.out.println("잘못된 입력입니다. 1(확인) 또는 2(취소)를 입력해 주세요.");
                    }
                }
            } else {
                System.out.printf("잘못된 입력입니다. 0~%d 사이의 숫자를 입력해 주세요.%n", products.size());
            }
        }
    }

    private void handleCartMenu(Scanner scanner) {
        if (cart.isEmpty()) {
            System.out.println("장바구니가 비어 있어 주문 관리 기능을 사용할 수 없습니다.");
            return;
        }

        System.out.println("아래와 같이 주문 하시겠습니까?");
        System.out.println();
        System.out.println("[ 장바구니 내역 ]");
        for (CartItem item : cart.getItems()) {
            Product p = item.getProduct();
            System.out.printf("%s | %,d원 | %s | 수량: %d개%n",
                    p.getName(), p.getPrice(), p.getDescription(), item.getQuantity());
        }
        System.out.println();
        System.out.println("[ 총 주문 금액 ]");
        System.out.printf("%,d원%n", cart.calculateTotalPrice());
        System.out.println();
        System.out.println("1. 주문 확정      2. 메인으로 돌아가기");

        while (true) {
            System.out.print("번호를 입력하세요: ");
            if (!scanner.hasNextLine()) {
                return;
            }
            String input = scanner.nextLine().trim();
            if ("1".equals(input)) {
                for (CartItem item : cart.getItems()) {
                    if (item.getQuantity() > item.getProduct().getStock()) {
                        System.out.printf("'%s'의 재고가 부족하여 주문을 완료할 수 없습니다. (현재 재고: %d개, 요청 수량: %d개)%n",
                                item.getProduct().getName(), item.getProduct().getStock(), item.getQuantity());
                        return;
                    }
                }

                System.out.printf("주문이 완료되었습니다! 총 금액: %,d원%n", cart.calculateTotalPrice());
                for (CartItem item : cart.getItems()) {
                    Product p = item.getProduct();
                    int beforeStock = p.getStock();
                    p.decreaseStock(item.getQuantity());
                    int afterStock = p.getStock();
                    System.out.printf("%s 재고가 %d개 → %d개로 업데이트되었습니다.%n", p.getName(), beforeStock, afterStock);
                }
                cart.clear();
                return;
            } else if ("2".equals(input)) {
                return;
            } else {
                System.out.println("잘못된 입력입니다. 1(주문 확정) 또는 2(메인으로 돌아가기)를 입력해 주세요.");
            }
        }
    }

    private void handleCancelOrderMenu(Scanner scanner) {
        if (cart.isEmpty()) {
            System.out.println("장바구니가 비어 있어 주문 관리 기능을 사용할 수 없습니다.");
            return;
        }

        System.out.println("진행중인 주문을 취소하고 장바구니를 비우시겠습니까?");
        System.out.println("1. 확인        2. 취소");

        while (true) {
            System.out.print("번호를 입력하세요: ");
            if (!scanner.hasNextLine()) {
                return;
            }
            String input = scanner.nextLine().trim();
            if ("1".equals(input)) {
                cart.clear();
                System.out.println("진행중인 주문이 취소되어 장바구니가 비워졌습니다.");
                return;
            } else if ("2".equals(input)) {
                return;
            } else {
                System.out.println("잘못된 입력입니다. 1(확인) 또는 2(취소)를 입력해 주세요.");
            }
        }
    }
}
