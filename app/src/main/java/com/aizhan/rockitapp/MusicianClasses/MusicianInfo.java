package com.aizhan.rockitapp.MusicianClasses;

public class MusicianInfo {
    String name;
    String surname;
    Integer age;
    String country;
    String city;

    public MusicianInfo(){
    }
    public MusicianInfo(String name, String surname, Integer age, String country, String city){
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.country = country;
        this.city = city;
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
}
