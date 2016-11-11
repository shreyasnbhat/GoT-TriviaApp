package com.example.shreyas.thrones;

/**
 * Created by Shreyas on 11/4/2016.
 */

public class CharacterDetails {

    String name;
    String gender;
    String culture;
    String born;
    String died;
    String playedBy;

    public  CharacterDetails()
    {

    }

    public CharacterDetails(String name, String gender, String culture, String born, String died, String playedBy) {
        this.name = name;
        this.gender = gender;
        this.culture = culture;
        this.born = born;
        this.died = died;
        this.playedBy = playedBy;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getCulture() {
        return culture;
    }

    public String getBorn() {
        return born;
    }

    public String getDied() {
        return died;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public void setDied(String died) {
        this.died = died;
    }

    public void setPlayedBy(String playedBy) {
        this.playedBy = playedBy;
    }

    public String getPlayedBy() {
        return playedBy;
    }
}
