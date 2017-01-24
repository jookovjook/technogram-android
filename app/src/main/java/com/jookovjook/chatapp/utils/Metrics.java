package com.jookovjook.chatapp.utils;


import android.content.res.Resources;

public class Metrics {

    public int screenWidth;
    public int screenHeight;

    public  static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
