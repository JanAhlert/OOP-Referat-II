module com.jkfd.oopii {
    requires org.slf4j;

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires com.calendarfx.view;

    opens com.jkfd.oopii to javafx.fxml;
    exports com.jkfd.oopii;
    exports com.jkfd.oopii.Controller;
    exports com.jkfd.oopii.Database;
    exports com.jkfd.oopii.Database.Models;
    exports com.jkfd.oopii.Database.Repository;
    exports com.jkfd.oopii.Subsystem;
    exports com.jkfd.oopii.Subsystem.Subsystems;
    opens com.jkfd.oopii.Controller to javafx.fxml;
}