package com.jookovjook.chatapp.feed_fragment;


import java.util.Date;

public class FeedCardProvider {

    public int pub_id;
    public int user_id;
    public String username;
    public String title;
    public int views;
    public int likes;
    public int comments;
    public String img_link;
    public String text;
    public Date date;
    public String small_avatar;

    public FeedCardProvider(int pub_id, int user_id, String username, String title,
                            int views, int likes, int comments, String img_link,
                            String text, Date date, String small_avatar){
        this.pub_id = pub_id;
        this.user_id = user_id;
        this.username = username;
        this.title = title;
        this.views = views;
        this.likes = likes;
        this.comments = comments;
        this.img_link = img_link;
        this.text = text;
        this.date = date;
        this.small_avatar = small_avatar;
    }

}
