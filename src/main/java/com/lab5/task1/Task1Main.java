package com.lab5.task1;

/**
 * Главный класс для демонстрации работы потоков из Задания 1
 */
public class Task1Main {

    public static void main(String[] args) {
        System.out.println("=== Задание 1: Создание потоков ===");

        // Создаем поток через наследование от Thread
        Thread evenThread = new EvenThread();
        evenThread.setName("EvenThread");

        // Создаем поток через реализацию Runnable
        Thread oddThread = new Thread(new OddRunnable(), "OddThread");

        System.out.println("Запускаем потоки...");

        // Запускаем потоки
        evenThread.start();
        oddThread.start();

        try {
            // Ждем завершения потоков
            evenThread.join();
            oddThread.join();
        } catch (InterruptedException e) {
            System.err.println("Главный поток прерван: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        System.out.println("Все потоки завершили работу");
    }
}