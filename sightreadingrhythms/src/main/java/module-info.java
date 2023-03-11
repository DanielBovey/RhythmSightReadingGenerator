module com.dbov {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.dbov to javafx.fxml;
    exports com.dbov;
}
