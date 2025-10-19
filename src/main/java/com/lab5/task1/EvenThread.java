package com.lab5.task1;

/**
 * Поток для вывода четных чисел
 * Наследуется от Thread
 */
public class EvenThread extends Thread {

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " начал работу");

        for (int i = 2; i <= 10; i += 2) {
            System.out.println("Четное число: " + i);
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