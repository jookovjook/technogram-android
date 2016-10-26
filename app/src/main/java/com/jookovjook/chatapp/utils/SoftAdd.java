package com.jookovjook.chatapp.utils;

public class SoftAdd {

    public static  String getLicenseById(int id){
        switch (id){
            case 1: return "Free Source";
            case 2: return "Free API";
            case 3: return "API";
            case 4: return "MIT License";
            case 5: return "Apache License";
            case 6: return "BSD License";
            case 7: return "Proprietary";
            default: return "";
        }
    }

    public static String getStageById(int id){
        switch (id){
            case 1: return "Concept";
            case 2: return "Prototype";
            case 3: return "Product";
            case 4: return "Alpha Version";
            case 5: return "Beta Version";
            case 6: return "Public Beta";
            case 7: return "Developing";
            case 8: return "Release";
            case 9: return "Frozen";
            case 10: return "Closed";
            default: return "";
        }
    }

}
