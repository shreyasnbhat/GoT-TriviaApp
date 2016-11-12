package com.example.shreyas.thrones;

import java.util.ArrayList;

/**
 * Created by Shreyas on 11/4/2016.
 */

public class HouseDetails {


    private String name = "";
    private String region = "";
    private String coatOfArms = "";
    private String words = "";
    private ArrayList<String> titles = new ArrayList<>();

    public void setCurrentLord(String currentLord) {
        this.currentLord = currentLord;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCoatOfArms(String coatOfArms) {
        this.coatOfArms = coatOfArms;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    private String currentLord = "";
    private ArrayList<String> members = new ArrayList<>();

    public HouseDetails() {}

    public HouseDetails(String name) {

        this.name = name;
        this.region = " - ";
        this.currentLord=" - ";
        this.coatOfArms = " - ";
        this.words = " - ";
    }

    public HouseDetails(String name, String region, String coatOfArms, String words, ArrayList<String> titles, String currentLord, ArrayList<String> members) {

        this.name = name;
        this.region = region;
        this.coatOfArms = coatOfArms;
        this.words = words;
        this.titles = titles;
        this.currentLord = currentLord;
        this.members = members;
    }


    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getCoatOfArms() {
        return coatOfArms;
    }

    public String getWords() {
        return words;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public String getCurrentLord() {
        return currentLord;
    }

    public ArrayList<String> getMembers() {
        return members;
    }
}
