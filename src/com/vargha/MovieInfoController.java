package com.vargha;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class MovieInfoController implements Initializable {

    @FXML private Text text1;
    @FXML private JFXTextArea text2;
    @FXML private Text text3;
    @FXML private ImageView movieView;
    @FXML private ComboBox<String> showingsBox;
    @FXML private JFXButton reserveButton;
    private User user;
    private Movie movie;
    private Stage stage;
    private Showing showing;
    private TicketControllerOne ticketControllerOne;
    private TicketControllerTwo ticketControllerTwo;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public MovieInfoController(User user, Movie movie, Stage stage, Showing showing) {
        if(!stage.getModality().equals(Modality.APPLICATION_MODAL)){
            stage.initModality(Modality.APPLICATION_MODAL);
        }
        this.movie = movie;
        this.stage = stage;
        this.showing = showing;
        this.user = user;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        movieView.setImage(movie.getImage());
        text1.setText(movie.getName());
        text2.setText(movie.getDescription());
        text2.setStyle("-fx-text-fill: white;");
        text2.setWrapText(true);
        text3.setText(movie.getLength() + " minutes");
        if(movie.getShowings().size() < 1){
            reserveButton.setDisable(true);
        }
        ArrayList<LocalDateTime> dateTimes = new ArrayList<>();
        for(Showing showing : movie.getShowings()){
            dateTimes.add(showing.getLocalDateTime());
        }
        Collections.sort(dateTimes);
        for(LocalDateTime lt: dateTimes){
            showingsBox.getItems().add(lt.format(formatter));
        }
        showingsBox.getSelectionModel().select(0);

        if(showing != null){
            showingsBox.getSelectionModel().select(showing.getLocalDateTime().format(formatter));
        }
    }

    //loads either ticketControllerOne or Two depending on which screen is selected
    @FXML
     void issue(){
        Showing showing = new Showing("00/00/0000","00/00/0000",0,0);
        for (Showing s : movie.getShowings()){
            if(s.getLocalDateTime().format(formatter).equals(showingsBox.getSelectionModel().getSelectedItem())){
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

}
