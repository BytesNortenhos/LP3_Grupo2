module bytesnortenhos.projetolp3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.sql.rowset;
    requires jakarta.xml.bind;
    requires java.desktop;
    requires java.dotenv;


    opens bytesnortenhos.projetolp3 to javafx.fxml;
    exports bytesnortenhos.projetolp3;
    exports controllers;
    exports Models;
    exports AuxilierXML;
    opens controllers to javafx.fxml, jakarta.xml.bind;
    opens Models to jakarta.xml.bind;
    opens AuxilierXML to jakarta.xml.bind;
    exports controllers.admin;
    opens controllers.admin to jakarta.xml.bind, javafx.fxml;
    exports controllers.athletes;
    opens controllers.athletes to jakarta.xml.bind, javafx.fxml;
}