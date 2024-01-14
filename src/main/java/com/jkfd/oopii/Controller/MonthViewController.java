package com.jkfd.oopii.Controller;

import com.calendarfx.model.Entry;
import com.calendarfx.view.page.MonthPage;
import com.calendarfx.view.page.WeekPage;
import com.calendarfx.view.page.YearPage;
import com.jkfd.oopii.Database.Models.Event;
import com.jkfd.oopii.Database.Models.Todo;
import com.jkfd.oopii.Date;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import static com.jkfd.oopii.HelloApplication.databaseManager;

/**
 * Controller for the month-view
 */
public class MonthViewController implements Initializable {
    static final Logger logger = LoggerFactory.getLogger(MonthViewController.class);

    public static Event observedEvent = null;
    /**
     * 0 - New
     * 1 - Edit
     */
    public static int observedEventAction = 0;

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
    @FXML
    VBox EventsVBox;
    @FXML
    VBox TodosVBox;


    private static boolean isInitialized = false; //Variable to check if the View is initialized
    Date currentDate = new Date(); //Variable for the Date
    private static MonthPage monthPage; //Variable for the MonthView um die View zu ändern
    private static YearPage yearPage = new YearPage(); //Variable for the YearView um die View zu ändern
    private static WeekPage weekPage = new WeekPage(); //Variable for the WeekView um die View zu ändern


   private static FXMLLoader fxmlLoader = new FXMLLoader(MonthViewController.class.getResource("/com/jkfd/oopii/month-view.fxml"));    //FXMLLoader for the month-view

    /**
     * Method to load the month-view file and set the scene in the stage
     * @param stage
     */
    public static void loadView(Stage stage) {
        try {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Home");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            logger.atError().setMessage("There was an error while loading the monthview: {}").addArgument(e.getMessage()).log();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Ein Fehler ist aufgetreten");
            alert.setContentText("Es gab ein Problem beim Laden der Monatsansicht:\n" + e.getLocalizedMessage() + "\n" + Arrays.toString(e.getStackTrace()));
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

                String id = param.getEntry().getId();
                try {
                    observedEvent = databaseManager.GetEvent(Integer.parseInt(id));
                    observedEventAction = 1;
                } catch (NumberFormatException e) {
                    logger.atInfo().setMessage("Event ID invalid ({}); might have been a newly created event.").addArgument(id).log();
                    observedEventAction = 0;
                }

                return loader.load();
            } catch (IOException e) {
                logger.atError().setMessage("Error while loading PopUpEdit content: {}").addArgument(e.getMessage()).log();
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
                logger.atError().setMessage("Error while loading PopUpEdit content: {}").addArgument(e.getMessage()).log();
                return new Label("Fehler beim Laden des Inhalts"); // Throws an Error Lable for the User
            }
        });

        // Load the right sidebar
        monthViewController.EventsVBox.getChildren().clear();
        monthViewController.TodosVBox.getChildren().clear();

        ArrayList<Event> events = databaseManager.GetEvents(3);
        //ArrayList<Todo> todos = databaseManager.GetUnfinishedTodos(3); FIXME => TODO Dates cannot be null. Boolean might be better

        for (Event tmp : events) {
            Label tmpLabel = new Label();
            tmpLabel.setText(tmp.title);

            monthViewController.EventsVBox.getChildren().add(tmpLabel);
        }

        /* FIXME: See above comment
        for (Todo tmp : todos) {
            CheckBox tmpCheckbox = new CheckBox();
            tmpCheckbox.setText(tmp.title);
            tmpCheckbox.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    tmp.SetCompletedDate(LocalDateTime.now());
                    databaseManager.UpdateTodo(tmp);
                    tmpCheckbox.setSelected(true);
                    tmpCheckbox.setDisable(true);
                }
            });

            monthViewController.TodosVBox.getChildren().add(tmpCheckbox);
        }
        */

        // Load the Calendar itself
        events = databaseManager.GetEvents();
        for (Event tmp : events) {
            ZonedDateTime tmpTime = tmp.GetStartDate().atZone(ZoneId.systemDefault());

            // Create entries
            Entry<?> tmpEntryMonth = monthPage.createEntryAt(tmpTime);
            Entry<?> tmpEntryWeek = weekPage.createEntryAt(tmpTime);
            Entry<?> tmpEntryYear = yearPage.createEntryAt(tmpTime);

            // Set ID
            tmpEntryMonth.setId(Integer.toString(tmp.GetID()));
            tmpEntryWeek.setId(Integer.toString(tmp.GetID()));
            tmpEntryYear.setId(Integer.toString(tmp.GetID()));

            // Set title
            tmpEntryMonth.setTitle(tmp.title);
            tmpEntryWeek.setTitle(tmp.title);
            tmpEntryYear.setTitle(tmp.title);

            // Set date
            tmpEntryMonth.changeStartDate(tmp.GetStartDate().toLocalDate());
            tmpEntryMonth.changeEndDate(tmp.GetEndDate().toLocalDate());
            tmpEntryWeek.changeStartDate(tmp.GetStartDate().toLocalDate());
            tmpEntryWeek.changeEndDate(tmp.GetEndDate().toLocalDate());
            tmpEntryYear.changeStartDate(tmp.GetStartDate().toLocalDate());
            tmpEntryYear.changeEndDate(tmp.GetEndDate().toLocalDate());

            // Set time
            tmpEntryMonth.changeStartTime(tmp.GetStartDate().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toLocalTime());
            tmpEntryMonth.changeEndTime(tmp.GetEndDate().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toLocalTime());
            tmpEntryWeek.changeStartTime(tmp.GetStartDate().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toLocalTime());
            tmpEntryWeek.changeEndTime(tmp.GetEndDate().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toLocalTime());
            tmpEntryYear.changeStartTime(tmp.GetStartDate().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toLocalTime());
            tmpEntryYear.changeEndTime(tmp.GetEndDate().toLocalDate().atStartOfDay(ZoneId.systemDefault()).toLocalTime());

            // Set full day
            tmpEntryMonth.setFullDay(tmp.fullDay);
            tmpEntryWeek.setFullDay(tmp.fullDay);
            tmpEntryYear.setFullDay(tmp.fullDay);
       }
    }

    @FXML
    private void onCreateTodoButtonPressed() {

    }

    @FXML
    private void onCreateEventButtonPressed() {

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
                    logger.atError().setMessage("onSelectionMonthViewTab error: {}").addArgument(e.getMessage()).log();

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
            logger.atError().setMessage("onSelectionYearViewTab error: {}").addArgument(e.getMessage()).log();

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
            logger.atError().setMessage("onSelectionWeekViewTab error: {}").addArgument(e.getMessage()).log();

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