package com.example.termtracker.Model;

import java.util.ArrayList;

public class Course extends ScheduledItem {
    private CourseStatus status;
    private ArrayList<CourseInstructor> courseInstructors;
    private ArrayList<Assessment> assessments;
    private ArrayList<Note> notes;
}

