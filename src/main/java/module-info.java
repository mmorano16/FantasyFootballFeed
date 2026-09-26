module com.mmorano.fantasyfootballfeed {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires java.xml.crypto;
    requires com.google.gson;

    opens com.mmorano.fantasyfootballfeed to javafx.fxml;
    exports com.mmorano.fantasyfootballfeed;
}