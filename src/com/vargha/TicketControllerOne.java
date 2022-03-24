package com.vargha;

import com.jfoenix.controls.JFXButton;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class TicketControllerOne implements Initializable {
    @FXML private JFXButton a0;
    @FXML private JFXButton a1;
    @FXML private JFXButton a2;
    @FXML private JFXButton a3;
    @FXML private JFXButton a4;
    @FXML private JFXButton a5;
    @FXML private JFXButton a6;
    @FXML private JFXButton a7;
    @FXML private JFXButton a8;
    @FXML private JFXButton a9;

    @FXML private JFXButton b0;
    @FXML private JFXButton b1;
    @FXML private JFXButton b2;
    @FXML private JFXButton b3;
    @FXML private JFXButton b4;
    @FXML private JFXButton b5;
    @FXML private JFXButton b6;
    @FXML private JFXButton b7;
    @FXML private JFXButton b8;
    @FXML private JFXButton b9;

    @FXML private JFXButton c0;
    @FXML private JFXButton c1;
    @FXML private JFXButton c2;
    @FXML private JFXButton c3;
    @FXML private JFXButton c4;
    @FXML private JFXButton c5;
    @FXML private JFXButton c6;
    @FXML private JFXButton c7;
    @FXML private JFXButton c8;
    @FXML private JFXButton c9;

    @FXML private JFXButton d0;
    @FXML private JFXButton d1;
    @FXML private JFXButton d2;
    @FXML private JFXButton d3;
    @FXML private JFXButton d4;
    @FXML private JFXButton d5;
    @FXML private JFXButton d6;
    @FXML private JFXButton d7;
    @FXML private JFXButton d8;
    @FXML private JFXButton d9;


    @FXML private ImageView movieView;
    @FXML private Text movieText;
    @FXML private Text price;
    @FXML private Text chosenSeats;
    @FXML private Text dateText;
    @FXML private ImageView set1;
    @FXML private ImageView set2;
    @FXML private ImageView set3;
    @FXML private JFXButton reserveButton;

    private boolean check = false;
    private List<JFXButton> seats = new ArrayList<>();
    private Movie movie;
    private Showing showing;
    private double priceD = 0;
    private int chosen = 0;
    private Stage stage;
    private Random r = new Random();
    private User user;



    public TicketControllerOne(User user, Movie movie, Showing showing, Stage stage) {
        this.movie = movie;
        this.showing = showing;
        this.stage = stage;
        this.user = user;
    }

    //sets up functioning buttons for each seat, sets up movie image, all text fields
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        movieView.setImage(movie.getImage());
        movieText.setText(movie.getName());
        dateText.setText(showing.getDateTime());
        set1.setImage(new Image("file:greySQ.png"));
        set2.setImage(new Image("file:orangeSQ.png"));
        set3.setImage(new Image("file:redSQ.png"));
        reserveButton.setDisable(true);

        JFXButton[] a = {a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, b0, b1, b2, b3, b4, b5, b6, b7, b8, b9,
                c0, c1, c2, c3, c4, c5, c6, c7, c8, c9, d0, d1, d2, d3, d4, d5, d6, d7, d8, d9};
        seats.addAll(Arrays.asList(a));

        for(JFXButton b : seats){
            ImageView imageView = new ImageView();
            imageView.setImage(new Image("file:greySQ.png"));
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            b.setGraphic(imageView);
            b.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(imageView.getImage().getPixelReader().getColor(50,50).equals(new Image
                            ("file:redSQ.png").getPixelReader().getColor(50,50))){
                        imageView.setImage(new Image("file:greySQ.png"));
                        imageView.setFitWidth(40);
                        imageView.setFitHeight(40);
                        b.setGraphic(imageView);
                        check = false;
                    } else if(imageView.getImage().getPixelReader().getColor(50,50).equals(new Image
                            ("file:orangeSQ.png").getPixelReader().getColor(50,50))){
                        System.out.println("taken");
                    } else {
                        imageView.setImage(new Image("file:redSQ.png"));
                        imageView.setFitWidth(40);
                        imageView.setFitHeight(40);
                        b.setGraphic(imageView);
                        check = true;
                    }
                    for(JFXButton b : seats){
                        ImageView i = (ImageView) b.getGraphic();
                        if(i.getImage().getPixelReader().getColor(50,50).equals
                                (new Image("file:redSQ.png").getPixelReader().getColor(50,50))){
                            priceD += 5.50;
                            chosen+=1;
                            check = true;
                        }
                    }
                    price.setText("Price: " + priceD + "0€");
                    chosenSeats.setText("Chosen seats: " + chosen);
                    if(check){
                        reserveButton.setDisable(false);
                    } else {
                        reserveButton.setDisable(true);
                    }
                    chosen = 0;
                    priceD = 0;
                    check = false;
                }
            });
        }

        for(int i = 0; i < showing.getTakenSeats().size(); i++){
            String s = showing.getTakenSeats().get(i);
            if(s.equals("x")){
                ImageView imageView = new ImageView();
                imageView.setImage(new Image("file:orangeSQ.png"));
                imageView.setFitWidth(40);
                imageView.setFitHeight(40);
                seats.get(i).setGraphic(imageView);
                seats.get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("x");
                    }
                });
            }
        }
    }

    //'takes' selected seats for the movie, adds order to users' orders
    @FXML
    private void reserve(){
        ArrayList<String> taken = new ArrayList<>();
        ArrayList<Integer> positions = new ArrayList<>();
        for(int i = 0; i < seats.size(); i++){
            ImageView image = (ImageView) seats.get(i).getGraphic();
            if(image.getImage().getPixelReader().getColor(50,50).equals(new Image
                    ("file:redSQ.png").getPixelReader().getColor(50,50))){
                List<String> a = Arrays.asList(showing.getSeats());
                taken.add(a.get(i));
                positions.add(i);
                showing.takeSeat("x", i);
            }
        }
        System.out.println(taken.toString());
        user.getOrders().add(new Order(user.getID(), r.nextInt(9999),positions, showing, movie,taken.toString(), LocalDateTime.now()));
        stage.close();
    }
}
