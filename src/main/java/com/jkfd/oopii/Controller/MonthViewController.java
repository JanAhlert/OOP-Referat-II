package com.jkfd.oopii.Controller;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.page.MonthPage;
import com.calendarfx.view.page.WeekPage;
import com.calendarfx.view.page.YearPage;
import com.jkfd.oopii.Database.Models.Element;
import com.jkfd.oopii.Database.Models.Event;
import com.jkfd.oopii.Database.Models.Todo;
import com.jkfd.oopii.Date;
import com.jkfd.oopii.HelloApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static com.jkfd.oopii.HelloApplication.databaseManager;

/**
 * Controller for the month-view
 */
public class MonthViewController implements Initializable {
    static final Logger logger = LoggerFactory.getLogger(MonthViewController.class);

    public static Event observedEvent = null;
    public static Todo observedTodo = null;
    /**
     * 0 - New
     * 1 - Edit
     */
    public static int observedEventAction = 0;
    public static int observedTodoAction = 0;

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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jkfd/oopii/EventPopUpEdit.fxml"));
                return loader.load();
            } catch (IOException e) {
                logger.atError().setMessage("Error while loading PopUpEdit content: {}").addArgument(e.getMessage()).log();
                return new Label("Fehler beim Laden des Inhalts"); // Throws an Error Lable for the User
            }
        });

        UpdateEntries();

        createTodoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
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
            }
        });

        createEventButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
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
            }
        });
    }

    /**
     * Updates the entries in the calendar and sidebar.
     */
    public static void UpdateEntries() {
        Calendar defaultCalendar = monthPage.getMonthView().getCalendarSources().get(0).getCalendars().get(0);
        List entries = defaultCalendar.findEntries("");
        defaultCalendar.removeEntries(entries);

        // Load the right sidebar
        monthViewController.EventsVBox.getChildren().clear();
        monthViewController.TodosVBox.getChildren().clear();

        ArrayList<Event> events = databaseManager.GetEvents(3);
        ArrayList<Todo> todos = databaseManager.GetUnfinishedTodos(3);

        for (Event tmp : events) {
            Label tmpLabel = new Label();
            tmpLabel.setText(tmp.title);

            monthViewController.EventsVBox.getChildren().add(tmpLabel);
        }

        for (Todo tmp : todos) {
            HBox tmpHBox = new HBox();

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
            tmpHBox.getChildren().add(tmpCheckbox);

            Label tmpEditLabel = new Label();
            tmpEditLabel.setText("[Edit]");
            tmpEditLabel.setPadding(new Insets(0, 0, 0, 5));
            tmpEditLabel.setTextFill(Color.color(0, 0.3, 0.7));
            tmpEditLabel.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
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
                }
            });
            tmpHBox.getChildren().add(tmpEditLabel);

            Label tmpDeleteLabel = new Label();
            tmpDeleteLabel.setText("[Del]");
            tmpDeleteLabel.setPadding(new Insets(0, 0, 0, 5));
            tmpDeleteLabel.setTextFill(Color.color(0.7, 0.2, 0));
            tmpDeleteLabel.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    databaseManager.DeleteTodo(tmp.GetID());
                    UpdateEntries();
                }
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