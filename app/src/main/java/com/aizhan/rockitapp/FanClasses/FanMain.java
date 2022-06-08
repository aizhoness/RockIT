package com.aizhan.rockitapp.FanClasses;

public class FanMain {
    String image_url;
    String name_surname;
    Integer subscribes;
    Integer audios;
    Integer videos;

    public FanMain(){

    }

    public FanMain(String image_url, String name_surname, Integer subscribes, Integer audios, Integer videos) {
        this.image_url = image_url;
        this.name_surname = name_surname;
        this.subscribes = subscribes;
        this.audios = audios;
        this.videos = videos;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getName_surname() {
        return name_surname;
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
}
