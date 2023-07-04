package com.example.booksmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("MainFrame.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 840, 420);
        ((MainController) fxmlLoader.getController()).initialize();
        stage.setTitle("Bookshelf");
        stage.setMinWidth(840);
        stage.setMinHeight(460);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}