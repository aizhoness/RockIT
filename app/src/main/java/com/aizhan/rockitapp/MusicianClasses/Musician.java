package com.aizhan.rockitapp.MusicianClasses;

public class Musician {
    private String mus_id;
    private String image_url;
    private String name_surname, skills;
    private double rating;

    public Musician(){

    }

    public Musician(String mus_id, String image_url, String name_surname, String skills, double rating) {
        this.mus_id = mus_id;
        this.image_url = image_url;
        this.name_surname = name_surname;
        this.skills = skills;
        this.rating = rating;
    }

    public String getMus_id() {
        return mus_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getName_surname() {
        return name_surname;
    }

    public String getSkills() {
        return skills;
    }

    public double getRating() {
        return rating;
    }
}
