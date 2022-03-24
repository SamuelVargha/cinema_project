package com.vargha;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

public class Showing {
    private String dateTime;
    private String screen;
    private int totalSeats;
    private int freeSeats;
    private String date;
    private LocalDate localDate;
    private LocalDateTime localDateTime;
    private String time;
    private ArrayList<String> takenSeats = new ArrayList<>();
    private Movie movie;
    private String movieName;
    private String[] seats;

    public Showing(String dateTime, String screen, int totalSeats, int freeSeats) {
        this.dateTime = dateTime;
        this.screen = screen;
        this.totalSeats = totalSeats;
        this.freeSeats = freeSeats;
        String[] s = {"a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "b0", "b1", "b2", "b3", "b4", "b5",
                "b6", "b7", "b8", "b9", "c0", "c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "d0", "d1", "d2",
                "d3", "d4", "d5", "d6", "d7", "d8", "d9", "e0", "e1", "e2", "e3", "e4", "e5", "e6", "e7", "e8", "e9"};
        seats = s;
        takenSeats.addAll(Arrays.asList(s));
        String[] split = dateTime.split("/", 2);
        time = split[0];
        date = split[1];

        if(!date.equals("00/0000")){
            localDate = toLocalDate(date);
        }

        if(!dateTime.equals("00/00/0000")){
            localDateTime = toLocalDateTime(dateTime);
        }
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        movieName = movie.getName();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {

        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTakenSeats(ArrayList<String> takenSeats) {
        this.takenSeats = takenSeats;
    }

    public void takeSeat(String s, int pos){
        takenSeats.set(pos, s);
    }

    public ArrayList<String> getTakenSeats() {
        return takenSeats;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getFreeSeats() {
        return freeSeats;
    }

    public void setFreeSeats(int freeSeats) {
        this.freeSeats = freeSeats;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String[] getSeats() {
        return seats;
    }

    private LocalDate toLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate.parse(date, formatter);
        return LocalDate.parse(date, formatter);
    }

    private LocalDateTime toLocalDateTime(String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm/dd/MM/yyyy");
        return LocalDateTime.parse(dateTime, formatter);
    }

}
