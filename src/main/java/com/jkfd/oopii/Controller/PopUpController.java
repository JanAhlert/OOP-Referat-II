package com.jkfd.oopii.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class PopUpController {

    private static FXMLLoader fxmlLoader_edit = new FXMLLoader(PopUpController.class.getResource("/com/jkfd/oopii/PopUpEdit.fxml"));    //FXMLLoader for the pop-upView
    private static FXMLLoader fxmlLoader_check = new FXMLLoader(PopUpController.class.getResource(("/com/jkfd/oopii/PopUpCheck.fxml")));

    private static FXMLLoader fxmlLoader_savedmessage = new FXMLLoader(PopUpController.class.getResource("/com/jkfd/oopii/PopUpSavedMessage.fxml"));

    private static FXMLLoader fxmlLoader_warningmessage = new FXMLLoader(PopUpController.class.getResource("/com/jkfd/oopii/PopUpWarningMessage.fxml"));


    /**
     * Method to load the pop-upView file and set the scene in the stage
     * @param stage
     * @throws IOException
     */

    @FXML

    //The Elements are from the File PopUpEdit.fxml --> The following Elements are from the Tab ToDo

    private TextField PopUpEdit_ToDo_TextField;
    private TextArea PopUpEdit_ToDo_TextArea;
    private ChoiceBox PopUpEdit_ToDo_PriorityChoiceBox;
    private DatePicker PopUpEdit_ToDo_DatePicker;
    private Button PopUpEdit_ToDo_SaveButton;
    private Button PopUpEdit_ToDo_CancelButton;
    private Button PopUpEdit_ToDo_CloseButton;

    //The Elements are from the File PopUpEdit.fxml --> The following Elements are from the Tab Events

    private TextField PopUpEdit_Event_TextField;
    private DatePicker PopUpEdit_Event_DatePicker;
    private DatePicker PopUpEdit_Event_TimePicker;
    private TextArea PopUpEdit_Event_TextArea;

    private ChoiceBox PopUpEdit_Event_PriorityChoiceBox;
    private Button PopUpEdit_Event_SaveButton;
    private Button PopUpEdit_Event_CancelButton;
    private Button PopUpEdit_Event_CloseButton;

    //The Elements are from the File PopUpCheck --> The following Elements are from the Tab "Aktuelle Aufgaben und Events".

    private Button PopUpCheck_ActiveEventsToDos_activToD_editButton;
    private Button PopUpCheck_ActiveEventsToDos_activToD_deleteButton;
    private Button PopUpCheck_ActiveEventsToDos_activEvent_editButton;
    private Button PopUpCheck_ActiveEventsToDos_activEvent_deleteButton;
    private Label  PopUpCheck_ActiveEventsToDos_LabelMessage;
    private Button PopUpCheck_ActiveEventsToDos_SaveButton;
    private Button PopUpCheck_ActiveEventsToDos_CancelButton;
    private Button PopUpCheck_ActiveEventsToDos_CloseButton;

    //The Elements are from the File PopUpCheck --> The following Elements are from the Tab "Erledigte Aufgaben und Events".

    private Button PopUpCheck_finishedEventsToDos_delTodos_RecoveryButton;
    private Button PopUpCheck_finishedEventsToDos_delTodos_FinallydelButton;
    private Button PopUpCheck_finishedEventsToDos_delEvents_RecoveryButton;
    private Button PopUpCheck_finishedEventsToDos_delEvents_FinallyButton;

    private Label PopUpCheck_finishedEventsToDos_delEvents_LabelMessage;
    private Button PopUpCheck_finishedEventsToDos_SaveButton;
    private Button PopUpCheck_finishedEventsToDos_CancelButton;
    private Button PopUpCheck_finishedEventsToDos_CloseButton;


    public static void loadView(Stage stage) throws IOException {
            Scene scene = new Scene(fxmlLoader_edit.load());
            stage.setScene(scene);
            stage.show();
            stage.setTitle("Termin bearbeiten");
    }




}
