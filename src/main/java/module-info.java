module com.jkfd.oopii {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.calendarfx.view;

    opens com.jkfd.oopii to javafx.fxml;
    exports com.jkfd.oopii;
    exports com.jkfd.oopii.Controller;
    opens com.jkfd.oopii.Controller to javafx.fxml;
}