package com.vargha;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Order {
    private int userID;
    private int ID;
    private ArrayList<Integer> positions;
    private Showing showing;
    private Movie movie;
    private String movieName;
    private String seats;
    private LocalDateTime dateTime;
    private LocalDateTime showingDateTime;
    private String dateTimeString;

    public Order(int userID, int ID, ArrayList<Integer> positions, Showing showing, Movie movie, String seats, LocalDateTime dateTime) {
        this.userID = userID;
        this.ID = ID;
        this.showing = showing;
        this.movie = movie;
        this.seats = seats;
        this.dateTime = dateTime;
        this.positions = positions;
        this.movieName = movie.getName();
        showingDateTime = showing.getLocalDateTime();
    }

    public LocalDateTime getShowingDateTime() {
        return showingDateTime;
    }

    public String getMovieName() {
        return movieName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    ArrayList<Integer> getPositions() {
        return positions;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setPositions(ArrayList<Integer> positions) {
        this.positions = positions;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Showing getShowing() {
        return showing;
    }

    public void setShowing(Showing showing) {
        this.showing = showing;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getDateTimeString() {
        return toDateString(dateTime);
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    private String toDateString(LocalDateTime lt){
        String s = lt.toString().toLowerCase().replaceAll("-", "/");
        s = s.toLowerCase().replaceAll("t", " ");
        String[] sArray = s.split(":", 3 );
        return sArray[0] + ":" + sArray[1];
    }
}
