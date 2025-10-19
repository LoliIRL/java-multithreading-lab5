package com.lab5.task2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Склад обуви - реализация паттерна Producer-Consumer
 */
public class ShoeWarehouse {

    // Публичное статическое поле со списком типов товаров
    public static final List<String> AVAILABLE_SHOE_TYPES = List.of(
            "Nike Air Max", "Adidas Ultraboost", "Puma RS-X",
            "Reebok Classic", "Converse Chuck Taylor", "Vans Old Skool"
    );

    // Приватная очередь заказов
    private final Queue<Order> orders = new LinkedList<>();
    private final int maxCapacity;

    // Конструктор с максимальной вместимостью склада
    public ShoeWarehouse(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * Метод для добавления заказов на склад (Producer)
     * @param order заказ для добавления
     */
    public synchronized void receiveOrder(Order order) {
        // Ждем, если склад заполнен
        while (orders.size() >= maxCapacity) {
            try {
                System.out.println("Склад заполнен. Producer ждет...");
                wait();
            } catch (InterruptedException e) {
                System.err.println("Producer прерван: " + e.getMessage());
                Thread.currentThread().interrupt();
                return;
            }
        }

        // Добавляем заказ
        orders.offer(order);
        System.out.println("Добавлен заказ: " + order +
                " | Всего заказов: " + orders.size());

        // Уведомляем всех ожидающих потребителей
        notifyAll();
    }

    /**
     * Метод для обработки заказов (Consumer)
     * @return обработанный заказ
     */
    public synchronized Order fulfillOrder() {
        // Ждем, если склад пуст
        while (orders.isEmpty()) {
            try {
                System.out.println("Склад пуст. Consumer ждет...");
                wait();
            } catch (InterruptedException e) {
                System.err.println("Consumer прерван: " + e.getMessage());
                Thread.currentThread().interrupt();
                return null;
            }
        }

        // Извлекаем заказ по принципу FIFO
        Order order = orders.poll();
        System.out.println("Обработан заказ: " + order +
                " | Осталось заказов: " + orders.size());

        // Уведомляем всех ожидающих производителей
        notifyAll();

        return order;
    }

    /**
     * Получить текущее количество заказов на складе
     */
    public synchronized int getOrderCount() {
        return orders.size();
    }
}