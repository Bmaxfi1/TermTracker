package com.example.termtracker.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;


//cannot be deleted if a course is assigned to it
public class Term extends ScheduledItem implements Validatable{
    private ArrayList<Course> courseList;

    public Term(String title, String startDate, String endDate, boolean completed, boolean canBeCheckedOff, boolean canBeDeleted) {
        super(title, startDate, endDate, completed, canBeCheckedOff, canBeDeleted);
    }


    @Override
    public boolean isValid() {

        if (Objects.equals(this.getTitle(), "") || Objects.equals(this.getStartDate(), "") || Objects.equals(this.getEndDate(), "")) {
            return false;
        }
        return true;
    }
}
