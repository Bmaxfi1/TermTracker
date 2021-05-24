package com.example.termtracker.Model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.termtracker.Data.DatabaseHelper;

import java.util.Objects;

public class Assessment extends ScheduledItem implements Validatable {
    private AssessmentType assessmentType;
    int courseId;

    public Assessment(int id, String title, String startDate, String endDate, boolean completed, boolean canBeDeleted, AssessmentType type, int courseId) {
        super(id, title, startDate, endDate, completed, canBeDeleted);
        assessmentType = type;
        this.courseId = courseId;
    }

    public AssessmentType getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(AssessmentType assessmentType) {
        this.assessmentType = assessmentType;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean isValid(Context context) {
        boolean valid = true;
        boolean fieldsComplete = true;
        if (Objects.equals(this.getTitle(), "") || Objects.equals(this.getStartDate(), "") || Objects.equals(this.getEndDate(), "")) {
            Toast.makeText(context, "Please complete all fields.", Toast.LENGTH_SHORT).show();
            valid = false;
            fieldsComplete = false;
        }

        Log.d("superdopetag", this.getStartDate());

        //further validation happens only if all fields are complete
        if (fieldsComplete) {
            if (Integer.parseInt(this.getStartDate()) > Integer.parseInt(this.getEndDate())) {
                Toast.makeText(context, "The assessment end date should not come before the assessment start date.", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            //assessment cannot exist outside the course start/end dates
            DatabaseHelper helper = new DatabaseHelper(context);
            Course courseToCheck = helper.getCourseById((int) this.courseId);

            Log.d("superdopetag", String.valueOf(Integer.parseInt(courseToCheck.getStartDate())));
            Log.d("superdopetag", String.valueOf(Integer.parseInt(this.getStartDate())));

            if (Integer.parseInt(courseToCheck.getStartDate()) > Integer.parseInt(this.getStartDate()) ||
                    Integer.parseInt(courseToCheck.getEndDate()) < Integer.parseInt(this.getEndDate())) {
                Toast.makeText(context, "The selected assessment dates are outside the dates of the selected course.", Toast.LENGTH_SHORT).show();
                valid = false;
            }
            return valid;
        }
    return false;
    }
}
