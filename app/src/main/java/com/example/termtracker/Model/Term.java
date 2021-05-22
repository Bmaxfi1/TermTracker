package com.example.termtracker.Model;

import java.util.ArrayList;
import java.util.Objects;


//cannot be deleted if a course is assigned to it
public class Term extends ScheduledItem implements Validatable{
    private ArrayList<Course> courseList;

    public Term(int id, String title, String startDate, String endDate, boolean completed, boolean canBeDeleted) {
        super(id, title, startDate, endDate, completed, canBeDeleted);
    }


    @Override
    public boolean isValid() {

        if (Objects.equals(this.getTitle(), "") || Objects.equals(this.getStartDate(), "") || Objects.equals(this.getEndDate(), "")) {
            return false;
        }
        if (Integer.parseInt(this.getStartDate()) > Integer.parseInt(this.getEndDate())) {
            return false;
        }


        return true;
    }
}
