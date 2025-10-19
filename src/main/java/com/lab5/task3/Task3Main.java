package com.lab5.task3;

import com.lab5.task2.Order;
import com.lab5.task2.ShoeWarehouse;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Главный класс для демонстрации работы с ExecutorService
 */
public class Task3Main {

    private static final int WAREHOUSE_CAPACITY = 15;
    private static final int TOTAL_ORDERS = 25;
    private static final int PRODUCER_THREADS = 2;
    private static final int CONSUMER_THREADS = 4;

    public static void main(String[] args) {
        System.out.println("=== Задание 3: ExecutorService ===");

        // Создаем склад
        ShoeWarehouse warehouse = new ShoeWarehouse(WAREHOUSE_CAPACITY);

        // Создаем пулы потоков
        ExecutorService producerExecutor = Executors.newFixedThreadPool(PRODUCER_THREADS);
        ExecutorService consumerExecutor = Executors.newFixedThreadPool(CONSUMER_THREADS);

        System.out.println("Используем FixedThreadPool:");
        System.out.println("- Producer threads: " + PRODUCER_THREADS);
        System.out.println("- Consumer threads: " + CONSUMER_THREADS);

        // Запускаем Producer задачи
        for (int i = 0; i < PRODUCER_THREADS; i++) {
            final int producerId = i + 1;
            producerExecutor.submit(() -> {
                Random random = new Random();
                int ordersPerProducer = TOTAL_ORDERS / PRODUCER_THREADS;

                for (int j = 1; j <= ordersPerProducer; j++) {
                    String shoeType = ShoeWarehouse.AVAILABLE_SHOE_TYPES.get(
                            random.nextInt(ShoeWarehouse.AVAILABLE_SHOE_TYPES.size())
                    );
                    int quantity = random.nextInt(10) + 1;
                    int orderId = producerId * 100 + j; // Уникальный ID

                    Order order = new Order(orderId, shoeType, quantity);
                    warehouse.receiveOrder(order);

                    try {
                        Thread.sleep(random.nextInt(200) + 100);
                    } catch (InterruptedException e) {
                        System.err.println("Producer " + producerId + " прерван");
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                System.out.println("Producer " + producerId + " завершил работу");
            });
        }

        // Запускаем Consumer задачи
        for (int i = 0; i < CONSUMER_THREADS; i++) {
            final int consumerId = i + 1;
            consumerExecutor.submit(() -> {
                Random random = new Random();
                int processed = 0;

                while (processed < 10) { // Каждый Consumer обрабатывает до 10 заказов
                    Order order = warehouse.fulfillOrder();
                    if (order != null) {
                        processed++;
                        System.out.println("Consumer " + consumerId +
                                " обработал: " + order.orderId() +
                                " (" + processed + "/10)");
                    }

                    try {
                        Thread.sleep(random.nextInt(300) + 200);
                    } catch (InterruptedException e) {
                        System.err.println("Consumer " + consumerId + " прерван");
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

                System.out.println("Consumer " + consumerId + " завершил работу");
            });
        }

        // Плавное завершение работы ExecutorService
        producerExecutor.shutdown();
        consumerExecutor.shutdown();

        try {
            // Ждем завершения всех задач (максимум 2 минуты)
            if (!producerExecutor.awaitTermination(2, TimeUnit.MINUTES)) {
                System.err.println("ProducerExecutor не завершился вовремя");
                producerExecutor.shutdownNow();
            }

            if (!consumerExecutor.awaitTermination(2, TimeUnit.MINUTES)) {
                System.err.println("ConsumerExecutor не завершился вовремя");
                consumerExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.err.println("Ожидание завершения прервано");
            producerExecutor.shutdownNow();
            consumerExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("Все ExecutorService завершили работу");
        System.out.println("Осталось заказов на складе: " + warehouse.getOrderCount());

        // Демонстрация других типов пулов
        demonstrateOtherPools();
    }

    /**
     * Демонстрация других типов ExecutorService
     */
    private static void demonstrateOtherPools() {
        System.out.println("\n--- Демонстрация других типов пулов ---");

        // SingleThreadExecutor
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        System.out.println("SingleThreadExecutor: один поток выполняет задачи последовательно");

        // CachedThreadPool
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        System.out.println("CachedThreadPool: создает потоки по мере необходимости");

        // FixedThreadPool (уже использовали выше)
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);
        System.out.println("FixedThreadPool: фиксированное количество потоков");

        // Завершаем демонстрационные пулы
        singleThreadExecutor.shutdown();
        cachedThreadPool.shutdown();
        fixedThreadPool.shutdown();
    }
}