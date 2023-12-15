package com.jkfd.oopii.CalenderView;

import java.time.LocalDate;
import java.time.LocalTime;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;

import com.calendarfx.view.MonthView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CalenderView {

    CalendarView cView = new CalendarView(); // (1)





    Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
        @Override
        public void run() {
            while (true) {
                Platform.runLater(() -> {
                    cView.setToday(LocalDate.now());
                    cView.setTime(LocalTime.now());
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
