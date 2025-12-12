package org.example.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.example.backend.home.*;
import org.example.backend.controller.CentralController;
import org.example.backend.scheduler.Scheduler;
import org.example.gui.controllers.DashboardPageController;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // ✅ Create core backend objects ONCE
        Home home = HomeFactory.createDefaultHome();
        CentralController centralController = new CentralController(home);
        Scheduler scheduler = new Scheduler();

        // ✅ Load FXML
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/gui/views/home.fxml")
        );

        Scene scene = new Scene(loader.load(), 1200, 800);

        // ✅ Inject dependencies into controller
        DashboardPageController controller = loader.getController();
        controller.init(centralController, scheduler);

        stage.setTitle("Smart Home Control");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
