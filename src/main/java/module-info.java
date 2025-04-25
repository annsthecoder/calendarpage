module com.example.calendarpage {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.calendarpage to javafx.fxml;
    exports com.example.calendarpage;
}