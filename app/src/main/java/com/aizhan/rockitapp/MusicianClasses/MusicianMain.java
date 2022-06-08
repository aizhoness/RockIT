package com.aizhan.rockitapp.MusicianClasses;

public class MusicianMain {

    private String nameSurname;
    private String imageUri;
    private double rating;
    private String skills;
    private Integer subscribes;
    private Integer audios;
    private Integer videos;

    public MusicianMain(){

    }

    public MusicianMain(String nameSurname, String imageUri, double rating, String skills, Integer subscribes, Integer audios, Integer videos) {
        this.nameSurname = nameSurname;
        this.imageUri = imageUri;
        this.rating = rating;
        this.skills = skills;
        this.subscribes = subscribes;
        this.audios = audios;
        this.videos = videos;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public String getImageUri() {
        return imageUri;
    }

    public double getRating() {
        return rating;
    }

    public String getSkills() {
        return skills;
    }

    public Integer getSubscribes() {
        return subscribes;
    }

    public Integer getAudios() {
        return audios;
    }

    public Integer getVideos() {
        return videos;
    }

    public void setNameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public void setSubscribes(Integer subscribes) {
        this.subscribes = subscribes;
    }

    public void setAudios(Integer audios) {
        this.audios = audios;
    }

    public void setVideos(Integer videos) {
        this.videos = videos;
    }
}
