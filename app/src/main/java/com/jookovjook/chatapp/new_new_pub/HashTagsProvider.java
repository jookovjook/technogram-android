package com.jookovjook.chatapp.new_new_pub;

public class HashTagsProvider {

    private String hashTag;
    private int level;
    private int type;

    public HashTagsProvider(String hashTag, int level, int type){
        this.hashTag = hashTag;
        this.level = level;
        this.type = type;
    }

    public String getHashTag() {
        return hashTag;
    }

    public int getLevel() {
        return level;
    }

    public int getType() {
        return type;
    }
}
