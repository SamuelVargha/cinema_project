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
import java.util.ResourceBundle;

public class AOrdersController implements Initializable {
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
    private AMoviesController aMoviesController;
    private AHomeController aHomeController;
    private AShowingsController aShowingsController;
    private ObservableList<Showing> observShowings;
    private ArrayList<User> users;


    AOrdersController(Stage currentStage, ObservableList<Showing> obesrvShowings, Controller controller,
                             ArrayList<Movie> movies, AMoviesController aMoviesController,
                             AHomeController aHomeController, AShowingsController aShowingsController, User user) {
        this.currentStage = currentStage;
        this.controller = controller;
        this.movies = movies;
        this.aHomeController = aHomeController;
        this.aMoviesController = aMoviesController;
        this.aShowingsController = aShowingsController;
        this.observShowings = obesrvShowings;
        this.user = user;
        this.users = aHomeController.getUsers();
    }

    //formats dateTimes for showings, sets table items
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        homeBut.setFocusTraversable(false);
        moviesBut.setFocusTraversable(false);
        backBut.setFocusTraversable(false);
        showingsBut.setFocusTraversable(false);
        ordersBut.setFocusTraversable(true);
        ObservableList<Order> orders = FXCollections.observableArrayList();
        table.setPlaceholder(new Label(""));

        for(User u : users){
            orders.addAll(u.getOrders());
        }
        format(timeCol);
        format(showingCol);

        table.setItems(orders);
        table.getSortOrder().add(timeCol);
    }

    //loads Controller
    @FXML
    private void selBack () throws IOException {
        controller.setUsers(users);
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

    //removes selected order from users' orders, frees taken seats from showing
    @FXML
    private void cancel(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.setTitle("CinemaProject");
        alert.setHeaderText("Are you sure?");
        alert.initModality(Modality.APPLICATION_MODAL);
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
