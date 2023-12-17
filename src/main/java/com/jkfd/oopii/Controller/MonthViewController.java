package com.jkfd.oopii.Controller;

import com.calendarfx.view.MonthView;
import com.calendarfx.view.YearView;
import com.calendarfx.view.page.WeekPage;
import javafx.event.Event;
import com.jkfd.oopii.Abstract.AbstractController;
import com.jkfd.oopii.Date;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
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

    private static boolean isInitialized = false;
    Date currentDate = new Date();

    /**
     * Initializes the month-view
     * @param stage
     * @throws IOException
     */
    public static void loadView(Stage stage) throws IOException{
        try {
        FXMLLoader fxmlLoader = new FXMLLoader(MonthViewController.class.getResource("/com/jkfd/oopii/month-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
        //Erstellung der MonthView aus dem CalendarFX
        MonthView monthView = new MonthView();
        monthView.setMinHeight(795);
        monthView.setMinWidth(1486);
        MonthViewController monthViewController = fxmlLoader.getController();
        monthViewController.MonthViewPane.getChildren().add(monthView);
        isInitialized = true;
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Ein Fehler ist aufgetreten");
            alert.setContentText("Es gab ein Problem beim Laden der View.");
            alert.showAndWait();
        }
    }

    /**
     * Sets the current date in the month-view
     */
    @FXML
    private void onSelectionMonthViewTab(Event event) {
            if (isInitialized == true) {
                try {
                    MonthView monthView = new MonthView();
                    monthView.setMinHeight(795);
                    monthView.setMinWidth(1486);
                    MonthViewPane.getChildren().clear();
                    MonthViewPane.getChildren().add(monthView);
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
    private void onSelectionYearViewTab(Event event){
        try {
            YearView yearView = new YearView();
            yearView.setMinHeight(795);
            yearView.setMinWidth(1486);
            MonthViewPane.getChildren().clear();
            MonthViewPane.getChildren().add(yearView);
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
    private void onSelectionWeekViewTab(Event event){
        try {

            WeekPage weekPage = new WeekPage();
            weekPage.setMinHeight(795);
            weekPage.setMinWidth(1486);
            weekPage.setShowNavigation(false);
            weekPage.setShowDate(false);
            MonthViewPane.getChildren().clear();
            MonthViewPane.getChildren().add(weekPage);
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
    private void setCurrentDateLabel(){
        CurrentDateLabel.setText(currentDate.getCurrentDate());
    }

    /**
     * Method that is loaded when the view is initialized
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCurrentDateLabel();
    }
}


