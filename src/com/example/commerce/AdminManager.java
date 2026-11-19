package com.example.commerce;

import java.util.List;
import java.util.Scanner;

public class AdminManager {
    private final String adminPassword = "admin123";
    private List<Category> categories;
    private Cart cart;

    public AdminManager(List<Category> categories, Cart cart) {
        if (categories == null || cart == null) {
            throw new IllegalArgumentException("카테고리와 장바구니는 null일 수 없습니다.");
        }
        this.categories = categories;
        this.cart = cart;
    }

    public void start(Scanner scanner) {
        if (!authenticate(scanner)) {
            return;
        }

        while (true) {
            System.out.println();
            System.out.println("[ 관리자 모드 ]");
            System.out.println("1. 상품 추가");
            System.out.println("2. 상품 수정");
            System.out.println("3. 상품 삭제");
            System.out.println("0. 메인으로 돌아가기");

            System.out.print("번호를 입력하세요: ");
            if (!scanner.hasNextLine()) {
                return;
            }

            String input = scanner.nextLine().trim();
            if ("0".equals(input)) {
                System.out.println("관리자 모드를 종료하고 메인 메뉴로 돌아갑니다.");
                return;
            } else if ("1".equals(input)) {
                handleAddProduct(scanner);
            } else if ("2".equals(input)) {
                handleUpdateProduct(scanner);
            } else if ("3".equals(input)) {
                handleDeleteProduct(scanner);
            } else {
                System.out.println("잘못된 입력입니다. 0~3 사이의 숫자를 입력해 주세요.");
            }
        }
    }

    private boolean authenticate(Scanner scanner) {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("관리자 비밀번호를 입력하세요: ");
            if (!scanner.hasNextLine()) {
                return false;
            }
            String password = scanner.nextLine().trim();
            if (adminPassword.equals(password)) {
                return true;
            }
            attempts++;
            if (attempts < 3) {
                System.out.printf("비밀번호가 일치하지 않습니다. (남은 횟수: %d회)%n", 3 - attempts);
            }
        }
        System.out.println("비밀번호를 3회 이상 틀려 메인 메뉴로 돌아갑니다.");
        return false;
    }

    private void handleAddProduct(Scanner scanner) {
        System.out.println();
        System.out.println("[ 카테고리 선택 ]");
        for (int i = 0; i < categories.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, categories.get(i).getCategoryName());
        }
        System.out.println("0. 취소");

        System.out.print("번호를 입력하세요: ");
        if (!scanner.hasNextLine()) {
            return;
        }

        String catInput = scanner.nextLine().trim();
        int catChoice;
        try {
            catChoice = Integer.parseInt(catInput);
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다. 상품 추가를 취소합니다.");
            return;
        }

        if (catChoice == 0) {
            System.out.println("상품 추가가 취소되었습니다.");
            return;
        }
        if (catChoice < 1 || catChoice > categories.size()) {
            System.out.println("존재하지 않는 카테고리 번호입니다. 상품 추가를 취소합니다.");
            return;
        }

        Category targetCategory = categories.get(catChoice - 1);

        System.out.print("상품명을 입력하세요: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("상품명은 공백일 수 없습니다. 상품 추가를 취소합니다.");
            return;
        }
        if (targetCategory.containsProductName(name)) {
            System.out.println("이미 존재하는 상품명입니다. 상품 추가를 중단합니다.");
            return;
        }

        System.out.print("가격을 입력하세요: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String priceInput = scanner.nextLine().trim();
        int price;
        try {
            price = Integer.parseInt(priceInput);
            if (price < 0) {
                System.out.println("가격은 0원 이상이어야 합니다. 상품 추가를 취소합니다.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("가격은 숫자만 입력 가능합니다. 상품 추가를 취소합니다.");
            return;
        }

        System.out.print("상품 설명을 입력하세요: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            System.out.println("상품 설명은 공백일 수 없습니다. 상품 추가를 취소합니다.");
            return;
        }

        System.out.print("재고수량을 입력하세요: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String stockInput = scanner.nextLine().trim();
        int stock;
        try {
            stock = Integer.parseInt(stockInput);
            if (stock < 0) {
                System.out.println("재고수량은 0개 이상이어야 합니다. 상품 추가를 취소합니다.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("재고수량은 숫자만 입력 가능합니다. 상품 추가를 취소합니다.");
            return;
        }

        try {
            Product newProduct = new Product(name, price, description, stock);
            targetCategory.addProduct(newProduct);
            System.out.printf("상품 '%s'이(가) %s 카테고리에 성공적으로 추가되었습니다.%n", name, targetCategory.getCategoryName());
        } catch (IllegalArgumentException e) {
            System.out.println("상품 추가 실패: " + e.getMessage());
        }
    }

    private void handleUpdateProduct(Scanner scanner) {
        System.out.println();
        System.out.print("수정할 상품명을 입력하세요: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String targetName = scanner.nextLine().trim();
        if (targetName.isEmpty()) {
            System.out.println("상품명을 입력해 주세요.");
            return;
        }

        Product targetProduct = null;
        for (Category category : categories) {
            Product p = category.findProductByName(targetName);
            if (p != null) {
                targetProduct = p;
                break;
            }
        }

        if (targetProduct == null) {
            System.out.println("해당 이름의 상품을 찾을 수 없습니다.");
            return;
        }

        System.out.printf("현재 상품 정보: %s | %,d원 | %s | 재고: %d개%n",
                targetProduct.getName(), targetProduct.getPrice(), targetProduct.getDescription(), targetProduct.getStock());
        System.out.println("수정할 항목을 선택하세요:");
        System.out.println("1. 가격  2. 설명  3. 재고수량  0. 취소");
        System.out.print("번호를 입력하세요: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String itemChoice = scanner.nextLine().trim();

        if ("0".equals(itemChoice)) {
            System.out.println("상품 수정을 취소합니다.");
        } else if ("1".equals(itemChoice)) {
            System.out.print("새 가격을 입력하세요: ");
            if (!scanner.hasNextLine()) {
                return;
            }
            try {
                int newPrice = Integer.parseInt(scanner.nextLine().trim());
                targetProduct.setPrice(newPrice);
                System.out.printf("가격이 %,d원으로 수정되었습니다.%n", newPrice);
            } catch (NumberFormatException e) {
                System.out.println("가격은 숫자만 입력 가능합니다.");
            } catch (IllegalArgumentException e) {
                System.out.println("수정 실패: " + e.getMessage());
            }
        } else if ("2".equals(itemChoice)) {
            System.out.print("새 설명을 입력하세요: ");
            if (!scanner.hasNextLine()) {
                return;
            }
            String newDesc = scanner.nextLine().trim();
            try {
                targetProduct.setDescription(newDesc);
                System.out.println("설명이 수정되었습니다.");
            } catch (IllegalArgumentException e) {
                System.out.println("수정 실패: " + e.getMessage());
            }
        } else if ("3".equals(itemChoice)) {
            System.out.print("새 재고수량을 입력하세요: ");
            if (!scanner.hasNextLine()) {
                return;
            }
            try {
                int newStock = Integer.parseInt(scanner.nextLine().trim());
                targetProduct.setStock(newStock);
                System.out.printf("재고수량이 %d개로 수정되었습니다.%n", newStock);
            } catch (NumberFormatException e) {
                System.out.println("재고수량은 숫자만 입력 가능합니다.");
            } catch (IllegalArgumentException e) {
                System.out.println("수정 실패: " + e.getMessage());
            }
        } else {
            System.out.println("잘못된 입력입니다. 상품 수정을 취소합니다.");
        }
    }

    private void handleDeleteProduct(Scanner scanner) {
        System.out.println();
        System.out.println("[ 카테고리 선택 ]");
        for (int i = 0; i < categories.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, categories.get(i).getCategoryName());
        }
        System.out.println("0. 취소");

        System.out.print("번호를 입력하세요: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String catInput = scanner.nextLine().trim();
        int catChoice;
        try {
            catChoice = Integer.parseInt(catInput);
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다. 상품 삭제를 취소합니다.");
            return;
        }
        if (catChoice == 0) {
            System.out.println("상품 삭제가 취소되었습니다.");
            return;
        }
        if (catChoice < 1 || catChoice > categories.size()) {
            System.out.println("존재하지 않는 카테고리 번호입니다.");
            return;
        }

        Category targetCategory = categories.get(catChoice - 1);
        List<Product> products = targetCategory.getProducts();
        if (products.isEmpty()) {
            System.out.println("해당 카테고리에 등록된 상품이 없습니다.");
            return;
        }

        System.out.println();
        System.out.printf("[ %s - 삭제할 상품 선택 ]%n", targetCategory.getCategoryName());
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            System.out.printf("%d. %s | %,d원 | 재고: %d개%n", i + 1, p.getName(), p.getPrice(), p.getStock());
        }
        System.out.println("0. 취소");

        System.out.print("번호를 입력하세요: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String prodInput = scanner.nextLine().trim();
        int prodChoice;
        try {
            prodChoice = Integer.parseInt(prodInput);
        } catch (NumberFormatException e) {
            System.out.println("잘못된 입력입니다. 상품 삭제를 취소합니다.");
            return;
        }
        if (prodChoice == 0) {
            System.out.println("상품 삭제가 취소되었습니다.");
            return;
        }
        if (prodChoice < 1 || prodChoice > products.size()) {
            System.out.println("존재하지 않는 상품 번호입니다.");
            return;
        }

        Product target = products.get(prodChoice - 1);
        System.out.printf("정말로 '%s' 상품을 삭제하시겠습니까? (1. 확인 / 2. 취소)%n", target.getName());
        System.out.print("번호를 입력하세요: ");
        if (!scanner.hasNextLine()) {
            return;
        }
        String confirm = scanner.nextLine().trim();
        if ("1".equals(confirm)) {
            boolean removed = targetCategory.removeProduct(target);
            if (removed) {
                System.out.printf("'%s' 상품이 카테고리에서 삭제되었습니다.%n", target.getName());
                boolean removedFromCart = cart.removeProduct(target);
                if (removedFromCart) {
                    System.out.println("장바구니에서도 해당 상품이 함께 제거되었습니다.");
                }
            } else {
                System.out.println("상품 삭제에 실패했습니다.");
            }
        } else {
            System.out.println("상품 삭제가 취소되었습니다.");
        }
    }
}
