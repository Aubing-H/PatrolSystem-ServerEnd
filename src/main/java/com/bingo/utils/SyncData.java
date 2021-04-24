package com.bingo.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SyncData {
    private static final String pattern = "yyyy-MM-dd HH:mm:ss";

    public static final int STATE_OK = 1;
    public static final int STATE_FAILED = 2;
    public static final int TYPE_ADMIN = 0;
    public static final int TYPE_PATROL = 1;

    public static String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    public static String dateToString2(Timestamp time){
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(time);
    }

    public static Date stringToDate(String str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.parse(str);
    }

    public static Timestamp stringToTimestamp(String str) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return new Timestamp(sdf.parse(str).getTime());
    }
}
