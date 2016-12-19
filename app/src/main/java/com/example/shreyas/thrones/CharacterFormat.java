package com.example.shreyas.thrones;

import java.util.Comparator;

/**
 * Created by Shreyas on 11/4/2016.
 */

public class CharacterFormat implements Comparable<CharacterFormat>  {

    String name="";
    String gender="Unknown :p";


    String culture="Not Found";
    String born="Not Found";
    String died="Not Found/Not Dead";
    String playedBy="Not Found";

    public CharacterFormat()
    {

    }

    @Override
    public int compareTo(CharacterFormat o) {
        return name.compareTo(o.getName());
    }

    public CharacterFormat(String name) {
        this.name = name;
    }

    public CharacterFormat(String name, String playedBy,String gender,String born,String died) {
        if(name.length()!=0)
        this.name = name;
        if(playedBy.length()!=0)
        this.playedBy = playedBy;
        if(gender.length()!=0)
        this.gender = gender;
        if(born.length()!=0)
        this.born = born;
        if(died.length()!=0)
        this.died = died;
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
