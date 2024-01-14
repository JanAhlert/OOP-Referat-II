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

import static com.jkfd.oopii.Controller.MonthViewController.observedEvent;
import static com.jkfd.oopii.Controller.MonthViewController.observedEventAction;
import static com.jkfd.oopii.HelloApplication.databaseManager;

public class PopUpController {
    final Logger logger = LoggerFactory.getLogger(MonthViewController.class);

    private static FXMLLoader fxmlLoader_edit = new FXMLLoader(PopUpController.class.getResource("/com/jkfd/oopii/PopUpEdit.fxml"));    //FXMLLoader for the pop-upView
    private static FXMLLoader fxmlLoader_check = new FXMLLoader(PopUpController.class.getResource(("/com/jkfd/oopii/PopUpCheck.fxml")));

    private boolean unsavedChanges = false;

    //The Elements are from the File PopUpEdit.fxml --> The following Elements are from the Tab ToDo
    @FXML
    TextField PopUpEdit_ToDo_TextField;
    @FXML
    TextArea PopUpEdit_ToDo_TextArea;
    @FXML
    ChoiceBox PopUpEdit_ToDo_PriorityChoiceBox;
    @FXML
    DatePicker PopUpEdit_ToDo_DatePicker;
    @FXML
    Button PopUpEdit_ToDo_SaveButton;

    //The Elements are from the File PopUpEdit.fxml --> The following Elements are from the Tab Events
    @FXML
    TextField PopUpEdit_Event_TitleTextField;
    @FXML
    DatePicker PopUpEdit_Event_StartDatePicker;
    @FXML
    DatePicker PopUpEdit_Event_EndDatePicker;
    @FXML
    TextArea PopUpEdit_Event_DescriptionTextArea;
    @FXML
    ChoiceBox PopUpEdit_Event_PriorityChoiceBox;
    @FXML
    Button PopUpEdit_Event_SaveButton;

    //The Elements are from the File PopUpCheck --> The following Elements are from the Tab "Aktuelle Aufgaben und Events".
    @FXML
    Button PopUpCheck_activToD_editButton;
    @FXML
    Button PopUpCheck_activToD_deleteButton;
    @FXML
    Button PopUpCheck_activEvent_editButton;
    @FXML
    Button PopUpCheck_activEvent_deleteButton;
    @FXML
    Label  PopUpCheck_ActiveEventsToDos_LabelMessage;
    @FXML
    Button PopUpCheck_ActiveEventsToDos_SaveButton;
    @FXML
    Button PopUpCheck_ActiveEventsToDos_CancelButton;
    @FXML
    Button PopUpCheck_ActiveEventsToDos_CloseButton;

    //The Elements are from the File PopUpCheck --> The following Elements are from the Tab "Erledigte Aufgaben und Events".
    @FXML
    Button PopUpCheck_finished_delTodos_RecoveryButton;
    @FXML
    Button PopUpCheck_finished_delTodos_FinallydelButton;
    @FXML
    Button PopUpCheck_finished_delEvents_RecoveryButton;
    @FXML
    Button PopUpCheck_finished_delEvents_FinallydelButton;
    @FXML
    Label PopUpCheck_finished_delEvents_LabelMessage;
    @FXML
    Button PopUpCheck_finished_SaveButton;
    @FXML
    Button PopUpCheck_finished_CancelButton;
    @FXML
    Button PopUpCheck_finished_CloseButton;

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
        // Fill the priority choice box with all priority possibilities.
        PopUpEdit_Event_PriorityChoiceBox.getItems().setAll(Element.Priority.values());

        if (observedEvent != null) {
            PopUpEdit_Event_TitleTextField.setText(observedEvent.title);
            PopUpEdit_Event_DescriptionTextArea.setText(observedEvent.description);
            PopUpEdit_Event_StartDatePicker.setValue(observedEvent.GetStartDate().toLocalDate());
            PopUpEdit_Event_EndDatePicker.setValue(observedEvent.GetEndDate().toLocalDate());

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
            Event tmp = new Event();
            tmp.title = PopUpEdit_Event_TitleTextField.getText();
            tmp.description = PopUpEdit_Event_DescriptionTextArea.getText();
            tmp.priority = (Element.Priority) PopUpEdit_Event_PriorityChoiceBox.getValue();
            tmp.SetDateRange(PopUpEdit_Event_StartDatePicker.getValue().atStartOfDay(), PopUpEdit_Event_EndDatePicker.getValue().atStartOfDay()); // TODO: Time is always currently 00:00:00

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

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("");
            info.setHeaderText("Hinweis!");
            info.setContentText("Ihre änderungen wurden gespeichert!");
            info.showAndWait();
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
        }
    }
}
