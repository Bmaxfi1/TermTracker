package com.example.termtracker.Model;

import android.content.Context;

import java.util.ArrayList;

public class Course extends ScheduledItem implements Validatable{
    private CourseStatus status;
    private ArrayList<CourseInstructor> courseInstructors;
    private ArrayList<Assessment> assessments;
    private ArrayList<Note> notes;

    public Course(int id, String title, String startDate, String endDate, boolean completed, boolean canBeCheckedOff, boolean canBeDeleted) {
        super(id, title, startDate, endDate, completed, canBeDeleted);
    }

    @Override
    public boolean isValid(Context context) {
        return false;
    }
}

