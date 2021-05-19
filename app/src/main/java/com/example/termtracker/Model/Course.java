package com.example.termtracker.Model;

import java.util.ArrayList;

public class Course extends ScheduledItem implements Validatable{
    private CourseStatus status;
    private ArrayList<CourseInstructor> courseInstructors;
    private ArrayList<Assessment> assessments;
    private ArrayList<Note> notes;

    public Course(String title, String startDate, String endDate, boolean completed, boolean canBeCheckedOff, boolean canBeDeleted) {
        super(title, startDate, endDate, completed, canBeCheckedOff, canBeDeleted);
    }

    @Override
    public boolean isValid() {
        return false;
    }
}

