package com.aizhan.rockitapp.MusicianClasses;

public class MyOffers {
    String offerid;
    String who;
    String when;
    String where;
    String salary;
    String requirements;
    String about;
    String offertype;
    String status;
    String type;

    public MyOffers(){

    }

    public MyOffers(String offerid, String who, String when, String where, String salary, String requirements, String about, String offertype, String status, String type) {
        this.offerid = offerid;
        this.who = who;
        this.when = when;
        this.where = where;
        this.salary = salary;
        this.requirements = requirements;
        this.about = about;
        this.offertype = offertype;
        this.status = status;
        this.type = type;
    }

    public String getOfferid() {
        return offerid;
    }

    public String getWho() {
        return who;
    }

    public String getWhen() {
        return when;
    }

    public String getWhere() {
        return where;
    }

    public String getSalary() {
        return salary;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getAbout() {
        return about;
    }

    public String getOffertype() {
        return offertype;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }
}
