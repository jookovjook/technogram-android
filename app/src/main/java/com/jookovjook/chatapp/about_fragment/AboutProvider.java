package com.jookovjook.chatapp.about_fragment;

public class AboutProvider {

    private int user_id;
    private String username;
    private String name;
    private String surname;
    private String about;
    private String avatar;

    public AboutProvider(int user_id, String username, String name, String surname, String about, String avatar){
        this.user_id = user_id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.about = about;
        this.avatar = avatar;

    }

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getAbout() {
        return about;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}

