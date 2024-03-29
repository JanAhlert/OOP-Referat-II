package com.jkfd.oopii.Controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.Entry;
import com.calendarfx.view.page.MonthPage;
import com.calendarfx.view.page.WeekPage;
import com.calendarfx.view.page.YearPage;
import com.jkfd.oopii.Database.Models.Element;
import com.jkfd.oopii.Database.Models.Event;
import com.jkfd.oopii.Database.Models.Todo;
import com.jkfd.oopii.Date;
import com.jkfd.oopii.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.jkfd.oopii.HelloApplication.databaseManager;

/**
 * Controller for the month-view.
 * This is the main view that the user will see.
 */
public class MonthViewController implements Initializable {
    /**
     * This variable keeps track for the calendar event handler of entries being currently refreshed.
     * Otherwise, it will think that all entries should be deleted.
     */
    private static boolean updatingEntries = false;

    /**
     * Logger instance for the month view controller.
     */
    private static final Logger logger = LoggerFactory.getLogger(MonthViewController.class);

    /**
     * The observed event is passed to the popup controllers.
     */
    public static Event observedEvent = null;

    /**
     * The observed todo is passed to the popup controllers.
     */
    public static Todo observedTodo = null;

    /**
     * Action to do with the observed event.
     * 0 - New
     * 1 - Edit
     */
    public static int observedEventAction = 0;

    /**
     * Action to do with the observed todo.
     * 0 - New
     * 1 - Edit
     */
    public static int observedTodoAction = 0;

    /**
     * FXML definitions
     */
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
    @FXML
    Button createTodoButton;
    @FXML
    Button createEventButton;


    private static boolean isInitialized = false; //Variable to check if the View is initialized
    Date currentDate = new Date(); //Variable for the Date
    private static MonthPage monthPage; //Variable for the MonthView um die View zu ändern
    private static MonthViewController monthViewController;
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
        monthViewController = fxmlLoader.getController();
        monthViewController.MonthViewPane.getChildren().add(monthPage);
        //Sets the pop-up window
        monthPage.setEntryDetailsPopOverContentCallback(param -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jkfd/oopii/EventPopUpEdit.fxml"));

                String id = param.getEntry().getId();
                try {
                    observedEvent = databaseManager.GetEvent(Integer.parseInt(id));
                    observedEventAction = 1;
                } catch (NumberFormatException e) {
                    logger.atInfo().setMessage("Event ID invalid ({}); might have been a newly created event.").addArgument(id).log();
                    Event tmp = new Event();
                    tmp.title = param.getEntry().getTitle();
                    tmp.priority = Element.Priority.NORMAL;
                    tmp.SetDateRange(param.getEntry().getStartAsLocalDateTime(), param.getEntry().getEndAsLocalDateTime());
                    observedEvent = tmp;
                    observedEventAction = 0;
                }

                return loader.load();
            } catch (IOException e) {
                logger.atError().setMessage("Error while loading PopUpEdit content: {}").addArgument(e.getMessage()).log();
                return new Label("Fehler beim Laden des Inhalts"); // Throws an Error Lable for the User
            }
        });
        monthPage.getCalendarSources().getFirst().getCalendars().getFirst().addEventHandler(param -> {
            if (updatingEntries) {
                return;
            }

            if (param.getEventType() == CalendarEvent.ENTRY_CALENDAR_CHANGED) {
                CalendarEvent tmp = (CalendarEvent) param;
                if (tmp.getCalendar() == null && Objects.equals(tmp.getOldCalendar().getName(), "Default")) {
                    Entry<?> tmpEntry = tmp.getEntry();

                    try {
                        databaseManager.DeleteEvent(Integer.parseInt(tmpEntry.getId()));
                        logger.atInfo().setMessage("event '{}' ({}) deleted").addArgument(tmpEntry.getTitle()).addArgument(tmpEntry.getId()).log();
                    } catch (NumberFormatException e) {
                        logger.atWarn().setMessage("error while deleting event {} ({}), might have been newly created (not persisted)")
                                .addArgument(tmpEntry.getTitle())
                                .addArgument(tmpEntry.getId())
                                .log();
                    }
                }
            }
        });
        isInitialized = true;

        //Loads the YearView
        yearPage = new YearPage();
        yearPage.setMinHeight(795);
        yearPage.setMinWidth(1486);
        yearPage.setShowNavigation(false);
        yearPage.setShowDate(false);



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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jkfd/oopii/EventPopUpEdit.fxml"));
                return loader.load();
            } catch (IOException e) {
                logger.atError().setMessage("Error while loading PopUpEdit content: {}").addArgument(e.getMessage()).log();
                return new Label("Fehler beim Laden des Inhalts"); // Throws an Error Lable for the User
            }
        });

        UpdateEntries();

        createTodoButton.setOnAction(actionEvent -> {
            try {
                observedTodo = null;
                observedTodoAction = 0;

                Parent root = FXMLLoader.load(HelloApplication.class.getResource("/com/jkfd/oopii/TodoPopUpEdit.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                logger.atError().setMessage("Error while trying to open TodoPopUpEdit.fxml (IOException): {}\n{}").addArgument(e.getMessage()).addArgument(e.getStackTrace()).log();
            } catch (Exception e) {
                logger.atError().setMessage("Error while trying to open TodoPopUpEdit.fxml (Exception): {}\n{}").addArgument(e.getMessage()).addArgument(e.getStackTrace()).log();
            }
        });

        createEventButton.setOnAction(actionEvent -> {
            try {
                observedEvent = null;
                observedEventAction = 0;

                Parent root = FXMLLoader.load(HelloApplication.class.getResource("/com/jkfd/oopii/EventPopUpEdit.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                logger.atError().setMessage("Error while trying to open EventPopUpEdit.fxml: {}").addArgument(e.getMessage()).log();
            }
        });
    }

    /**
     * Updates the entries in the calendar and sidebar.
     */
    public static void UpdateEntries() {
        updatingEntries = true;

        Calendar defaultCalendar = monthPage.getMonthView().getCalendarSources().get(0).getCalendars().get(0);
        List entries = defaultCalendar.findEntries("");
        defaultCalendar.removeEntries(entries);

        // Load the right sidebar
        monthViewController.EventsVBox.getChildren().clear();
        monthViewController.TodosVBox.getChildren().clear();

        ArrayList<Event> events = databaseManager.GetEvents(10);
        ArrayList<Todo> todos = databaseManager.GetUnfinishedTodos(10);

        for (Event tmp : events) {
            HBox tmpHBox = new HBox();

            Label tmpLabel = new Label();
            tmpLabel.setText(String.format("%s (%s)", tmp.title, tmp.GetStartDate().toLocalDate()));
            tmpHBox.getChildren().add(tmpLabel);

            Label tmpEditLabel = new Label();
            tmpEditLabel.setText("[Edit]");
            tmpEditLabel.setPadding(new Insets(0, 0, 0, 5));
            tmpEditLabel.setTextFill(Color.color(0, 0.3, 0.7));
            tmpEditLabel.setOnMousePressed(mouseEvent -> {
                try {
                    observedEvent = tmp;
                    observedEventAction = 1;

                    Parent root = FXMLLoader.load(HelloApplication.class.getResource("/com/jkfd/oopii/EventPopUpEdit.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    logger.atError().setMessage("Error while trying to open EventPopUpEdit.fxml (IOException): {}\n{}").addArgument(e.getMessage()).addArgument(e.getStackTrace()).log();
                } catch (Exception e) {
                    logger.atError().setMessage("Error while trying to open EventPopUpEdit.fxml (Exception): {}\n{}").addArgument(e.getMessage()).addArgument(e.getStackTrace()).log();
                }
            });
            tmpHBox.getChildren().add(tmpEditLabel);

            Label tmpDeleteLabel = new Label();
            tmpDeleteLabel.setText("[Del]");
            tmpDeleteLabel.setPadding(new Insets(0, 0, 0, 5));
            tmpDeleteLabel.setTextFill(Color.color(0.7, 0.2, 0));
            tmpDeleteLabel.setOnMousePressed(mouseEvent -> {
                databaseManager.DeleteEvent(tmp.GetID());
                UpdateEntries();
            });
            tmpHBox.getChildren().add(tmpDeleteLabel);

            monthViewController.EventsVBox.getChildren().add(tmpHBox);
        }

        for (Todo tmp : todos) {
            HBox tmpHBox = new HBox();

            CheckBox tmpCheckbox = new CheckBox();
            tmpCheckbox.setText(tmp.title);
            tmpCheckbox.setOnAction(actionEvent -> {
                tmp.SetCompletedDate(LocalDateTime.now());
                databaseManager.UpdateTodo(tmp);
                tmpCheckbox.setSelected(true);
                tmpCheckbox.setDisable(true);
            });
            tmpHBox.getChildren().add(tmpCheckbox);

            Label tmpEditLabel = new Label();
            tmpEditLabel.setText("[Edit]");
            tmpEditLabel.setPadding(new Insets(0, 0, 0, 5));
            tmpEditLabel.setTextFill(Color.color(0, 0.3, 0.7));
            tmpEditLabel.setOnMousePressed(mouseEvent -> {
                try {
                    observedTodo = tmp;
                    observedTodoAction = 1;

                    Parent root = FXMLLoader.load(HelloApplication.class.getResource("/com/jkfd/oopii/TodoPopUpEdit.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    logger.atError().setMessage("Error while trying to open TodoPopUpEdit.fxml (IOException): {}\n{}").addArgument(e.getMessage()).addArgument(e.getStackTrace()).log();
                } catch (Exception e) {
                    logger.atError().setMessage("Error while trying to open TodoPopUpEdit.fxml (Exception): {}\n{}").addArgument(e.getMessage()).addArgument(e.getStackTrace()).log();
                }
            });
            tmpHBox.getChildren().add(tmpEditLabel);

            Label tmpDeleteLabel = new Label();
            tmpDeleteLabel.setText("[Del]");
            tmpDeleteLabel.setPadding(new Insets(0, 0, 0, 5));
            tmpDeleteLabel.setTextFill(Color.color(0.7, 0.2, 0));
            tmpDeleteLabel.setOnMousePressed(mouseEvent -> {
                databaseManager.DeleteTodo(tmp.GetID());
                UpdateEntries();
            });
            tmpHBox.getChildren().add(tmpDeleteLabel);

            monthViewController.TodosVBox.getChildren().add(tmpHBox);
        }

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
            tmpEntryMonth.changeStartTime(tmp.GetStartDate().toLocalTime());
            tmpEntryMonth.changeEndTime(tmp.GetEndDate().toLocalTime());
            tmpEntryWeek.changeStartTime(tmp.GetStartDate().toLocalTime());
            tmpEntryWeek.changeEndTime(tmp.GetEndDate().toLocalTime());
            tmpEntryYear.changeStartTime(tmp.GetStartDate().toLocalTime());
            tmpEntryYear.changeEndTime(tmp.GetEndDate().toLocalTime());

            // Set full day
            tmpEntryMonth.setFullDay(tmp.fullDay);
            tmpEntryWeek.setFullDay(tmp.fullDay);
            tmpEntryYear.setFullDay(tmp.fullDay);
        }

        updatingEntries = false;
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
     * Method for the NextMonth Button
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
     * Method for the PreviousMonth Button
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
     * @param month month to change to
     */
    private void changeMonth(int month) {
        currentDate.setPlusMonth(month);
        CurrentDateLabel.setText(currentDate.getCurrentselectedDate(Date.MMYYYY));
        monthPage.setDate(currentDate.getSelectedDate());
    }

    /**
     * Changes the week view by the given value
     * @param week week to change to
     */
    private void changeWeek(int week) {
        currentDate.setPlusWeek(week);
        CurrentDateLabel.setText(currentDate.getCurrentselectedDate(Date.DDMMYYYY));
        weekPage.setDate(currentDate.getSelectedDate());
    }

    /**
     * Changes the year view by the given value
     * @param year year to change to
     */
    private void changeYear(int year) {
        currentDate.setPlusYear(year);
        CurrentDateLabel.setText(currentDate.getCurrentselectedDate(Date.YYYY));
        yearPage.setDate(currentDate.getSelectedDate());
    }

    //---------------------------------------------------Functions to get Data from the User---------------------------------------------------//
}