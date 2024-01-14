module com.example.downloader {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires javafx.media;


    opens com.example.downloader to javafx.fxml;
    opens com.example.downloader.Controllers to javafx.fxml;
    exports com.example.downloader;
    exports com.example.downloader.Controllers;
}