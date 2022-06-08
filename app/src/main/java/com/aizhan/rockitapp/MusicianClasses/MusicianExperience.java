package com.aizhan.rockitapp.MusicianClasses;

public class MusicianExperience {
    String where;
    String who;
    String howlong;
    public MusicianExperience(){
    }

    public MusicianExperience(String where, String who, String howlong){
        this.where = where;
        this.who = who;
        this.howlong = howlong;
    }

    public String getWhere() {
        return where;
    }

    public String getWho() {
        return who;
    }

    public String getHowlong() {
        return howlong;
    }
}
