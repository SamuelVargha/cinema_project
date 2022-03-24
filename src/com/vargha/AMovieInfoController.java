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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class AMovieInfoController implements Initializable {

    @FXML private JFXTextArea text1;
    @FXML private JFXTextArea text2;
    @FXML private JFXTextArea text3;
    @FXML private Pane pane1;
    @FXML private Pane pane2;
    @FXML private Pane pane3;
    @FXML private ImageView movieView;
    @FXML private TableView<Showing> showingsTable;
    @FXML private TableColumn<Showing, LocalDateTime> showingsCol;
    private ObservableList<Showing> observShowings;
    private User user;
    private Movie movie;
    private ArrayList<Movie> movies;
    private Stage stage;
    private Stage stageAdd;
    private TicketControllerOne ticketControllerOne;
    private TicketControllerTwo ticketControllerTwo;
    private ArrayList<Showing> toRemove = new ArrayList<>();
    private Showing showing;


    public AMovieInfoController(User user, Movie movie, Stage stage, ObservableList<Showing> observShowings,
                                ArrayList<Movie> movies, Stage stageAdd ,Showing showing) {
        this.movie = movie;
        this.stage = stage;
        this.user = user;
        this.observShowings = observShowings;
        this.movies = movies;
        this.stageAdd = stageAdd;
        this.showing = showing;
    }

    //sets up all text fields and table from movie
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!stage.getModality().equals(Modality.APPLICATION_MODAL)){
            stage.initModality(Modality.APPLICATION_MODAL);
        }
        movieView.setImage(movie.getImage());
        text1.setText(movie.getName());
        text1.setStyle("-fx-text-fill: white;");
        text1.setWrapText(true);
        text2.setText(movie.getDescription());
        text2.setStyle("-fx-text-fill: white;");
        text2.setWrapText(true);
        text3.setText(movie.getLength() + " minutes");
        text3.setStyle("-fx-text-fill: white;");
        showingsTable.setPlaceholder(new Label(""));

        if(showing != null){
            showingsTable.getSelectionModel().select(showing);
        }else {
            try {
                showingsTable.getSelectionModel().select(movie.getShowings().get(0));
            } catch (IndexOutOfBoundsException e){
                System.out.println();
            }

        }

        ObservableList<Showing> s = FXCollections.observableArrayList(movie.getShowings());
        showingsCol.setStyle("-fx-alignment: CENTER;");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        showingsCol.setCellFactory(tc -> new TableCell<Showing, LocalDateTime>() {
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
        showingsTable.setItems(s);
    }

    //loads either ticketControllerOne or Two depending on which screen is selected
    @FXML
    void issue(){
        if(showingsTable.getSelectionModel().getSelectedItem() == null){
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("Please select a showing");
            a.setTitle("CinemaProject");
            a.initModality(Modality.APPLICATION_MODAL);
            a.showAndWait();
            return;
        }
        Showing showing = new Showing("00/00/0000","00/00/0000",0,0);
        for (Showing s : movie.getShowings()){
            if(s.getLocalDateTime().equals(showingsTable.getSelectionModel().getSelectedItem().getLocalDateTime())){
                showing = s;
            }
        }
        if(showing.getScreen().equals("screen1")){
            ticketControllerOne = new TicketControllerOne(user, movie, showing, stage);
            FXMLLoader newLoader = new FXMLLoader(getClass().getResource("ticketOneUI.fxml"));
            newLoader.setControllerFactory(t -> ticketControllerOne);
            try {
                stage.setTitle("CinemaProject");
                stage.setResizable(false);
                Parent root = newLoader.load();
                stage.setScene(new Scene(root, 760, 550));
                stage.show();

            }catch (IOException e){
                e.printStackTrace();
            }
        } else if(showing.getScreen().equals("screen2")){
            ticketControllerTwo = new TicketControllerTwo(user, movie, showing, stage);
            FXMLLoader newLoader = new FXMLLoader(getClass().getResource("ticketTwoUI.fxml"));
            newLoader.setControllerFactory(t -> ticketControllerTwo);
            try {
                stage.setTitle("CinemaProject");
                stage.setResizable(false);
                Parent root = newLoader.load();
                stage.setScene(new Scene(root, 760, 550));
                stage.show();

            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //removes showing from table, adds to toRemove list

    @FXML
    private void remove(){
        toRemove.add(showingsTable.getSelectionModel().getSelectedItem());
        showingsTable.getItems().remove(showingsTable.getSelectionModel().getSelectedItem());
    }

    //loads AddShowingController, updates showingsTable
    @FXML
    private void add(){
        ObservableList<Showing> a = FXCollections.observableArrayList(new ArrayList<>());
        a.addAll(showingsTable.getItems());
        ArrayList<Showing> a2 = new ArrayList<>();
        a2.addAll(movie.getShowings());
        AddShowingController addShowingController = new AddShowingController(movie, observShowings ,stageAdd);
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("addShowingUI.fxml"));
        newLoader.setControllerFactory(t -> addShowingController);
        try {
            stageAdd.setTitle("CinemaProject");
            stageAdd.setResizable(false);
            Parent root = newLoader.load();
            stageAdd.setScene(new Scene(root, 350, 230));
            stageAdd.showAndWait();

        }catch (IOException e){
            e.printStackTrace();
        }
        for(Showing s : a){
            if(a2.contains(s)){
                a2.remove(s);
            }
        }
        showingsTable.setItems(FXCollections.observableArrayList(movie.getShowings()));
        for(Showing s : a2){
            showingsTable.getItems().remove(s);
        }
    }

    //removes movie from movies, removes all containing showings from observShowings
    @FXML
    private void removeMovie(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.getButtonTypes().remove(ButtonType.OK);
        alert.getButtonTypes().add(ButtonType.YES);
        alert.setTitle("CinemaProject");
        alert.setHeaderText("Are you sure?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.CANCEL){
            return;
        }
        observShowings.removeAll(movie.getShowings());
        movies.remove(movie);
        stage.close();
    }

    //removes toRemove showings form all lists, puts backgrounds back to purple
    @FXML
    private void save(){
        for(Showing s : toRemove){
            observShowings.remove(s);
            movie.getShowings().remove(s);
        }

        movie.setName(text1.getText());
        movie.setDescription(text2.getText());
        String[] l = text3.getText().split(" ");
        movie.setLength(Integer.parseInt(l[0]));

        text1.setEditable(true);
        text1.setStyle("-fx-text-fill: white ;");
        pane1.setStyle("-fx-background-color: #6c5ce7;");

        text2.setEditable(true);
        text2.setStyle("-fx-text-fill: white ;");
        pane2.setStyle("-fx-background-color: #6c5ce7;");

        text3.setEditable(true);
        text3.setStyle("-fx-text-fill: white ;");
        pane3.setStyle("-fx-background-color: #6c5ce7;");
    }

    //enables editing of text1, puts text1 background to white
    @FXML
    private void edit1(){
        text1.setEditable(true);
        text1.setStyle("-fx-text-fill: black ;");
        pane1.setStyle("-fx-background-color: white;");
    }
    //enables editing of text2, puts text2 background to white
    @FXML
    private void edit2(){
        text2.setEditable(true);
        text2.setStyle("-fx-text-fill: black ;");
        pane2.setStyle("-fx-background-color: white;");

    }
    //enables editing of text3, puts text3 background to white
    @FXML
    private void edit3(){
        text3.setEditable(true);
        text3.setStyle("-fx-text-fill: black ;");
        pane3.setStyle("-fx-background-color: white;");
    }

    void setShowing(Showing showing) {
        this.showing = showing;
    }
}
