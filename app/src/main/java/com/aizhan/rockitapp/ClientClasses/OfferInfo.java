package com.aizhan.rockitapp.ClientClasses;

public class OfferInfo {
    String offerid;
    String who;
    String when;
    String where;
    String salary;
    String requirements;
    String about;
    String offertype;


    public OfferInfo(){

    }

    public OfferInfo(String offerid, String who, String when, String where, String salary, String requirements, String about, String offertype) {
        this.offerid = offerid;
        this.who = who;
        this.when = when;
        this.where = where;
        this.salary = salary;
        this.requirements = requirements;
        this.about = about;
        this.offertype = offertype;
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
}
