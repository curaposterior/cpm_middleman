package org.cpm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CPMApplication extends Application {
    static Object controller;
    @Override
    public void start(Stage stage) throws IOException {
        load("input.fxml", stage);
    }

    public static void load(String file, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CPMApplication.class.getResource(file));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        if (stage == null)
            stage = new Stage();
        stage.setTitle("CPM");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
        controller = fxmlLoader.getController();
    }

    public static Object getController() {
        return controller;
    }

    public static void main(String[] args) {
        launch();
    }
}