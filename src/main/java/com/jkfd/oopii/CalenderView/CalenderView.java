package com.jkfd.oopii.CalenderView;

import java.time.LocalDate;
import java.time.LocalTime;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CalenderView {

    CalendarView calendarView = new CalendarView(); // (1)

    Calendar birthdays = new Calendar("Birthdays"); // (2)
    Calendar holidays = new Calendar("Holidays");




    Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
        @Override
        public void run() {
            while (true) {
                Platform.runLater(() -> {
                    calendarView.setToday(LocalDate.now());
                    calendarView.setTime(LocalTime.now());
                });

                try {
                    // update every 10 seconds
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    };


}
