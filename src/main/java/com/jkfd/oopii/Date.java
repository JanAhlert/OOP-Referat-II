package com.jkfd.oopii;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Takes care of date management and formatting
 * @Author: Fabian Klein
 * @Version: 0.1
 */
public class Date {

    /**
     *   The pattern for the Date formate
     */
    DateTimeFormatter DDMMYYYY = DateTimeFormatter.ofPattern("dd.MM.uuuu");
    LocalDate localDate = LocalDate.now();

    /**
     *
     * @return The Current Date as String
     */
    public String getCurrentDate()
    {
        return localDate.format(DDMMYYYY);
    }
}
