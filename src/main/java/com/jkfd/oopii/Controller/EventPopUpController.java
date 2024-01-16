package com.jkfd.oopii.Controller;

import com.jkfd.oopii.Database.Models.Element;
import com.jkfd.oopii.Database.Models.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.jkfd.oopii.Controller.MonthViewController.*;
import static com.jkfd.oopii.HelloApplication.databaseManager;

public class EventPopUpController {
    final Logger logger = LoggerFactory.getLogger(MonthViewController.class);

    private static FXMLLoader fxmlLoader_edit = new FXMLLoader(EventPopUpController.class.getResource("/com/jkfd/oopii/EventPopUpEdit.fxml"));    //FXMLLoader for the pop-upView

    private boolean unsavedChanges = false;


    //The Elements are from the File EventPopUpEdit.fxml --> The following Elements are from the Tab Events
    @FXML
    TextField PopUpEdit_Event_TitleTextField;
    @FXML
    DatePicker PopUpEdit_Event_StartDatePicker;
    @FXML
    TextField PopUpEdit_Event_StartTimeField;
    @FXML
    DatePicker PopUpEdit_Event_EndDatePicker;
    @FXML
    TextField PopUpEdit_Event_EndTimeField;
    @FXML
    CheckBox PopUpEdit_Event_FullDayCheckbox;
    @FXML
    TextArea PopUpEdit_Event_DescriptionTextArea;
    @FXML
    ChoiceBox PopUpEdit_Event_PriorityChoiceBox;
    @FXML
    Button PopUpEdit_Event_SaveButton;

    /**
     * Method to load the pop-upView file and set the scene in the stage
     * @param stage
     * @throws IOException
     */
    public static void loadView(Stage stage) throws IOException {
        Scene scene = new Scene(fxmlLoader_edit.load());
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Termin bearbeiten");
    }

    /**
     * Initialization logic of the PopUpController.
     * If an already existing event was selected, it's data will be filled here.
     */
    public void initialize()
    {
        PopUpEdit_Event_StartTimeField.setEditable(true);
        PopUpEdit_Event_EndTimeField.setEditable(true);

        PopUpEdit_Event_FullDayCheckbox.setSelected(false);

        // Fill the priority choice box with all priority possibilities.
        PopUpEdit_Event_PriorityChoiceBox.getItems().setAll(Element.Priority.values());

        if (observedEvent != null) {
            if (observedEvent.fullDay)
            {
                PopUpEdit_Event_StartTimeField.setEditable(false);
                PopUpEdit_Event_EndTimeField.setEditable(false);
            }

            PopUpEdit_Event_FullDayCheckbox.setSelected(observedEvent.fullDay);

            PopUpEdit_Event_TitleTextField.setText(observedEvent.title);
            PopUpEdit_Event_DescriptionTextArea.setText(observedEvent.description);
            PopUpEdit_Event_StartDatePicker.setValue(observedEvent.GetStartDate().toLocalDate());
            PopUpEdit_Event_EndDatePicker.setValue(observedEvent.GetEndDate().toLocalDate());
            PopUpEdit_Event_StartTimeField.setText(observedEvent.GetStartDate().toLocalTime().toString());
            PopUpEdit_Event_EndTimeField.setText(observedEvent.GetEndDate().toLocalTime().toString());

            PopUpEdit_Event_PriorityChoiceBox.setValue(observedEvent.priority);
        } else {
            PopUpEdit_Event_PriorityChoiceBox.setValue(Element.Priority.NORMAL);
        }

        // Keep track if fields have been typed in or changed
        PopUpEdit_Event_TitleTextField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                unsavedChanges = true;
            }
        });

        PopUpEdit_Event_DescriptionTextArea.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                unsavedChanges = true;
            }
        });

        PopUpEdit_Event_StartDatePicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                unsavedChanges = true;
            }
        });

        PopUpEdit_Event_EndDatePicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                unsavedChanges = true;
            }
        });

        PopUpEdit_Event_PriorityChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                unsavedChanges = true;
            }
        });
    }

    /**
     * Fired when the user presses the save button.
     * The edited/new event should be persisted into the database and the user informed that changes have been saved.
     */
    @FXML
    private void onPopUpEditEventSaveClicked(){
        try {
            // Create new event instance and parse basic information
            Event tmp = new Event();
            tmp.title = PopUpEdit_Event_TitleTextField.getText();
            tmp.description = PopUpEdit_Event_DescriptionTextArea.getText();
            tmp.priority = (Element.Priority) PopUpEdit_Event_PriorityChoiceBox.getValue();

            // Time & Date of the event
            LocalTime tmpStartTime = LocalTime.parse(PopUpEdit_Event_StartTimeField.getText());
            LocalDateTime tmpStartDateTime = PopUpEdit_Event_StartDatePicker.getValue().atStartOfDay().withHour(tmpStartTime.getHour()).withMinute(tmpStartTime.getMinute()).withSecond(tmpStartTime.getSecond());

            LocalTime tmpEndTime = LocalTime.parse(PopUpEdit_Event_EndTimeField.getText());
            LocalDateTime tmpEndDateTime = PopUpEdit_Event_EndDatePicker.getValue().atStartOfDay().withHour(tmpEndTime.getHour()).withMinute(tmpEndTime.getMinute()).withSecond(tmpEndTime.getSecond());

            tmp.SetDateRange(tmpStartDateTime, tmpEndDateTime);

            tmp.fullDay = PopUpEdit_Event_FullDayCheckbox.isSelected();

            // Logic to determine if event is new or edited
            if (observedEventAction == 0) {
                databaseManager.CreateEvent(tmp);
            } else if (observedEventAction == 1) {
                tmp.SetID(observedEvent.GetID());
                databaseManager.UpdateEvent(tmp);
            } else {
                logger.atError().setMessage("Invalid observedEventAction: {}").addArgument(observedEventAction).log();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehler");
                alert.setHeaderText("Ein Fehler ist aufgetreten");
                alert.setContentText("Wert von observedEventAction unbekannt: " + observedEventAction);
                alert.showAndWait();
            }

            // Update view
            UpdateEntries();

            // Notify user
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("");
            info.setHeaderText("Hinweis!");
            info.setContentText("Ihre änderungen wurden gespeichert!");
            info.showAndWait();

            TryCloseStage();
        } catch (Exception e) {
            logger.atError().setMessage("Error while trying to save event: {}").addArgument(e.getMessage()).log();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Ein Fehler ist aufgetreten");
            alert.setContentText("Es gab ein Problem beim speichern des Events.\n" + e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    /**
     * Fired when the user presses the close button.
     * Warning should be displayed if there were changes. Otherwise, just close the window.
     */
    @FXML
    private void onCloseButtonPressed() {
        if (unsavedChanges)
        {
            Alert warning = new Alert(Alert.AlertType.CONFIRMATION);
            warning.setTitle("Warnung");
            warning.setHeaderText("Ungespeicherte änderungen");
            warning.setContentText("Wollen sie wirklich die änderungen verwerfen?");
            warning.showAndWait();

            if (warning.getResult() == ButtonType.OK) {
                TryCloseStage();
            }
        } else {
            TryCloseStage();
        }
    }

    /**
     * This private function will try to close the window, which is only successful when it is standalone.
     * The code throwing an exception is "normal" when it is a popup in the calendar.
     */
    private void TryCloseStage() {
        try {
            Stage stage = (Stage) PopUpEdit_Event_SaveButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            // We only debug log here because the code above only works when the stage has been set up by the create button.
            // It cannot be closed like this if it was a calendar popup.
            logger.atDebug().setMessage("Note: Error while casting stage from save button: {}").addArgument(e.getMessage()).log();
        }
    }
}
