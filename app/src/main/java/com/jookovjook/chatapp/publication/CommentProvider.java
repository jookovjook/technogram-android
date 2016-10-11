package com.jookovjook.chatapp.publication;

public class CommentProvider {

    private int publication_id;
    private int comment_id;
    private int user_id;
    private String username;
    private String comment;

    CommentProvider(int publication_id, int comment_id, int user_id, String username, String comment){
        this.publication_id = publication_id;
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.username = username;
        this.comment = comment;
    }

    public void setPublication_id(int publication_id) {
        this.publication_id = publication_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPublication_id() {
        return publication_id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }
}
