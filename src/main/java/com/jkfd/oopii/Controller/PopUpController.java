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
    @FXML
    Button PopUpEdit_ToDo_CancelButton;
    @FXML
    Button PopUpEdit_ToDo_CloseButton;

    //The Elements are from the File PopUpEdit.fxml --> The following Elements are from the Tab Events

    @FXML
    TextField PopUpEdit_Event_TextField;
    @FXML
    DatePicker PopUpEdit_Event_DatePicker;
    @FXML
    DatePicker PopUpEdit_Event_TimePicker;
    @FXML
    TextArea PopUpEdit_Event_TextArea;

    @FXML
    ChoiceBox PopUpEdit_Event_PriorityChoiceBox;
    @FXML
    Button PopUpEdit_Event_SaveButton;
    @FXML
    Button PopUpEdit_Event_CancelButton;
    @FXML
    Button PopUpEdit_Event_CloseButton;

    //The Elements are from the File PopUpCheck --> The following Elements are from the Tab "Aktuelle Aufgaben und Events".

    @FXML
    Button PopUpCheck_activToD_editButton;
    @FXML
    Button PopUpCheck_ActiveEventsToDos_activToD_deleteButton;
    @FXML
    Button PopUpCheck_ActiveEventsToDos_activEvent_editButton;
    @FXML
    Button PopUpCheck_ActiveEventsToDos_activEvent_deleteButton;
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
    Button PopUpCheck_finishedEventsToDos_delTodos_RecoveryButton;
    @FXML
    Button PopUpCheck_finishedEventsToDos_delTodos_FinallydelButton;
    @FXML
    Button PopUpCheck_finishedEventsToDos_delEvents_RecoveryButton;
    @FXML
    Button PopUpCheck_finishedEventsToDos_delEvents_FinallyButton;
    @FXML
    Label PopUpCheck_finishedEventsToDos_delEvents_LabelMessage;
    @FXML
    Button PopUpCheck_finishedEventsToDos_SaveButton;
    @FXML
    Button PopUpCheck_finishedEventsToDos_CancelButton;
    @FXML
    Button PopUpCheck_finishedEventsToDos_CloseButton;

    public static void loadView(Stage stage) throws IOException {
            Scene scene = new Scene(fxmlLoader_edit.load());
            stage.setScene(scene);
            stage.show();
            stage.setTitle("Termin bearbeiten");
    }




}
