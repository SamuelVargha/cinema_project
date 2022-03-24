package com.vargha;

import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AShowingsController implements Initializable {
    @FXML private JFXButton homeBut;
    @FXML private JFXButton moviesBut;
    @FXML private JFXButton backBut;
    @FXML private JFXButton showingsBut;
    @FXML private JFXButton ordersBut;
    @FXML private TableView<Showing> table;
    @FXML private TableColumn dateCol;
    @FXML private TableColumn<Showing, LocalDateTime> tempCol;
    private ObservableList<Showing> observShowings;
    private Stage currentStage;
    private Controller controller;
    private ArrayList<Movie> movies;
    private AMoviesController aMoviesController;
    private AHomeController aHomeController;
    private AMovieInfoController aInfoController;
    private AOrdersController aOrdersController;
    private Stage stage = new Stage();
    private Movie movie;


    AShowingsController(Stage currentStage, Controller controller,
                              ObservableList<Showing> observShowings, ArrayList<Movie> movies,
                              AMoviesController aMoviesController, AHomeController aHomeController,
                              AMovieInfoController aInfoController, AOrdersController aOrdersController) {
        this.currentStage = currentStage;
        this.controller = controller;
        this.observShowings = observShowings;
        this.movies = movies;
        this.aHomeController = aHomeController;
        this.aMoviesController = aMoviesController;
        this.aInfoController = aInfoController;
        this.aOrdersController = aOrdersController;
    }

    //sets up table, formats dateTime
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stage.getIcons().add(new Image("file:icon.png"));
        if(!stage.getModality().equals(Modality.APPLICATION_MODAL)){
            stage.initModality(Modality.APPLICATION_MODAL);
        }
        homeBut.setFocusTraversable(false);
        moviesBut.setFocusTraversable(false);
        backBut.setFocusTraversable(false);
        showingsBut.setFocusTraversable(true);
        ordersBut.setFocusTraversable(false);
        table.setPlaceholder(new Label(""));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateCol.setCellFactory(tc -> new TableCell<TableView, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(formatter.format(date));
                }
            }
        });
        table.setItems(observShowings);
        table.getSortOrder().add(tempCol);
    }

    //loads Controller
    @FXML
    private void selBack () throws IOException {
        controller.setUsers(aHomeController.getUsers());
        controller.setMovies(movies);
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


    //loads aInfoController
    @FXML
    private void reserve(){
        if(table.getSelectionModel().getSelectedItem() == null){
            return;
        }
        this.movie = table.getSelectionModel().getSelectedItem().getMovie();
        aInfoController = new AMovieInfoController(aHomeController.getUser(), movie, stage, observShowings, movies, new Stage(), table.getSelectionModel().getSelectedItem());
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("aMovieInfoUI.fxml"));
        newLoader.setControllerFactory(t -> aInfoController);
        try {
            stage.setTitle("CinemaProject");
            stage.setResizable(false);
            Parent root = newLoader.load();
            stage.setScene(new Scene(root, 830, 570));
            stage.showAndWait();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //loads aMoviesController
    @FXML
    private void selMovies(){
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("aMoviesUI.fxml"));
        newLoader.setControllerFactory(t -> aMoviesController);
        try {
            currentStage.setTitle("CinemaProject");
            Parent root = newLoader.load();
            currentStage.setScene(new Scene(root, 1100, 650));
            currentStage.show();

        }catch (IOException e){
            e.printStackTrace();
        }
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

    //removes selected showing from observShowings and movies' showings
    @FXML
    private void remove(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.setTitle("CinemaProject");
        alert.setHeaderText("Are you sure?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.CANCEL){
            return;
        }
        table.getSelectionModel().getSelectedItem().getMovie().getShowings().remove(table.getSelectionModel().getSelectedItem());
        observShowings.remove(table.getSelectionModel().getSelectedItem());
    }

    public void setAOrdersController(AOrdersController aOrdersController) {
        this.aOrdersController = aOrdersController;
    }
}
