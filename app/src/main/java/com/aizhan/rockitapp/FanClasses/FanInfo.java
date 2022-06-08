package com.aizhan.rockitapp.FanClasses;

public class FanInfo {
    String name;
    String surname;
    Integer age;
    String country;
    String city;
    String gmail;

    public FanInfo(){

    }

    public FanInfo(String name, String surname, Integer age, String country, String city, String gmail){
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.country = country;
        this.city = city;
        this.gmail = gmail;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Integer getAge() {
        return age;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getGmail() {
        return gmail;
    }
}
