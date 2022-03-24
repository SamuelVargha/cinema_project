package com.vargha;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML private JFXButton bigButton;
    @FXML private Text nameText;
    @FXML private JFXTextArea dateArea;
    @FXML private JFXTextArea descArea;
    private User user;
    private Stage currentStage;
    private Controller controller;
    private ArrayList<Movie> movies;
    private Random random = new Random();
    private Movie movie;
    private ShowingsController showingsController;
    private MovieInfoController infoController;
    private MoviesController moviesController;
    private OrdersController ordersController;
    private ObservableList<Showing> observShowings;
    private Stage stage = new Stage();

    HomeController(Stage currentStage, Controller controller, ObservableList<Showing> showings, ArrayList<Movie> movies) {
        if(!stage.getModality().equals(Modality.APPLICATION_MODAL)){
            stage.initModality(Modality.APPLICATION_MODAL);
        }
        this.currentStage = currentStage;
        this.controller = controller;
        this.observShowings = showings;
        this.movies = movies;
        Random r = new Random();

        user = new User(r.nextInt(999), new ArrayList<>());
        controller.addUser(user);

        infoController = new MovieInfoController(user, null, stage, null);
        moviesController = new MoviesController(currentStage, controller, movies, observShowings, null,
                this, null, null);

        showingsController = new ShowingsController(currentStage, controller, observShowings, movies,
                moviesController, this, infoController, null);
        moviesController.setShowingsController(showingsController);

        ordersController = new OrdersController(currentStage, observShowings, controller, movies,
                moviesController, this, infoController, showingsController, user);
        moviesController.setOrdersController(ordersController);
        moviesController.setShowingsController(showingsController);
        moviesController.setInfoController(infoController);
        showingsController.setOrdersController(ordersController);

    }

    //formats dateTimes, adds a random movie image to the button, sets up all text fields and areas
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stage.getIcons().add(new Image("file:icon.png"));
        movie = movies.get(random.nextInt(movies.size()));
        ImageView imageView = new ImageView();
        imageView.setFitWidth(336);
        imageView.setFitHeight(504);
        imageView.setImage(movie.getImage());
        bigButton.setGraphic(imageView);
        if(movie.getName().length() > 25){
            nameText.setFont(Font.font("System", FontWeight.BOLD, 34));
            if(movie.getName().length() > 35){
                nameText.setFont(Font.font("System", FontWeight.BOLD, 25));
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        nameText.setText(movie.getName());
        descArea.setText(movie.getDescription());
        ArrayList<LocalDateTime> dateTimes = new ArrayList<>();
        for(Showing showing : movie.getShowings()){
            dateTimes.add(showing.getLocalDateTime());
        }
        Collections.sort(dateTimes);
        for(LocalDateTime lt: dateTimes){
            dateArea.appendText(lt.format(formatter) + "\n");
        }
    }

    //loads controller
    @FXML
    private void selBack () throws IOException {
        controller.updateShowings(observShowings);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.setTitle("CinemaProject");
        alert.setHeaderText("Do you wish to exit?");
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

    //loads infoController
    @FXML
    private void reserve(){
        infoController = new MovieInfoController(user, movie, stage, null);
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("movieInfoUI.fxml"));
        newLoader.setControllerFactory(t -> infoController);
        try {
            stage.setTitle("CinemaProject");
            stage.setResizable(false);
            Parent root = newLoader.load();
            stage.setScene(new Scene(root, 600, 400));
            stage.showAndWait();

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

    public ObservableList<Showing> getObservShowings() {
        return observShowings;
    }

    //loads ordersController
    @FXML
    private void selOrders(){
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("ordersUI.fxml"));
        newLoader.setControllerFactory(t -> ordersController);
        try {
            currentStage.setTitle("CinemaProject");
            Parent root = newLoader.load();
            currentStage.setScene(new Scene(root, 1100, 650));
            currentStage.show();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public void setObservShowings(ObservableList<Showing> observShowings) {
        this.observShowings = observShowings;
    }
}
