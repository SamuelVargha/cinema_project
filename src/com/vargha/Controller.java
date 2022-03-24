package com.vargha;

import com.jfoenix.controls.JFXButton;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML private JFXButton userButton;
    @FXML private JFXButton adminButton;
    private Stage stage = new Stage();
    private Stage adminStage = new Stage();
    private Stage primaryStage;
    private ObservableList<Showing> observShowings = FXCollections.observableArrayList();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Movie> movies = new ArrayList<>();
    private HomeController homeController;
    private AHomeController aHomeController;
    private AdminLoginController adminLoginController;
    private boolean check;

    Controller(Stage primaryStage) {
        this.primaryStage = primaryStage;
        check = true;
    }

    //sets up four initial movies,
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(check){
            Image image = new Image("file:interstellar.jpg");
            Showing showing = new Showing("10:40/10/12/2015", "screen1", 40, 40);
            Showing showing1 = new Showing("15:55/10/12/2015", "screen2", 50, 50);
            Showing showing2 = new Showing("19:55/11/12/2015", "screen2", 50, 50);
            ArrayList<Showing> showings = new ArrayList<>();
            showings.add(showing);
            showings.add(showing1);
            showings.add(showing2);
            observShowings.add(showing);
            observShowings.add(showing1);
            observShowings.add(showing2);
            Movie movie = new Movie("Interstellar", "Earth's future has been riddled by disasters, famines, and droughts. " +
                    "There is only one way to ensure mankind's survival: " +
                    "Interstellar travel. A newly discovered wormhole in the far reaches of our solar s" +
                    "ystem allows a team of astronauts to go where no man has gone before", 150, image, showings);
            movies.add(movie);


            Image imageTwo = new Image("file:IM2.jpg");
            Showing showingTwo = new Showing("15:40/10/02/2016", "screen1", 40, 40);
            showing1 = new Showing("15:20/10/12/2015", "screen1", 40, 40);
            showing2 = new Showing("18:55/11/12/2015", "screen2", 50, 50);
            ArrayList<Showing> showingsTwo = new ArrayList<>();
            showingsTwo.add(showingTwo);
            showingsTwo.add(showing1);
            showingsTwo.add(showing2);
            observShowings.add(showingTwo);
            observShowings.add(showing1);
            observShowings.add(showing2);
            Movie movieTwo = new Movie("Iron Man 2", "With the world now aware of his" +
                    " dual life as the armored superhero Iron Man, billionaire inventor Tony Stark faces pressure " +
                    "from the government, the press, and the public to share his technology " +
                    "with the military.", 120, imageTwo, showingsTwo);
            movies.add(movieTwo);

            Image imageThree = new Image("file:matrix.jpg");
            Showing showingThree = new Showing("18:25/11/02/2016", "screen1", 50, 50);
            ArrayList<Showing> showingsThree = new ArrayList<>();
            showingsThree.add(showingThree);
            observShowings.add(showingThree);
            Movie movieThree = new Movie("Matrix Reloaded", "A computer hacker learns from mysterious rebels " +
                    "about the true nature of his reality and his role in the war against its " +
                    "controllers.", 110, imageThree, showingsThree);
            movies.add(movieThree);

            Image imageFour = new Image("file:alex.jpg");
            Showing showingFour = new Showing("15:45/10/03/2015", "screen2", 50, 50);
            ArrayList<Showing> showingsFour = new ArrayList<>();
            showingsFour.add(showingFour);
            observShowings.add(showingFour);
            Movie movieFour = new Movie("Who killed Captain Alex", "Uganda's president gives Captain " +
                    "Alex the mission to defeat the Tiger Maffia, but Alex gets killed in the process. Upon hearing " +
                    "the tragic news, his brother investigates to avenge Alex, hence the title Who Killed " +
                    "Captain Alex.", 50, imageFour, showingsFour);
            movies.add(movieFour);
            check = false;
        }

        if(!stage.getModality().equals(Modality.APPLICATION_MODAL) && !adminStage.getModality().equals(Modality.APPLICATION_MODAL)){
            stage.initModality(Modality.APPLICATION_MODAL);
            adminStage.initModality(Modality.APPLICATION_MODAL);
        }
        userButton.setFocusTraversable(false);
        adminButton.setFocusTraversable(false);
        homeController = new HomeController(stage, this, observShowings, movies);
        aHomeController = new AHomeController(null, stage, this, observShowings, movies, users);
        adminLoginController = new AdminLoginController( primaryStage, aHomeController, stage, adminStage);
    }

    //loads homeController
    @FXML
    private void user(){
        if(movies.size() < 1){
            return;
        }
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("homeUI.fxml"));
        newLoader.setControllerFactory(t -> homeController);
        try {
            stage.setTitle("CinemaProject");
            Parent root = newLoader.load();
            stage.setScene(new Scene(root, 1100, 650));
            stage.getIcons().add(new Image("file:icon.png"));
            stage.setResizable(false);
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
            primaryStage.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //loads adminLoginController
    @FXML
    private void admin(){
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("adminLoginUI.fxml"));
        newLoader.setControllerFactory(t -> adminLoginController);
        try {
            adminStage.setTitle("CinemaProject");
            Parent root = newLoader.load();
            adminStage.setScene(new Scene(root, 290, 250));
            adminStage.getIcons().add(new Image("file:icon.png"));
            adminStage.setResizable(false);
            adminStage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    void setClose(Stage stage){
        primaryStage = stage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    void addUser(User user) {
        this.users.add(user);
    }

    void updateShowings(ObservableList<Showing> showings) {
        int i = 0;
        for(Showing s : showings){
            if(s.getTakenSeats().contains("x") ){
                for(int e = 0; e < s.getTakenSeats().size(); e++){
                    if(s.getTakenSeats().get(e).equals("x")){
                        observShowings.get(i).takeSeat("x", e);
                    }
                }
            }
            i++;
        }
    }
}
