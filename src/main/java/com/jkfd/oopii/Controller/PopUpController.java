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

    //Elements from the File PopUpEdit.fxml --> These Elements are from the Tab ToDo

    private TextField PopUpEdit_ToDo_TextField;
    private TextArea PopUpEdit_ToDo_TextArea;
    private ChoiceBox PopUpEdit_ToDo_PriorityChoiceBox;
    private DatePicker PopUpEdit_ToDo_DatePicker;
    private Button PopUpEdit_ToDo_SaveButton;
    private Button PopUpEdit_ToDo_CancelButton;
    private Button PopUpEdit_ToDo_CloseButton;

    //Elements from tge File PopUpEdit.fxml --> These Elements are from the Tab Events

    private TextField PopUpEdit_Event_TextField;
    private DatePicker PopUpEdit_Event_DatePicker;
    private DatePicker PopUpEdit_Event_TimePicker;
    private TextArea PopUpEdit_Event_TextArea;

    private ChoiceBox PopUpEdit_Event_PriorityChoiceBox;
    private Button PopUpEdit_Event_SaveButton;
    private Button PopUpEdit_Event_CancelButton;
    private Button PopUpEdit_Event_CloseButton;

    //


    public static void loadView(Stage stage) throws IOException {
            Scene scene = new Scene(fxmlLoader_edit.load());
            stage.setScene(scene);
            stage.show();
            stage.setTitle("Termin bearbeiten");
    }




}
