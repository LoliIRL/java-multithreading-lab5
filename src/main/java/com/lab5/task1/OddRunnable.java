package com.lab5.task1;

/**
 * Класс для вывода нечетных чисел
 * Реализует интерфейс Runnable
 */
public class OddRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " начал работу");

        for (int i = 1; i <= 9; i += 2) {
            System.out.println("Нечетное число: " + i);
            try {
                // Имитация работы
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.err.println("Поток прерван: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

        System.out.println(Thread.currentThread().getName() + " завершил работу");
    }
}