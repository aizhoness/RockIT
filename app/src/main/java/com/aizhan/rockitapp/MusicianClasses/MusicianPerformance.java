package com.aizhan.rockitapp.MusicianClasses;

public class MusicianPerformance {
    String where;
    String activity;

    public MusicianPerformance(){

    }
    public MusicianPerformance(String where, String activity){
        this.where = where;
        this.activity = activity;
    }

    public String getWhere() {
        return where;
    }

    public String getActivity() {
        return activity;
    }
}
