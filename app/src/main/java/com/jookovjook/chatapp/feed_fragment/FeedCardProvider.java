package com.jookovjook.chatapp.feed_fragment;

import java.util.Date;

public class FeedCardProvider {

    private int publication_id;
    private int user_id;
    private String username;
    private String title;
    private int views;
    private int stars;
    private int comments;
    private String img_link;
    private String text;
    private Date date;
    private String small_avatar;

    public FeedCardProvider(int publication_id, int user_id, String username, String title,
                            int views, int stars, int comments, String img_link, String text, Date date, String small_avatar) {
        this.publication_id = publication_id;
        this.user_id = user_id;
        this.username = username;
        this.title = title;
        this.views = views;
        this.stars = stars;
        this.comments = comments;
        this.img_link = img_link;
        this.text = text;
        this.date = date;
        this.small_avatar = small_avatar;
    }

    public int getPublication_id() {
        return publication_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public int getViews() {
        return views;
    }

    public int getStars() {
        return stars;
    }

    public int getComments() {
        return comments;
    }

    public String getImg_link() {
        return img_link;
    }

    public String getText() {
        return text;
    }

    public Date getDate() {
        return date;
    }

    public String getSmall_avatar() {
        return small_avatar;
    }

    public void setPublication_id(int publication_id) {
        this.publication_id = publication_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
