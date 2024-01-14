package com.jkfd.oopii.Subsystem.Subsystems;

import com.jkfd.oopii.Database.Models.Event;
import com.jkfd.oopii.Subsystem.Subsystem;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import static com.jkfd.oopii.HelloApplication.databaseManager;

/**
 * The event reminder subsystem periodically checks if there is an upcoming event, and notifies the user accordingly.
 */
public class EventReminderSubsystem extends Subsystem {
    /**
     * This variable keeps track of which events the user was already notified of.
     */
    private final HashSet<Event> notified = new HashSet<>();

    public EventReminderSubsystem() {
        this.name = "event-reminders";
    }

    @Override
    public void Fire() {
        try {
            LocalDate now = LocalDate.now();
            ArrayList<Event> events = databaseManager.GetEvents();

            for (Event event : events)
            {
                if (event.GetStartDate().toLocalDate().isEqual(now) && !notified.contains(event)) {
                    ArrayList<Event> tmp = new ArrayList<>();
                    tmp.add(event);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            notified.addAll(tmp);

                            StringBuilder sb = new StringBuilder();
                            sb.append("Events die heute anstehen:\n");

                            for (Event event : tmp) {
                                sb.append("\t- ").append(event.title).append("\n");
                            }

                            Alert notification = new Alert(Alert.AlertType.INFORMATION);
                            notification.setTitle("Information");
                            notification.setHeaderText("Event Erinnerung");
                            notification.setContentText(sb.toString());
                            notification.showAndWait();
                        }
                    });

                    logger.atInfo().setMessage("Event notification: {}").addArgument(event.title).log();
                }
            }
        } catch(Exception e) {
            super.HandleException(e);
        }
    }
}
