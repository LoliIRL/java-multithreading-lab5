package com.lab5.task2;

import java.util.Random;

/**
 * Главный класс для демонстрации работы Producer-Consumer
 */
public class Task2Main {

    private static final int WAREHOUSE_CAPACITY = 10;
    private static final int TOTAL_ORDERS = 20;
    private static final int CONSUMER_COUNT = 3;
    private static final int ORDERS_PER_CONSUMER = 5;

    public static void main(String[] args) {
        System.out.println("=== Задание 2: Producer-Consumer (Склад обуви) ===");

        // Создаем склад
        ShoeWarehouse warehouse = new ShoeWarehouse(WAREHOUSE_CAPACITY);

        // Поток Producer
        Thread producerThread = new Thread(() -> {
            Random random = new Random();

            for (int i = 1; i <= TOTAL_ORDERS; i++) {
                // Генерируем случайный заказ
                String shoeType = ShoeWarehouse.AVAILABLE_SHOE_TYPES.get(
                        random.nextInt(ShoeWarehouse.AVAILABLE_SHOE_TYPES.size())
                );
                int quantity = random.nextInt(10) + 1; // 1-10 пар

                Order order = new Order(i, shoeType, quantity);
                warehouse.receiveOrder(order);

                try {
                    // Имитация времени на создание заказа
                    Thread.sleep(random.nextInt(300) + 200);
                } catch (InterruptedException e) {
                    System.err.println("Producer прерван: " + e.getMessage());
                    Thread.currentThread().interrupt();
                    break;
                }
            }

            System.out.println("Producer завершил генерацию всех заказов");
        }, "Producer");

        // Потоки Consumer
        Thread[] consumerThreads = new Thread[CONSUMER_COUNT];

        for (int i = 0; i < CONSUMER_COUNT; i++) {
            final int consumerId = i + 1;
            consumerThreads[i] = new Thread(() -> {
                int processedOrders = 0;

                while (processedOrders < ORDERS_PER_CONSUMER) {
                    Order order = warehouse.fulfillOrder();
                    if (order != null) {
                        processedOrders++;
                        System.out.println("Consumer " + consumerId +
                                " обработал заказ: " + order.orderId() +
                                " (" + processedOrders + "/" + ORDERS_PER_CONSUMER + ")");
                    }

                    try {
                        // Имитация времени на обработку заказа
                        Thread.sleep(new Random().nextInt(400) + 300);
                    } catch (InterruptedException e) {
                        System.err.println("Consumer " + consumerId + " прерван: " + e.getMessage());
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                System.out.println("Consumer " + consumerId + " завершил работу");
            }, "Consumer-" + (i + 1));
        }

        // Запускаем все потоки
        System.out.println("Запускаем Producer и " + CONSUMER_COUNT + " Consumer...");
        producerThread.start();

        for (Thread consumer : consumerThreads) {
            consumer.start();
        }

        // Ждем завершения всех потоков
        try {
            producerThread.join();
            for (Thread consumer : consumerThreads) {
                consumer.join();
            }
        } catch (InterruptedException e) {
            System.err.println("Главный поток прерван: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.println("Все заказы обработаны. Работа склада завершена");
        System.out.println("Осталось заказов на складе: " + warehouse.getOrderCount());
    }
}