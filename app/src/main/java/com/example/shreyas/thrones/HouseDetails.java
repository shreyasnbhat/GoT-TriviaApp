package com.example.shreyas.thrones;

import java.util.ArrayList;

/**
 * Created by Shreyas on 11/4/2016.
 */

public class HouseDetails {


    private String name;
    private String region;
    private String coatOfArms;
    private String words;
    private ArrayList<String> titles = new ArrayList<>();
    private String currentLord;
    private ArrayList<String> members = new ArrayList<>();

    public HouseDetails()
    {

    }

    public HouseDetails(String name) {
        this.name = name;


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
