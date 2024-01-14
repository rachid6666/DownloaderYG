package com.example.downloader.Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadItem {

    private  String fileName;
    @FXML
   public Pane pane;
    private Label Download;
    private Label ProgressLabel;
    private Label TitleLink;
    private Label Progress;

    private DownloadThread downloadThread;
    ProgressBar progressBar;
    Button pauseButton,resumeButton,cancelButton;
    private CountDownLatch countDownLatch;
    private String ext;

    public static ExecutorService executorService = Executors.newFixedThreadPool(3);
    public DownloadItem(String inputurl ,String fileName , String ext) {
        createUI(inputurl);
        this.fileName=fileName;
        this.ext=ext;

    }
    private void createUI(String inputurl) {

        System.out.println("wslt");
        //labels
        Download = creatLabel("Download");
        Download.setStyle("-fx-translate-x: 27;-fx-translate-y: 26");
        ProgressLabel = creatLabel("Progress");
        ProgressLabel.setStyle("-fx-translate-x: 27;-fx-translate-y: 51");
        Progress = creatLabel("Progress");
        Progress.setStyle("-fx-translate-x: 159;-fx-translate-y: 52");
        TitleLink = creatLabel(inputurl);
        TitleLink.setStyle("-fx-translate-x: 95;-fx-translate-y: 26; -fx-pref-width: 150");


        // Initialize buttons
        pauseButton = createButton("Pause", event -> pauseDownload());
        pauseButton.setStyle("-fx-translate-x: 362;-fx-translate-y: 43;-fx-pref-width: 68;-fx-pref-height: 31");
         resumeButton = createButton("Resume", event -> resumeDownload());
         //x; 294
        resumeButton.setStyle("-fx-translate-x: 362;-fx-translate-y: 43;-fx-pref-width: 68;-fx-pref-height: 31");
        resumeButton.setVisible(false);
         cancelButton = createButton("X", event -> cancelDownload());
        cancelButton.setStyle("-fx-translate-x: 412;-fx-translate-y: 15;-fx-opacity: 0.27");

        pane=new Pane();
        pane.setPrefSize(451,93);
        pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");


        pane.getChildren().addAll( pauseButton, resumeButton, cancelButton,Download,TitleLink,Progress,ProgressLabel);


    }
    private Button createButton(String text, EventHandler<ActionEvent> handler) {
        Button button = new Button(text);

        button.setOnAction(handler);
        return button;
    }
    private Label creatLabel(String name){
       Label label = new Label(name);
        //label.setText(name);
        return label;
    }
    public void pauseDownload() {
        if (downloadTask != null) {
            pauseButton.setVisible(false);
            resumeButton.setVisible(true);
            System.out.println("paused");
            downloadTask.pause();
        }
    }

    public void resumeDownload() {
        if (downloadTask != null) {
            pauseButton.setVisible(true);
            resumeButton.setVisible(false);

            downloadTask.resume();
        }
    }

    public void cancelDownload() {
        if (downloadTask != null) {
            pane.setVisible(false);
           downloadTask.cancel();
        }
    }
    private DownloadTask downloadTask;
    public void startDownload(  CountDownLatch countDownLatch) {
        // Initialize the latch


        downloadTask = new DownloadTask(TitleLink.getText().trim(),fileName,ext,progressBar
                , pauseButton, resumeButton, cancelButton, countDownLatch);


        //progress bar

        progressBar = new ProgressBar(0);
        progressBar.setStyle("-fx-translate-x: 87;-fx-translate-y: 51;-fx-pref-width: 200");
        progressBar.progressProperty().bind(downloadTask.progressProperty());
        pane.getChildren().add(progressBar);

        executorService.submit(downloadTask);
    }
}
