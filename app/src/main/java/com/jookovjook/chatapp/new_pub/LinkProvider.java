package com.jookovjook.chatapp.new_pub;

public class LinkProvider {

    public String link;
    public Logo logo;
    public int selectionId;

    public LinkProvider(){
        this.link = "";
        this.logo = Logo.web;
        this.selectionId = 0;
    }

    public int getType(){
        switch(logo){
            case bitbucket: return 2;
            case facebook: return 3;
            case github: return 4;
            case youtube: return 5;
            default: return 1;
        }
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }
}
