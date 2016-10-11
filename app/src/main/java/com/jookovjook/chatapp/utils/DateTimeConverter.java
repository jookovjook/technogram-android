package com.jookovjook.chatapp.utils;

import java.util.Calendar;
import java.util.Date;

public class DateTimeConverter {

    private Date date;

    public DateTimeConverter(Date date){
        this.date = date;
    }

    public String convert(){
        String result = "";
        String months[] = new String[]{"January","February","March","April","May","June","July",
                "August","September","October","November","December"};

        Date date_now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(date_now);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int day_y = cal.get(Calendar.DAY_OF_YEAR);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE) + hour*60;
        int minute1 = cal.get(Calendar.MINUTE);

        int year_now = cal_now.get(Calendar.YEAR);
        int day_y_now = cal_now.get(Calendar.DAY_OF_YEAR);
        int hour_now = cal_now.get(Calendar.HOUR_OF_DAY);
        int minute_now = cal_now.get(Calendar.MINUTE) + hour_now*60;


        if(year < year_now) {
            result = String.valueOf(day) + " " + months[month] + " " + String.valueOf(year);
        }else{
            if(day_y_now - day_y > 6){
                result = String.valueOf(day) + " " + months[month];
            }else{
                if(day_y_now - day_y > 1){
                   result =  String.valueOf(day) + " " + months[month] + " "
                           + String.valueOf(hour) + ":";
                    if(minute1 < 10) {result += "0" + String.valueOf(minute1);}
                    else {result += String.valueOf(minute1);}
                }else {
                    if(day_y_now - day_y == 1){
                        result = "Yesterday at " + String.valueOf(hour) + ":";
                        if(minute1 < 10) {result += "0" + String.valueOf(minute1);}
                        else {result += String.valueOf(minute1);}
                    }else{
                        if(minute_now - minute > 15){
                            result = "Today at " + String.valueOf(hour) + ":";
                            if(minute1 < 10) {result += "0" + String.valueOf(minute1);}
                            else {result += String.valueOf(minute1);}
                        }else{
                            if(minute_now - minute > 1){
                                result =String.valueOf(minute_now - minute) + " minutes ago";
                            }else{
                                result = "Just now";
                            }
                        }
                    }
                }
            }
        }
        /*
        */

        return result;
    }
}
