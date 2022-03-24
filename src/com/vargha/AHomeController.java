package com.vargha;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.collections.FXCollections;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.ResourceBundle;

public class AHomeController implements Initializable {
    @FXML private JFXButton bigButton;
    @FXML private JFXButton reserveButton;
    @FXML private Text nameText;
    @FXML private JFXTextArea dateArea;
    @FXML private JFXTextArea descArea;
    private Admin admin;
    private User user;
    private Stage currentStage;
    private Controller controller;
    private ArrayList<Movie> movies;
    private Random random = new Random();
    private Movie movie;
    private AMovieInfoController aInfoController;
    private AMoviesController aMoviesController;
    private AShowingsController aShowingsController;
    private AOrdersController aOrdersController;
    private ObservableList<Showing> observShowings;
    private ArrayList<User> users;
    private Stage stage = new Stage();
    private boolean check = true;

    AHomeController(Admin admin, Stage currentStage, Controller controller, ObservableList<Showing> showings, ArrayList<Movie> movies, ArrayList<User> users) {
        this.currentStage = currentStage;
        this.controller = controller;
        this.observShowings = showings;
        this.movies = movies;
        this.users = users;
        Random r = new Random();

    }

    //creates necessary controllers, adds a random movie image to the button, sets up all text fields and areas
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentStage.getIcons().add(new Image("file:icon.png"));
        if(!stage.getModality().equals(Modality.APPLICATION_MODAL)){
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.getIcons().add(new Image("file:icon.png"));
        }
        if(movies.size() > 0){
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
            nameText.setText(movie.getName());
            descArea.setText(movie.getDescription());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            ArrayList<LocalDateTime> dateTimes = new ArrayList<>();
            for(Showing showing : movie.getShowings()){
                dateTimes.add(showing.getLocalDateTime());
            }
            Collections.sort(dateTimes);
            for(LocalDateTime lt: dateTimes){
                dateArea.appendText(lt.format(formatter) + "\n");
            }
        } else {
            bigButton.setDisable(true);
            reserveButton.setDisable(true);
            nameText.setText("");
        }

        this.user = new User(admin.getID(), admin.getOrders());
        aInfoController = new AMovieInfoController(user, null, stage, observShowings, movies, new Stage(), null);

        aMoviesController = new AMoviesController(currentStage, controller, movies, observShowings, null,
                this, null, null);

        aShowingsController = new AShowingsController(currentStage, controller, observShowings, movies, aMoviesController,
                this, aInfoController, null);

        aOrdersController = new AOrdersController(currentStage, observShowings, controller, movies, aMoviesController,
                this, aShowingsController, user);
        aMoviesController.setAShowingsController(aShowingsController);
        aMoviesController.setInfoController(aInfoController);
        aMoviesController.setOrdersController(aOrdersController);
        aShowingsController.setAOrdersController(aOrdersController);
        if(check){
            users.add(user);
            check = false;
        }
    }

    //loads back into Controller
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
        primaryStage.getIcons().add(new Image("file:icon.png"));
        primaryStage.show();
        currentStage.close();
    }

    //loads aInfoController
    @FXML
    private void reserve(){
        Stage s = new Stage();
        aInfoController = new AMovieInfoController(user, movie, stage, observShowings, movies, s, null);
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("aMovieInfoUI.fxml"));
        newLoader.setControllerFactory(t -> aInfoController);
        try {
            stage.setTitle("CinemaProject");
            stage.setResizable(false);
            Parent root = newLoader.load();
            stage.setScene(new Scene(root, 830, 570));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    s.close();
                }
            });
            stage.showAndWait();

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

    public ObservableList<Showing> getObservShowings() {
        return observShowings;
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

    void setAdmin(Admin admin) {
        this.admin = admin;
    }

    Admin getAdmin() {
        return admin;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setObservShowings(ObservableList<Showing> observShowings) {
        this.observShowings = observShowings;
    }
}
