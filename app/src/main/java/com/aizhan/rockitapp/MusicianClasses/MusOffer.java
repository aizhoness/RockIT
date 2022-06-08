package com.aizhan.rockitapp.MusicianClasses;

public class MusOffer {
    String offerid;
    String status;
    String type;

    public MusOffer(){

    }

    public MusOffer(String offerid, String status, String type) {
        this.offerid = offerid;
        this.status = status;
        this.type = type;
    }

    public String getOfferid() {
        return offerid;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }
}
