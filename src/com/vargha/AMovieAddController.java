package com.vargha;

import com.jfoenix.controls.JFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AMovieAddController implements Initializable {
    @FXML private TextField titleField;
    @FXML private TextArea descField;
    @FXML private TextField lengthField;
    @FXML private JFXButton okBut;
    @FXML private ImageView movieView;
    private Stage stage;
    private ArrayList<Movie> movies;

    public AMovieAddController(Stage stage, ArrayList<Movie> movies) {
        this.stage = stage;
        this.movies = movies;
    }

    //disables ok button, executes setNums
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        descField.setWrapText(true);
        okBut.setDisable(true);
        setNums(lengthField, true, null);
        setNums(titleField, false, null);
        setNums(null, false, descField);
    }

    //opens fileChooser to add an image for the movie
    @FXML
    private void addImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Documents"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.jpeg"));
        fileChooser.setTitle("Choose a file");
        File file = fileChooser.showOpenDialog(stage);

        if(file != null){
            movieView.setImage(new Image(file.toURI().toString()));
        }

        if(descField.getText().length() > 0 && lengthField.getText().length() > 0
                && titleField.getText().length() > 0 && movieView.getImage() != null){
            okBut.setDisable(false);
        } else {
            okBut.setDisable(true);
        }
    }

    @FXML
    private void ok(){
        movies.add(new Movie(titleField.getText(), descField.getText(),
                Integer.parseInt(lengthField.getText()), movieView.getImage(), new ArrayList<>()));
        stage.close();
    }

    @FXML
    private void cancel(){
        stage.close();
    }

    //only allows numbers in textField (if check == true), checks for changes to enable okButton
    private void setNums(TextField textField, boolean check, TextArea textArea){
        if(textArea == null){
            textField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                                    String newValue) {
                    if(descField.getText().length() > 0 && lengthField.getText().length() > 0
                            && titleField.getText().length() > 0 && movieView.getImage() != null){
                        okBut.setDisable(false);
                    } else {
                        okBut.setDisable(true);
                    }
                    if(check){
                        if (!newValue.matches("\\d*")) {
                            textField.setText(newValue.replaceAll("[^\\d]", ""));
                        }
                    }
                }
            });
        } else {
            textArea.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue,
                                    String newValue) {
                    if(descField.getText().length() > 0 && lengthField.getText().length() > 0
                            && titleField.getText().length() > 0 && movieView.getImage() != null){
                        okBut.setDisable(false);
                    } else {
                        okBut.setDisable(true);
                    }
                }
            });
        }

    }
}
