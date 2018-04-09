package com.example.app.quickler;

/**
 * Created by Deep on 31-03-2018.
 */

public class CardData {
    String name;
    String image;  //We can use this var to send URL of image stored in Database

    boolean isbtn = false;
    boolean ismark = false;

    //Default Constructor
    public CardData() {
    }

    public CardData(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
