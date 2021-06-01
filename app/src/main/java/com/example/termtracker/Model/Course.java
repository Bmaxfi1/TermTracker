package com.example.termtracker.Model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.termtracker.Data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Course extends ScheduledItem implements Validatable {
    private CourseStatus status;
    private int termId;
    private ArrayList<CourseInstructor> courseInstructors;
    private ArrayList<Assessment> assessments;
    private ArrayList<Note> notes;

    public Course(int id, String title, String startDate, String endDate, boolean completed, boolean canBeDeleted, int termId) {
        super(id, title, startDate, endDate, completed, canBeDeleted);
        this.termId = termId;
    }

    @Override
    public boolean isValid(Context context) {
        boolean valid = true;
        boolean fieldsComplete = true;
        if (Objects.equals(this.getTitle(), "") || Objects.equals(this.getStartDate(), "") || Objects.equals(this.getEndDate(), "") ||
                Objects.equals(this.getTermId(), "")) {
            Toast.makeText(context, "Please complete all fields.", Toast.LENGTH_SHORT).show();
            valid = false;
            fieldsComplete = false;
        }

        Log.d("superdopetag", this.getStartDate());

        //further validation happens only if all fields are complete
        if (fieldsComplete) {
            if (Integer.parseInt(this.getStartDate()) > Integer.parseInt(this.getEndDate())) {
                Toast.makeText(context, "The course end date should not come before the course start date.", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            //duplicate course names not allowed
            DatabaseHelper helper = new DatabaseHelper(context);
            List<Course> allCourses = helper.getAllCourses();
            List<String> allCoursesAsString = new ArrayList<>();
            for (Course course : allCourses) {
                allCoursesAsString.add(course.getTitle());
            }
            for (String string : allCoursesAsString) {
                if (this.getTitle().equals(string)) {
                    Toast.makeText(context, "There is already a course with that name.", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
            }

            //course cannot exist outside the term start/end dates
            Term termToCheck = helper.getTermById(this.termId);

            Log.d("superdopetag", String.valueOf(Integer.parseInt(termToCheck.getStartDate())));
            Log.d("superdopetag", String.valueOf(Integer.parseInt(this.getStartDate())));

            if (Integer.parseInt(termToCheck.getStartDate()) > Integer.parseInt(this.getStartDate()) ||
            Integer.parseInt(termToCheck.getEndDate()) < Integer.parseInt(this.getEndDate())) {
                Toast.makeText(context, "The selected course dates are outside the dates of the selected term", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            return valid;
        }
        return false;
    }

    public boolean isValidEdit(Context context) {
        boolean valid = true;
        boolean fieldsComplete = true;
        if (Objects.equals(this.getTitle(), "") || Objects.equals(this.getStartDate(), "") || Objects.equals(this.getEndDate(), "") ||
                Objects.equals(this.getTermId(), "")) {
            Toast.makeText(context, "Please complete all fields.", Toast.LENGTH_SHORT).show();
            valid = false;
            fieldsComplete = false;
        }

        Log.d("superdopetag", this.getStartDate());

        //further validation happens only if all fields are complete
        if (fieldsComplete) {
            if (Integer.parseInt(this.getStartDate()) > Integer.parseInt(this.getEndDate())) {
                Toast.makeText(context, "The course end date should not come before the course start date.", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            DatabaseHelper helper = new DatabaseHelper(context);

            //course cannot exist outside the term start/end dates
            Term termToCheck = helper.getTermById(this.termId);

            Log.d("superdopetag", String.valueOf(Integer.parseInt(termToCheck.getStartDate())));
            Log.d("superdopetag", String.valueOf(Integer.parseInt(this.getStartDate())));

            if (Integer.parseInt(termToCheck.getStartDate()) > Integer.parseInt(this.getStartDate()) ||
                    Integer.parseInt(termToCheck.getEndDate()) < Integer.parseInt(this.getEndDate())) {
                Toast.makeText(context, "The selected course dates are outside the dates of the selected term", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            //find out if edit would push assessment dates out of range
            List<Assessment> allAssessments = helper.getAllAssessments();
            boolean startDateClipped = false;
            boolean endDateClipped = false;
            for (Assessment assessment: allAssessments) {
                if (assessment.getCourseId() == this.getId()) {
                    if (Integer.parseInt(assessment.getStartDate()) < Integer.parseInt(this.getStartDate())) {
                        startDateClipped = true;
                        valid = false;
                    }
                    if (Integer.parseInt(assessment.getEndDate()) > Integer.parseInt(this.getEndDate())) {
                        endDateClipped = true;
                        valid = false;
                    }
                }
                if (startDateClipped && endDateClipped) {
                    break;
                }
            }
            if (startDateClipped) {
                Toast.makeText(context, "The course start date cannot come after any of the assessment start dates.", Toast.LENGTH_SHORT).show();
            }
            if (endDateClipped) {
                Toast.makeText(context, "The course end date cannot come before any of the assessment end dates.", Toast.LENGTH_SHORT).show();
            }

            return valid;
        }
        return false;
    }


    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }
}

