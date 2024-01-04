package com.jkfd.oopii;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Class for the Date.
 * It handels the Date and the Date formate.
 */
public class Date {


    /**
     *   The pattern for the Date formate
     */
    public static final DateTimeFormatter DDMMYYYY = DateTimeFormatter.ofPattern("dd.MM.uuuu");
    public static final DateTimeFormatter MMYYYY = DateTimeFormatter.ofPattern("MM.uuuu");
    public static final DateTimeFormatter YYYY = DateTimeFormatter.ofPattern("uuuu");

    //LocaleVariables
    LocalDate selectedDate = LocalDate.now();
    LocalDate currentDate = LocalDate.now();

    /**
     * Gives the current Date
     * @return Current Date as LocalDate
     */
    public LocalDate getCurrentDate() {
        return currentDate;
    }

    /**
     * Gives the selected Date
     * @return Selected Date as LocalDate
     */
    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    /**
     * Resets the selected Date to the current Date
     */
    public void resetSelectedDate() {
        selectedDate = currentDate;
    }

    /**
     * Gives the current selected Date
     * @param Format
     * @return New Date
     */
    public String getCurrentselectedDate(DateTimeFormatter Format) {
        return selectedDate.format(Format);
    }

    /**
     * Adds a year to the selected Date.
     * You can use negative numbers to subtract a year
     * @param Year
     * @return The new Date
     */
    public LocalDate setPlusYear(int Year) {
        selectedDate = selectedDate.plusYears(Year);
        return selectedDate;
    }

    /**
     * Adds a month to the selected Date.
     * You can use negative numbers to subtract a month
     * @param Month
     * @return The new Date
     */
    public LocalDate setPlusMonth(int Month)
    {
        selectedDate = selectedDate.plusMonths(Month);
        return selectedDate;
    }

    /**
     * Adds a week to the selected Date.
     * You can use negative numbers to subtract a week
     * @param Week
     * @return The new Date
     */
    public LocalDate setPlusWeek(int Week)
    {
        selectedDate = selectedDate.plusWeeks(Week);
        return selectedDate;
    }


}
