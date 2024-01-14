package com.example.downloader.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

public class MainController  {
@FXML
    public Button pausebtn;
    @FXML
    public Pane downloadContainer;
    @FXML
    public Label downloadTitle;
    public Button downloadAll;

    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button addbtn;
    @FXML
    public VBox vbox;

    @FXML
    private TextField urlTxt;
    public List<DownloadItem> downloadItems = new ArrayList<>();
   final String destinationPath = "C://Users//Yahia Gadouche//Desktop//downloads//";


    public void clicked(ActionEvent actionEvent) {

        if (urlTxt != null) {

            try {
                // Validate the URL
                String inputUrl = urlTxt.getText();
                URL url = new URL(inputUrl);
                url.toURI();
                //creates container the first container
                String fileName = new File(url.getPath()).getName();

                Path targetPath = Paths.get(destinationPath,fileName);
                String ext=getFileExtension(inputUrl);

                DownloadItem newDownloadItem = createDownloadItem(inputUrl,fileName,ext);

                vbox.getChildren().add(newDownloadItem.pane);
                urlTxt.setText("");





            } catch (MalformedURLException | URISyntaxException e) {
                //msg box alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid URL");
                alert.setHeaderText("URL is not valid");
                alert.setContentText("Please enter a valid URL to download.");
                alert.showAndWait();
            }
        } else {
            //msg box alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("empty url");
            alert.setHeaderText("empty text !");
            alert.setContentText("Please enter a URL to download.");
            alert.showAndWait();
        }

    }
    private DownloadItem createDownloadItem(String inputUrl, String filename,String ext) {
        DownloadItem downloadItem = new DownloadItem(inputUrl,filename,ext);
        downloadItems.add(downloadItem);
        // Add each item to the list

        return downloadItem;
    }


    public void cancel(ActionEvent actionEvent) {
        //destroy the donwload container
    }


    public void downloadall(ActionEvent actionEvent) {

        CountDownLatch countDownLatch = new CountDownLatch(downloadItems.size());
        System.out.println(downloadItems.size());
        for (DownloadItem downloadItem : downloadItems) {
            downloadItem.startDownload(countDownLatch);
        }
    }
    static String fileName(String url) {
        // Extract the file name from the URL
        String[] parts = url.split("/");
        return parts[parts.length - 1];
    }
    private static String getFileExtension(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }

        int lastIndexOf = filePath.lastIndexOf('.');
        if (lastIndexOf == -1) {
            return "";
        }

        return filePath.substring(lastIndexOf + 1);
    }
}

