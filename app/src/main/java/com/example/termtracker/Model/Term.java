package com.example.termtracker.Model;

import android.content.Context;
import android.widget.Toast;

import com.example.termtracker.Data.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


//cannot be deleted if a course is assigned to it
public class Term extends ScheduledItem implements Validatable {
    private ArrayList<Course> courseList;

    public Term(int id, String title, String startDate, String endDate, boolean completed, boolean canBeDeleted) {
        super(id, title, startDate, endDate, completed, canBeDeleted);
    }


    public boolean isValid(Context context) {
        boolean valid = true;
        boolean fieldsComplete = true;
        if (Objects.equals(this.getTitle(), "") || Objects.equals(this.getStartDate(), "") || Objects.equals(this.getEndDate(), "")) {
            Toast.makeText(context, "Please complete all fields.", Toast.LENGTH_SHORT).show();
            valid = false;
            fieldsComplete = false;
        }

        //further validation happens only if all fields are complete
        if (fieldsComplete) {
            if (Integer.parseInt(this.getStartDate()) > Integer.parseInt(this.getEndDate())) {
                Toast.makeText(context, "The term end date should not come before the term start date.", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            //duplicate term names not allowed
            DatabaseHelper helper = new DatabaseHelper(context);
            List<Term> allTerms = helper.getAllTerms();
            List<String> allTermsAsString = new ArrayList<>();
            for (Term term : allTerms) {
                allTermsAsString.add(term.getTitle());
            }
            for (String string : allTermsAsString) {
                if (this.getTitle().equals(string)) {
                    Toast.makeText(context, "There is already a term with that name.", Toast.LENGTH_SHORT).show();
                    valid = false;
                }
            }
            return valid;
        }
        return false;
    }

    public boolean isValidEdit(Context context) {
        boolean valid = true;
        boolean fieldsComplete = true;
        if (Objects.equals(this.getTitle(), "") || Objects.equals(this.getStartDate(), "") || Objects.equals(this.getEndDate(), "")) {
            Toast.makeText(context, "Please complete all fields.", Toast.LENGTH_SHORT).show();
            valid = false;
            fieldsComplete = false;
        }

        //further validation happens only if all fields are complete
        if (fieldsComplete) {
            if (Integer.parseInt(this.getStartDate()) > Integer.parseInt(this.getEndDate())) {
                Toast.makeText(context, "The term end date should not come before the term start date.", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            //find out if edit would push course dates out of range
            DatabaseHelper helper = new DatabaseHelper(context);
            List<Course> allCourses = helper.getAllCourses();
            boolean startDateClipped = false;
            boolean endDateClipped = false;
            for (Course course: allCourses) {
                if (course.getTermId() == this.getId()) {
                    if (Integer.parseInt(course.getStartDate()) < Integer.parseInt(this.getStartDate())) {
                        startDateClipped = true;
                        valid = false;
                    }
                    if (Integer.parseInt(course.getEndDate()) > Integer.parseInt(this.getEndDate())) {
                        endDateClipped = true;
                        valid = false;
                    }
                }
                if (startDateClipped && endDateClipped) {
                    break;
                }
            }
            if (startDateClipped) {
                Toast.makeText(context, "The term start date cannot come after any of the course start dates.", Toast.LENGTH_SHORT).show();
            }
            if (endDateClipped) {
                Toast.makeText(context, "The term end date cannot come before any of the course end dates.", Toast.LENGTH_SHORT).show();
            }

            return valid;
        }
        return false;
    }

}
