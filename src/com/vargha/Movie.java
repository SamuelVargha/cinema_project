package com.vargha;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Movie {
    private String name;
    private String description;
    private int length;
    private Image image;
    private ArrayList<Showing> showings;

    public Movie(String name, String description, int length, Image image, ArrayList<Showing> showings) {
        this.name = name;
        this.description = description;
        this.length = length;
        this.image = image;
        this.showings = showings;
        for (Showing s: showings){
            s.setMovie(this);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getLength() {
        return length;
    }

    public ArrayList<Showing> getShowings() {
        return showings;
    }

    public void setShowings(ArrayList<Showing> showings) {
        this.showings = showings;
    }
}
