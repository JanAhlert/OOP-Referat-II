package com.jkfd.oopii.Controller;

import com.calendarfx.view.page.MonthPage;
import com.calendarfx.view.page.WeekPage;
import com.calendarfx.view.page.YearPage;
import javafx.event.Event;
import com.jkfd.oopii.Abstract.AbstractController;
import com.jkfd.oopii.Date;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller for the month-view
 */
public class MonthViewController extends AbstractController implements Initializable {

    @FXML
    Pane MonthViewPane;
    @FXML
    Tab MonthViewTab;
    @FXML
    Tab WeekViewTab;
    @FXML
    Tab YearViewTab;
    @FXML
    Label CurrentDateLabel;
    @FXML
    Button NextMonth;
    @FXML
    Button PreviousMonth;


    private static boolean isInitialized = false; //Variable to check if the View is initialized
    Date currentDate = new Date(); //Variable for the Date
    private static MonthPage monthPage; //Variable for the MonthView um die View zu ändern
    private static YearPage yearPage = new YearPage(); //Variable for the YearView um die View zu ändern
    private static WeekPage weekPage = new WeekPage(); //Variable for the WeekView um die View zu ändern


   private static FXMLLoader fxmlLoader = new FXMLLoader(MonthViewController.class.getResource("/com/jkfd/oopii/month-view.fxml"));    //FXMLLoader for the month-view

    /**
     * Method to load the month-view file and set the scene in the stage
     * @param stage
     * @throws IOException
     */
    public static void loadView(Stage stage) throws IOException{
        try {
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Ein Fehler ist aufgetreten");
            alert.setContentText("Es gab ein Problem beim Laden der Home-View.");
            alert.showAndWait();
        }
    }

    /**
     * Method that is loaded when the view is initialized
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Loads the MonthView
        monthPage = new MonthPage();
        monthPage.setMinHeight(795);
        monthPage.setMinWidth(1486);
        monthPage.setShowNavigation(false);
        monthPage.setShowDate(false);
        MonthViewController monthViewController = fxmlLoader.getController();
        monthViewController.MonthViewPane.getChildren().add(monthPage);
        //Sets the pop-up window
        monthPage.setEntryDetailsPopOverContentCallback(param -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jkfd/oopii/PopUpEdit.fxml"));
                return loader.load();
            } catch (IOException e) {
                return new Label("Fehler beim Laden des Inhalts"); // Throws an Error Lable for the User
            }
        });
        isInitialized = true;


        //Loads the YearView
        yearPage = new YearPage();
        yearPage.setMinHeight(795);
        yearPage.setMinWidth(1486);
        yearPage.setShowNavigation(false);
        yearPage.setShowDate(false);
        //Sets the pop-up window ToDo man kann keine eintrage eintragen, gibt es die Funktion für die Jahres ansicht nicht oder kann mann dieses noch Implementieren? Per setPopUp setzt sich kein PopUp


        //Loads the WeekView
        weekPage = new WeekPage();
        weekPage.setMinHeight(795);
        weekPage.setMinWidth(1486);
        weekPage.setShowNavigation(false);
        weekPage.setShowDate(false);
        setCurrentDateLabel(Date.MMYYYY);
        //Sets the pop-up window
        weekPage.setEntryDetailsPopOverContentCallback(param -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jkfd/oopii/PopUpEdit.fxml"));
                return loader.load();
            } catch (IOException e) {
                return new Label("Fehler beim Laden des Inhalts"); // Throws an Error Lable for the User
            }
        });

    }


//---------------------------------------------------Functions for Changing the View by selcting the Tab---------------------------------------------------//

    /**
     * Sets the current date in the month-view
     */
    @FXML
    private void onSelectionMonthViewTab() {
            if (isInitialized) {
                try {
                    MonthViewPane.getChildren().clear();
                    MonthViewPane.getChildren().add(monthPage);
                    currentDate.resetSelectedDate();
                    setCurrentDateLabel(Date.MMYYYY);
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Fehler");
                    alert.setHeaderText("Ein Fehler ist aufgetreten");
                    alert.setContentText("Es gab ein Problem beim Laden der Monatsansicht.");
                    alert.showAndWait();
                }

            }
    }

    /**
     * Sets the current date in the year-view
     */
    @FXML
    private void onSelectionYearViewTab(){
        try {
            MonthViewPane.getChildren().clear();
            MonthViewPane.getChildren().add(yearPage);
            currentDate.resetSelectedDate();
            setCurrentDateLabel(Date.YYYY);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Ein Fehler ist aufgetreten");
            alert.setContentText("Es gab ein Problem beim Laden der Jahresansicht.");
            alert.showAndWait();
        }
    }

    /**
     * Sets the current date in the week-view
     */
    @FXML
    private void onSelectionWeekViewTab(){
        try {
            MonthViewPane.getChildren().clear();
            MonthViewPane.getChildren().add(weekPage);
            currentDate.resetSelectedDate();
            setCurrentDateLabel(Date.DDMMYYYY);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Ein Fehler ist aufgetreten");
            alert.setContentText("Es gab ein Problem beim Laden der Wochenansicht.");
            alert.showAndWait();
        }
    }

    /**
     * Setter Methode for the current date in the home-views
     */
    @FXML
    private void setCurrentDateLabel(DateTimeFormatter Format)
    {
        CurrentDateLabel.setText(currentDate.getCurrentselectedDate(Format));
    }


    //---------------------------------------------------Functions for Changing the Date over the Buttons---------------------------------------------------//

    /**
     *Mehtode for the NextMonth Button //TODO: Rename the Function
     */
    @FXML
    private void setNextMonth()
    {
        if (MonthViewTab.isSelected()) {
            changeMonth(1);
        } else if (WeekViewTab.isSelected()) {
            changeWeek(1);
        } else if (YearViewTab.isSelected()) {
            changeYear(1);
        }
    }

    /**
     *Mehtode for the PreviousMonth Button //TODO: Rename the Function
     */
    @FXML
    private void setPreviousMonth(){
        if (MonthViewTab.isSelected()) {
            changeMonth(-1);
        } else if (WeekViewTab.isSelected()) {
            changeWeek(-1);
        } else if (YearViewTab.isSelected()) {
            changeYear(-1);
        }
    }

    //---------------------------------------------------Functions for Changing the Date---------------------------------------------------//

    /**
     * Changes the month view by the given value
     * @param month
     */
    private void changeMonth(int month) {
        currentDate.setPlusMonth(month);
        CurrentDateLabel.setText(currentDate.getCurrentselectedDate(Date.MMYYYY));
        monthPage.setDate(currentDate.getSelectedDate());
    }

    /**
     * Changes the week view by the given value
     * @param week
     */
    private void changeWeek(int week) {
        currentDate.setPlusWeek(week);
        CurrentDateLabel.setText(currentDate.getCurrentselectedDate(Date.DDMMYYYY));
        weekPage.setDate(currentDate.getSelectedDate());
    }

    /**
     * Changes the year view by the given value
     * @param year
     */
    private void changeYear(int year) {
        currentDate.setPlusYear(year);
        CurrentDateLabel.setText(currentDate.getCurrentselectedDate(Date.YYYY));
        yearPage.setDate(currentDate.getSelectedDate());
    }

    //---------------------------------------------------Functions to get Data from the User---------------------------------------------------//



}


