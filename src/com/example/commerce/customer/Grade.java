package com.example.commerce.customer;

public enum Grade {
    BRONZE(0),
    SILVER(5),
    GOLD(10),
    PLATINUM(15);

    private final int discountRate;

    Grade(int discountRate) {
        this.discountRate = discountRate;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public int calculateDiscountAmount(int totalPrice) {
        return totalPrice * discountRate / 100;
    }

    public int calculateFinalPrice(int totalPrice) {
        return totalPrice - calculateDiscountAmount(totalPrice);
    }

    public static Grade fromOption(int option) {
        switch (option) {
            case 1:
                return BRONZE;
            case 2:
                return SILVER;
            case 3:
                return GOLD;
            case 4:
                return PLATINUM;
            default:
                throw new IllegalArgumentException("올바른 등급 번호(1~4)를 입력해 주세요.");
        }
    }
}
