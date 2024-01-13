package com.jkfd.oopii.Controller;

import com.jkfd.oopii.Database.Models.Element;
import com.jkfd.oopii.Database.Models.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private static FXMLLoader fxmlLoader_warningmessage = new FXMLLoader(PopUpController.class.getResource("/com/jkfd/oopii/PopUpWarningMessage.fxml"));


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
    }

    @FXML
    private void onPopUpEditEventSaveClicked(){
        try {
            Event tmp = new Event();
            tmp.title = PopUpEdit_Event_TitleTextField.getText();
            tmp.description = PopUpEdit_Event_DescriptionTextArea.getText();
            tmp.priority = (Element.Priority) PopUpEdit_Event_PriorityChoiceBox.getValue();
            tmp.SetDateRange(java.sql.Date.valueOf(PopUpEdit_Event_StartDatePicker.getValue()), java.sql.Date.valueOf(PopUpEdit_Event_EndDatePicker.getValue()));

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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Fehler");
            alert.setHeaderText("Ein Fehler ist aufgetreten");
            alert.setContentText("Es gab ein Problem beim speichern des Events.\n" + e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void onCloseButtonPressed() {

    }
}
