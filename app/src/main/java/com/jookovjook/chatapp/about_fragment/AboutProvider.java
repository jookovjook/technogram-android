package com.jookovjook.chatapp.about_fragment;

public class AboutProvider {

    private int user_id;
    private String username;
    private String name;
    private String surname;

    public AboutProvider(int user_id, String username, String name, String surname){
        this.user_id = user_id;
        this.username = username;
        this.name = name;
        this.surname = surname;
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

}

