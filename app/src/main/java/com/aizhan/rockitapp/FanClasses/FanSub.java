package com.aizhan.rockitapp.FanClasses;

public class FanSub {
    String musicianid;
    Integer myrate;

    public FanSub(){

    }

    public FanSub(String musicianid, Integer myrate) {
        this.musicianid = musicianid;
        this.myrate = myrate;
    }

    public String getMusicianid() {
        return musicianid;
    }

    public Integer getMyrate() {
        return myrate;
    }
}
