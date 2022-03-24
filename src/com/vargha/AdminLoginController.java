package com.vargha;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminLoginController implements Initializable {
    @FXML private JFXTextField nameField;
    @FXML private JFXPasswordField passField;
    @FXML private JFXButton logBut;
    private Stage primaryStage;
    private Stage stage;
    private Stage adminStage;
    private AHomeController aHomeController;

    public AdminLoginController(Stage primaryStage, AHomeController aHomeController, Stage stage, Stage adminStage) {
        this.primaryStage = primaryStage;
        this.aHomeController = aHomeController;
        this.stage = stage;
        this.adminStage = adminStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    //gets name and password from textfields and checks if they are in the database, then loads aHomeController
    @FXML
    private void login(){
        String name = nameField.getText();
        String password = passField.getText();
        Admin admin = new Admin(new ArrayList<>());
        aHomeController.setAdmin(admin);
        try {
            Connection connection = DbConnect.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet r = statement.executeQuery("select * from tableAdmins where name = '"+name+"' and password = '"+password+"'");

            if(r.next()){
                FXMLLoader newLoader = new FXMLLoader(getClass().getResource("aHomeUI.fxml"));
                newLoader.setControllerFactory(t -> aHomeController);
                try {
                    stage.setTitle("CinemaProject");
                    Parent root = newLoader.load();
                    stage.setScene(new Scene(root, 1100, 650));
                    stage.show();
                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.getButtonTypes().remove(ButtonType.OK);
                            alert.getButtonTypes().add(ButtonType.YES);
                            alert.setTitle("CinemaProject");
                            alert.setHeaderText("Do you wish to exit?");
                            alert.initModality(Modality.APPLICATION_MODAL);
                            alert.showAndWait();
                            if(alert.getResult() == ButtonType.CANCEL){
                                event.consume();
                            }
                        }
                    });

                }catch (IOException e){
                    e.printStackTrace();
                }
                primaryStage.close();
                adminStage.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

    }
}
