package com.vargha;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class OrdersController implements Initializable {
    @FXML private JFXButton homeBut;
    @FXML private JFXButton moviesBut;
    @FXML private JFXButton backBut;
    @FXML private JFXButton showingsBut;
    @FXML private JFXButton ordersBut;
    @FXML private TableView<Order> table;
    @FXML private TableColumn<Order, LocalDateTime> timeCol;
    @FXML private TableColumn<Order, LocalDateTime> showingCol;
    private User user;
    private Stage currentStage;
    private Controller controller;
    private ArrayList<Movie> movies;
    private MoviesController moviesController;
    private HomeController homeController;
    private MovieInfoController infoController;
    private ShowingsController showingsController;
    private ObservableList<Showing> observShowings;


    OrdersController(Stage currentStage, ObservableList<Showing> obesrvShowings, Controller controller, ArrayList<Movie> movies,
                            MoviesController moviesController, HomeController homeController,
                            MovieInfoController infoController, ShowingsController showingsController, User user) {
        this.currentStage = currentStage;
        this.controller = controller;
        this.movies = movies;
        this.homeController = homeController;
        this.moviesController = moviesController;
        this.infoController = infoController;
        this.showingsController = showingsController;
        this.observShowings = obesrvShowings;
        this.user = user;
    }

    //formats dateTimes for times and showings, sets table items
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        homeBut.setFocusTraversable(false);
        moviesBut.setFocusTraversable(false);
        backBut.setFocusTraversable(false);
        showingsBut.setFocusTraversable(false);
        ordersBut.setFocusTraversable(true);
        ObservableList<Order> orders = FXCollections.observableArrayList(homeController.getUser().getOrders());
        table.setPlaceholder(new Label(""));
        format(timeCol);
        format(showingCol);
        table.setItems(orders);
        table.getSortOrder().add(timeCol);
    }

    //loads controller
    @FXML
    private void selBack () throws IOException {
        controller.updateShowings(observShowings);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.setTitle("CinemaProject");
        alert.setHeaderText("Do you wish to go back?");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
        if(alert.getResult() == ButtonType.CANCEL){
            return;
        }
        Stage primaryStage = new Stage();
        controller.setPrimaryStage(primaryStage);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("users.fxml"));
        loader.setControllerFactory(t -> controller);
        Parent root = loader.load();
        primaryStage.setTitle("CinemaProject");
        primaryStage.setScene(new Scene(root, 420, 270));
        primaryStage.setResizable(false);
        primaryStage.show();
        currentStage.close();
    }

    //loads homeController
    @FXML
    private void selHome(){
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("homeUI.fxml"));
        newLoader.setControllerFactory(t -> homeController);
        try {
            currentStage.setTitle("CinemaProject");
            Parent root = newLoader.load();
            currentStage.setScene(new Scene(root, 1100, 650));
            currentStage.show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //loads showingsController
    @FXML
    private void selShowings(){
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("showingsUI.fxml"));
        newLoader.setControllerFactory(t -> showingsController);
        try {
            currentStage.setTitle("CinemaProject");
            Parent root = newLoader.load();
            currentStage.setScene(new Scene(root, 1100, 650));
            currentStage.show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //loads moviesController
    @FXML
    private void selMovies(){
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("moviesUI.fxml"));
        newLoader.setControllerFactory(t -> moviesController);
        try {
            currentStage.setTitle("CinemaProject");
            Parent root = newLoader.load();
            currentStage.setScene(new Scene(root, 1100, 650));
            currentStage.show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //removes selected order from users' orders, frees taken seats from showing
    @FXML
    private void cancel(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.setTitle("CinemaProject");
        alert.setHeaderText("Do you wish to proceed?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.NO){
            return;
        }

        String[] seatsArray = {"a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "b0", "b1", "b2", "b3", "b4", "b5",
                "b6", "b7", "b8", "b9", "c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "d0", "d1", "d2",
                "d3", "d4", "d5", "d6", "d7", "d8", "d9", "e0", "e1", "e2", "e3", "e4", "e5", "e6", "e7", "e8", "e9"};

        Order o = table.getSelectionModel().getSelectedItem();
        Showing s = table.getSelectionModel().getSelectedItem().getShowing();
        for(int i : o.getPositions()){
            s.getTakenSeats().set(i, seatsArray[i]);
        }
        table.getItems().remove(table.getSelectionModel().getSelectedItem());
        if (user.getOrders().size() == 1){
            user.getOrders().clear();
        } else {
            user.getOrders().remove(table.getSelectionModel().getSelectedItem());
        }
    }

    private void format(TableColumn<Order, LocalDateTime> col){
        System.out.println("e");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        col.setCellFactory(tc -> new TableCell<Order, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(formatter.format(date));
                }
            }
        });
    }
}
