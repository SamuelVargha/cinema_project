package com.vargha;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("users.fxml"));
        Controller c = new Controller(primaryStage);
        loader.setControllerFactory(t -> c);
        Parent root = loader.load();
        primaryStage.setTitle("CinemaProject");
        primaryStage.getIcons().add(new Image("file:icon.png"));
        primaryStage.setScene(new Scene(root, 420, 270));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
