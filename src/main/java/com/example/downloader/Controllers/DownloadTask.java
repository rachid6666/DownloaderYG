package com.example.downloader.Controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import javafx.scene.media.AudioClip;

import static com.example.downloader.Controllers.MainController.fileName;

public class DownloadTask  extends Task<Void> {
    private  ProgressBar progressBar;
    public String url;
    public String fileName;
    Button pauseButton,resumeButton,cancelButton;
    private boolean paused = false;
    private boolean cancelled = false;
    public  CountDownLatch countDownLatch;
    private String ext;

    final String destinationPath = "C://Users//Yahia Gadouche//Desktop//downloads//";
    public DownloadTask(String url , String filename,String ext, ProgressBar progressBar  , Button pauseButton, Button resumeButton,
                        Button cancelButton, CountDownLatch countDownLatch) {
        this.url=url;
        this.fileName=filename;
        this.progressBar=progressBar;
        this.countDownLatch=countDownLatch;
        this.pauseButton=pauseButton;
        this.resumeButton=resumeButton;
        this.cancelButton=cancelButton;
        this.ext=ext;


    }

    @Override
    protected Void call() throws Exception {
        //7ot real download here
        try {
            if (isConnectedToInternet()){
               // Path  targetPath= Paths.get("C://Users//Yahia Gadouche//Desktop//downloads//",fileName(url) + "." + fileExtension);

               // Path targetPath = Paths.get(destinationPath,fileName,ext);
                Path targetPath = Paths.get(fileName).resolveSibling(fileName );
                URL downloadUrl = new URL(url);
                URLConnection connection = downloadUrl.openConnection();
                connection.connect();

                int fileSize = connection.getContentLength();

                FileOutputStream outputStream = new FileOutputStream(targetPath.toFile());
                BufferedInputStream inputStream = new BufferedInputStream(downloadUrl.openStream());

                byte[] data = new byte[1024];
                int bytesRead;
                long totalBytesRead = 0;

                while (!cancelled && (bytesRead = inputStream.read(data)) != -1) {
                    if (paused) {
                        // Paused, so wait until resumed
                        while (paused) {
                            Thread.sleep(100); // Sleep to reduce CPU usage
                        }
                    }
                    outputStream.write(data, 0, bytesRead);
                    totalBytesRead += bytesRead;
                    updateProgress(totalBytesRead, fileSize);

                }
                System.out.println("wslt ll countdown");
                countDownLatch.countDown();

                if (countDownLatch.getCount() == 0) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Download completed successfully ");
                        alert.setHeaderText("Download completed successfully ");
                        alert.setContentText("Download completed successfully");

                       // AudioClip sound = new AudioClip(getClass().getResource("  C://Users//Yahia Gadouche//Documents//software_engenerring//dac//downloader//Windows Logon.wav").toString());
                      //  sound.play();
                        alert.showAndWait();
                    });
                }
                outputStream.close();
                inputStream.close();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No connection ");
                alert.setHeaderText("No connection to the internet");
                alert.setContentText("Please check your connection! ");
                alert.showAndWait();
            }




        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    @Override
    protected void succeeded(){
        System.out.println("Downloaded");


    }
    @Override
    protected void failed(){
        System.out.println("Failed!");
    }
    private static boolean isConnectedToInternet() {
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Test");
            connection.setRequestProperty("Connection", "close");
            connection.setConnectTimeout(1000);
            connection.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }
   public void Cancel() {
        cancelled = true;
    }


}
