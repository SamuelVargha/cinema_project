package com.vargha;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class AddShowingController implements Initializable {

    @FXML private JFXTextField dayField;
    @FXML private JFXTextField monthField;
    @FXML private JFXTextField yearField;
    @FXML private JFXTextField hourField;
    @FXML private JFXTextField minutesField;
    @FXML private ComboBox<String> screenBox;
    @FXML private JFXButton okBut;
    private Movie movie;
    private ObservableList<Showing> observShowings;
    private Stage stage;

    public AddShowingController(Movie movie, ObservableList<Showing> observShowings, Stage stage) {
        this.movie = movie;
        this.observShowings = observShowings;
        this.stage = stage;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!stage.getModality().equals(Modality.APPLICATION_MODAL)){
            stage.initModality(Modality.APPLICATION_MODAL);
        }
        screenBox.getItems().add("screen1");
        screenBox.getItems().add("screen2");
        screenBox.getSelectionModel().select(0);
        setNums(dayField);
        setNums(minutesField);
        setNums(hourField);
        setNums(yearField);
        setNums(monthField);
        okBut.setDisable(true);

    }

    //adds showing to the movie and observShowings, closes stage
    @FXML
    private void ok(){
        String day = addZero(dayField);
        String month = addZero(monthField);
        String hour = addZero(hourField);
        String minutes = addZero(minutesField);
        if (Integer.parseInt(day) > 32 || Integer.parseInt(month) > 12 || Integer.parseInt(hour) > 24
                || Integer.parseInt(minutes) > 60 || yearField.getText().length() != 4){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setHeaderText("Incorrect format");
            a.initModality(Modality.APPLICATION_MODAL);
            a.showAndWait();
            return;
        }

        Showing showing = new Showing( hour + ":" + minutes + "/" + day + "/" +
                month + "/" + yearField.getText(),
                screenBox.getSelectionModel().getSelectedItem(), 50, 50);
        showing.setMovie(movie);
        observShowings.add(showing);
        movie.getShowings().add(showing);
        stage.close();
    }

    //enables ok button, only if all fields are full
    @FXML
    private void type1(){
        if(minutesField.getText().length() > 0 && hourField.getText().length() > 0 &&
                yearField.getText().length() > 0 && monthField.getText().length() > 0 &&
                dayField.getText().length() > 0){
            okBut.setDisable(false);
        }
    }

    //only allows numbers in textfields
    private void setNums(JFXTextField textField){
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    //adds a zero in front of a number if it's of length 1
    private String addZero(JFXTextField textField){
        String x = textField.getText();
        if (x.length() < 2){
            x = "0" + textField.getText();
        }
        return x;
    }

}
