module com.mmorano.fantasyfootballfeed {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.mmorano.fantasyfootballfeed to javafx.fxml;
    exports com.mmorano.fantasyfootballfeed;
}