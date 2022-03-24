package com.vargha;

import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AMoviesController implements Initializable {
    private Stage currentStage;
    private Controller controller;
    @FXML private HBox movieBox;
    @FXML private HBox movieBox2;
    @FXML private HBox movieBox3;
    @FXML private HBox movieBox4;
    @FXML private HBox movieBox5;
    @FXML private JFXButton homeBut;
    @FXML private JFXButton backBut;
    @FXML private JFXButton moviesBut;
    @FXML private JFXButton showingsBut;
    @FXML private JFXButton ordersBut;
    private ArrayList<Movie> movies;
    private AMovieInfoController aInfoController;
    private AShowingsController aShowingsController;
    private AOrdersController aOrdersController;
    private AHomeController aHomeController;
    private ObservableList<Showing> observShowings;
    private Stage stage = new Stage();
    private User user;


    AMoviesController(Stage currentStage, Controller controller, ArrayList<Movie> movies,
                     ObservableList<Showing> observShowings, AShowingsController aShowingsController,
                     AHomeController aHomeController, AMovieInfoController aMovieInfoController, AOrdersController aOrdersController) {
        this.currentStage = currentStage;
        this.controller = controller;
        this.movies = movies;
        this.observShowings = observShowings;
        this.aShowingsController = aShowingsController;
        this.aHomeController = aHomeController;
        this.aInfoController = aMovieInfoController;
        this.aOrdersController = aOrdersController;
        user = new User(aHomeController.getAdmin().getID(), aHomeController.getAdmin().getOrders());
    }

    //executes selMovies
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stage.getIcons().add(new Image("file:icon.png"));
        if(!stage.getModality().equals(Modality.APPLICATION_MODAL)){
            stage.initModality(Modality.APPLICATION_MODAL);
        }
        homeBut.setFocusTraversable(false);
        moviesBut.setFocusTraversable(true);
        backBut.setFocusTraversable(false);
        showingsBut.setFocusTraversable(false);
        ordersBut.setFocusTraversable(false);
        if(movies.size() > 0){
            selMovies();
        }
    }

    //loads Controller
    @FXML
    private void selBack () throws IOException {
        controller.updateShowings(observShowings);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.setTitle("CinemaProject");
        alert.setHeaderText("Do you wish to go back?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.CANCEL){
            return;
        }
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("users.fxml"));
        loader.setControllerFactory(t -> controller);
        controller.setPrimaryStage(primaryStage);
        Parent root = loader.load();
        primaryStage.setTitle("CinemaProject");
        primaryStage.setScene(new Scene(root, 420, 270));
        primaryStage.setResizable(false);
        primaryStage.show();
        currentStage.close();
    }

    //adds all movies form 'movies' to a ScrollPane
    @FXML
    private void selMovies(){
        movieBox.getChildren().clear();
        movieBox2.getChildren().clear();
        movieBox3.getChildren().clear();
        movieBox4.getChildren().clear();
        movieBox5.getChildren().clear();
        if(movies.size() < 1){
            return;
        }

        for(int i = 0; i < 4; i++){
            Movie movie = movies.get(i);
            addButton(movie, movieBox);
            if(i + 1 == movies.size()){
                return;
            }
        }

        for(int i = 4; i < 8; i++){
            Movie movie = movies.get(i);
            addButton(movie, movieBox2);
            if(i + 1 == movies.size()){
                return;
            }
        }

        for(int i = 8; i < 12; i++){
            Movie movie = movies.get(i);
            addButton(movie, movieBox3);
            if(i + 1 == movies.size()){
                return;
            }
        }

        for(int i = 12; i < 16; i++){
            Movie movie = movies.get(i);
            addButton(movie, movieBox4);
            if(i + 1 == movies.size()){
                return;
            }
        }
    }

    //loads aHomeController
    @FXML
    private void selHome(){
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("aHomeUI.fxml"));
        newLoader.setControllerFactory(t -> aHomeController);
        try {
            currentStage.setTitle("CinemaProject");
            Parent root = newLoader.load();
            currentStage.setScene(new Scene(root, 1100, 650));
            currentStage.show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //loads aShowingsController
    @FXML
    private void selShowings(){
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("aShowingsUI.fxml"));
        newLoader.setControllerFactory(t -> aShowingsController);
        try {
            currentStage.setTitle("CinemaProject");
            Parent root = newLoader.load();
            currentStage.setScene(new Scene(root, 1100, 650));
            currentStage.show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }


    //sets up a functioning button for passed movie and HBox
    private void addButton(Movie movie, HBox movieBox){
        Stage stageAdd = new Stage();
        ImageView imageView = new ImageView();
        imageView.setImage(movie.getImage());
        imageView.setFitWidth(180);
        imageView.setFitHeight(250);
        JFXButton but = new JFXButton();
        but.setGraphic(imageView);
        but.setPrefSize(180, 250);
        but.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                aInfoController = new AMovieInfoController(user, movie, stage, observShowings, movies, stageAdd, null);
                FXMLLoader newLoader = new FXMLLoader(getClass().getResource("aMovieInfoUI.fxml"));
                newLoader.setControllerFactory(t -> aInfoController);
                try {
                    stage.setTitle("CinemaProject");
                    stage.setResizable(false);
                    Parent root = newLoader.load();
                    stage.setScene(new Scene(root, 830, 570));
                    stage.showAndWait();
                    selMovies();

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        movieBox.getChildren().add(but);
    }

    //loads aOrdersController
    @FXML
    private void selOrders(){
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("aOrdersUI.fxml"));
        newLoader.setControllerFactory(t -> aOrdersController);
        try {
            currentStage.setTitle("CinemaProject");
            Parent root = newLoader.load();
            currentStage.setScene(new Scene(root, 1100, 650));
            currentStage.show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //loads aMovieAddController, updates scrollPane
    @FXML
    private void add(){
        AMovieAddController aMovieAddController = new AMovieAddController(stage, movies);
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("aMovieAddUI.fxml"));
        newLoader.setControllerFactory(t -> aMovieAddController);
        try {
            stage.setTitle("CinemaProject");
            Parent root = newLoader.load();
            stage.setScene(new Scene(root, 690, 350));
            stage.showAndWait();

        }catch (IOException e){
            e.printStackTrace();
        }
        selMovies();
    }

    void setAShowingsController(AShowingsController aShowingsController) {
        this.aShowingsController = aShowingsController;
    }

    void setInfoController(AMovieInfoController aInfoController) {
        this.aInfoController = aInfoController;
    }

    void setOrdersController(AOrdersController aOrdersController){
        this.aOrdersController = aOrdersController;
    }
}
