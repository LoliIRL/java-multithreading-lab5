package com.lab5.task2;

/**
 * Заказ на обувь
 * Используем record для неизменяемой структуры данных
 */
public record Order(int orderId, String shoeType, int quantity) {

    @Override
    public String toString() {
        return String.format("Order{id=%d, type='%s', quantity=%d}",
                orderId, shoeType, quantity);
    }
}