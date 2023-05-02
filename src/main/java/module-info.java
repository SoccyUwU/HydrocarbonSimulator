module com.example.hydrocarbonsimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hydrocarbonsimulator to javafx.fxml;
    exports com.example.hydrocarbonsimulator;
}