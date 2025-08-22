package com.example.hotel_managment.utils;

import java.util.concurrent.ThreadLocalRandom;

public class PaymentUtils {

    /**
     * Возвращает случайный способ оплаты
     * @return Строку: "Карта", "Наличные" или "СБП"
     */
    public static String getRandomPaymentMethod() {
        // Создаём массив с вариантами оплаты
        String[] methods = {"Карта", "Наличные", "СБП"};

        // Генерируем случайный индекс (0, 1 или 2)
        int randomIndex = ThreadLocalRandom.current().nextInt(methods.length);

        // Возвращаем случайный метод оплаты
        return methods[randomIndex];
    }
}