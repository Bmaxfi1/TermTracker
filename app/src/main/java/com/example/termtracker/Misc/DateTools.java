package com.example.termtracker.Misc;

public abstract class DateTools {
    public static String addHyphensToDate(String rawDate) {
        StringBuilder builder = new StringBuilder(rawDate);
        builder.insert(4, '-');
        builder.insert(7, '-');

        return builder.toString();

    }

//    public static String removeHyphensFromDate(String decoratedDate) {
//
//    }

}
