package com.jkfd.oopii.Controller;

import com.jkfd.oopii.Database.Models.Element;
import com.jkfd.oopii.Database.Models.Todo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.jkfd.oopii.Controller.MonthViewController.*;
import static com.jkfd.oopii.HelloApplication.databaseManager;

public class TodoPopUpController {
    final Logger logger = LoggerFactory.getLogger(MonthViewController.class);
    private boolean unsavedChanges = false;


    //The Elements are from the File EventPopUpEdit.fxml --> The following Elements are from the Tab Events
    @FXML
    TextField PopUpEdit_ToDo_TitleField;
    @FXML
    TextArea PopUpEdit_ToDo_DescriptionArea;
    @FXML
    ChoiceBox PopUpEdit_ToDo_PriorityChoiceBox;
    @FXML
    DatePicker PopUpEdit_ToDo_DueDatePicker;
    @FXML
    Button PopUpEdit_ToDo_SaveButton;

    /**
     * Initialization logic of the PopUpController.
     * If an already existing event was selected, it's data will be filled here.
     */
    public void initialize()
    {
        // Fill the priority choice box with all priority possibilities.
        PopUpEdit_ToDo_PriorityChoiceBox.getItems().setAll(Element.Priority.values());

        if (observedTodo != null) {
            PopUpEdit_ToDo_TitleField.setText(observedTodo.title);
            PopUpEdit_ToDo_DescriptionArea.setText(observedTodo.description);

            PopUpEdit_ToDo_PriorityChoiceBox.setValue(observedTodo.priority);
        } else {
            PopUpEdit_ToDo_PriorityChoiceBox.setValue(Element.Priority.NORMAL);
        }

        // Keep track if fields have been typed in or changed
        PopUpEdit_ToDo_TitleField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                unsavedChanges = true;
            }
        });

        PopUpEdit_ToDo_DescriptionArea.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                unsavedChanges = true;
            }
        });

        PopUpEdit_ToDo_PriorityChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
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
    private void onPopUpEditTodoSaveClicked(){
        try {
            // Create new event instance and parse basic information
            Todo tmp = new Todo();
            tmp.title = PopUpEdit_ToDo_TitleField.getText();
            tmp.description = PopUpEdit_ToDo_DescriptionArea.getText();
            tmp.priority = (Element.Priority) PopUpEdit_ToDo_PriorityChoiceBox.getValue();

            // Logic to determine if event is new or edited
            if (observedTodoAction == 0) {
                databaseManager.CreateTodo(tmp);
            } else if (observedTodoAction == 1) {
                tmp.SetID(observedTodo.GetID());
                databaseManager.UpdateTodo(tmp);
            } else {
                logger.atError().setMessage("Invalid observedEventAction: {}").addArgument(observedTodoAction).log();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Fehler");
                alert.setHeaderText("Ein Fehler ist aufgetreten");
                alert.setContentText("Wert von observedEventAction unbekannt: " + observedTodoAction);
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
            Stage stage = (Stage) PopUpEdit_ToDo_SaveButton.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            // We only debug log here because the code above only works when the stage has been set up by the create button.
            // It cannot be closed like this if it was a calendar popup.
            logger.atDebug().setMessage("Note: Error while casting stage from save button: {}").addArgument(e.getMessage()).log();
        }
    }
}
