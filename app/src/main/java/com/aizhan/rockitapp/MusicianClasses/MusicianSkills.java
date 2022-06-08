package com.aizhan.rockitapp.MusicianClasses;

public class MusicianSkills {
    String skills;
    String email;
    String phone;
    String insta;
    String facebook;
    String soundcloud;
    String about;
    public MusicianSkills(){
    }

    public MusicianSkills(String skills, String email, String phone, String insta, String facebook, String soundcloud, String about) {
        this.skills = skills;
        this.email = email;
        this.phone = phone;
        this.insta = insta;
        this.facebook = facebook;
        this.soundcloud = soundcloud;
        this.about = about;
    }

    public String getSkills() {
        return skills;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getInsta() {
        return insta;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getSoundcloud() {
        return soundcloud;
    }

    public String getAbout() {
        return about;
    }
}
