package com.example.termtracker.Model;

import android.content.Context;

public class Assessment extends ScheduledItem implements Validatable {
    private AssessmentType assessmentType;

    public Assessment(int id, String title, String startDate, String endDate, boolean completed, boolean canBeDeleted) {
        super(id, title, startDate, endDate, completed, canBeDeleted);
    }

    @Override
    public boolean isValid(Context context) {
        return false;
    }
}
