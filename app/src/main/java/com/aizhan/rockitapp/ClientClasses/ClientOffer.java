package com.aizhan.rockitapp.ClientClasses;

public class ClientOffer {
    String offerid;
    String musicianid;
    String status;
    String type;


    public ClientOffer(){

    }

    public ClientOffer(String offerid, String musicianid, String status, String type) {
        this.offerid = offerid;
        this.musicianid = musicianid;
        this.status = status;
        this.type = type;
    }

    public String getOfferid() {
        return offerid;
    }

    public String getMusicianid() {
        return musicianid;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }
}
