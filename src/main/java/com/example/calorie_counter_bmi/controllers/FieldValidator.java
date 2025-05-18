package com.example.calorie_counter_bmi.controllers;

import java.util.regex.Pattern;

public class FieldValidator {

    // Проверка ФИО (2 или 3 слова с заглавной буквы, разделенные пробелами)
    public static boolean isValidFio(String fio) {
        // Паттерн: 2-3 слова с заглавной буквы, разделенные пробелами
        String regex = "^[А-ЯЁ][а-яё]+(?:\\s[А-ЯЁ][а-яё]+){1,2}$";
        return Pattern.matches(regex, fio);
    }

    // Метод для проверки телефонного номера (формат: +7XXXXXXXXXX)
    public static boolean isValidPhone(String phone) {
        // Паттерн: +7, затем 10 цифр
        String regex = "^\\+7\\d{10}$";
        return Pattern.matches(regex, phone);
    }

    // Метод для проверки email
    public static boolean isValidEmail(String email) {
        // Стандартный паттерн для email
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, email);
    }

    // Метод для проверки паспортных данных (формат: 4 цифры, пробел, 6 цифр)
    public static boolean isValidPassport(String passport) {
        // Паттерн: 4 цифры, пробел, 6 цифр
        String regex = "^\\d{4}\\s\\d{6}$";
        return Pattern.matches(regex, passport);
    }
}
