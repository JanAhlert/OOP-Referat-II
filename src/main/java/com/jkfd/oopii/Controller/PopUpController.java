package com.jkfd.oopii.Controller;

import com.jkfd.oopii.Database.Models.Todo;
import com.jkfd.oopii.Database.Repository.SQLiteDB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PopUpController {


    @FXML
    Button safe;
    @FXML
    TextArea ToDoDescription;
    @FXML
    TextField ToDoTitel;

    private static FXMLLoader fxmlLoader = new FXMLLoader(PopUpController.class.getResource("/com/jkfd/oopii/PopUpEdit.fxml"));    //FXMLLoader for the pop-upView
    private  SQLiteDB SQ = new SQLiteDB();

    public PopUpController() throws Exception {
    }

    /**
     * Method to load the pop-upView file and set the scene in the stage
     * @param stage
     * @throws IOException
     */
    public static void loadView(Stage stage) throws IOException {
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
            stage.setTitle("Termin bearbeiten");

    }




    //----------------------------------------------------------------Functions to Collect Datas from User------------------------------------------------

    @FXML
    public void onSafe(){
        Todo TD = new Todo(ToDoTitel.getText(), ToDoDescription.getText());
        SQ.CreateTodo(TD);
        System.out.println("Test");

    }



}
