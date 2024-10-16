module com.projetolp3.projetolp3_grupo2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.projetolp3.projetolp3_grupo2 to javafx.fxml;
    exports com.projetolp3.projetolp3_grupo2;
}