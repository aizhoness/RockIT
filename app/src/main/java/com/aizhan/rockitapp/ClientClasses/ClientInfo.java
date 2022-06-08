package com.aizhan.rockitapp.ClientClasses;

public class ClientInfo {
    String client_name;
    String client_type;
    String country;
    String city;
    String phone_number;
    String gmail;
    String facebook;
    String site;
    String insta;

    public  ClientInfo(){
    }

    public ClientInfo(String client_name, String client_type, String country, String city, String phone_number, String gmail, String facebook, String site, String insta){
        this.client_name = client_name;
        this.client_type = client_type;
        this.country = country;
        this.city = city;
        this.phone_number = phone_number;
        this.gmail = gmail;
        this.facebook = facebook;
        this.site = site;
        this.insta = insta;
    }

    public String getSite() {
        return site;
    }

    public String getInsta() {
        return insta;
    }

    public String getClient_name() {
        return client_name;
    }

    public String getClient_type() {
        return client_type;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getGmail() {
        return gmail;
    }

    public String getFacebook() {
        return facebook;
    }
}
