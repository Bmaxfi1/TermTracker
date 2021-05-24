package com.example.termtracker.Model;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public enum AssessmentType {
    OBJECTIVE("objective"),
    PERFORMANCE("performance");

    private final String string;

    @NonNull
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

    // Reverse-lookup map for getting a type from a string
    private static final Map<String, AssessmentType> lookup = new HashMap<String, AssessmentType>();

    static {
        for (AssessmentType d : AssessmentType.values()) {
            lookup.put(d.getString(), d);
        }
    }

    //constructor
    AssessmentType(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public static AssessmentType get(String string) {
        return lookup.get(string);
    }

}

