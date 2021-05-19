package com.example.termtracker.Model;

public class Assessment extends ScheduledItem implements Validatable {
    private AssessmentType assessmentType;

    public Assessment(String title, String startDate, String endDate, boolean completed, boolean canBeCheckedOff, boolean canBeDeleted) {
        super(title, startDate, endDate, completed, canBeCheckedOff, canBeDeleted);
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
