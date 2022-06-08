package com.aizhan.rockitapp.MusicianClasses;

public class MusSub {
    String fanid;
    Integer myrate;


    public MusSub(){

    }

    public MusSub(String fanid, Integer myrate) {
        this.fanid = fanid;
        this.myrate = myrate;
    }

    public String getFanid() {
        return fanid;
    }

    public Integer getMyrate() {
        return myrate;
    }
}
