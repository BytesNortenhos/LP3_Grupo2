module bytesnortenhos.projetolp3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens bytesnortenhos.projetolp3 to javafx.fxml;
    exports bytesnortenhos.projetolp3;
    exports controllers;
}