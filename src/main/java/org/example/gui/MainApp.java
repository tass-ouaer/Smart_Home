package org.example.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/views/home.fxml"));



        Scene scene = new Scene(loader.load(), 1200, 800);
        
        primaryStage.setTitle("Smart Home Control");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}