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
        if (Objects.equals(this.getTitle(), "") || Objects.equals(this.getStartDate(), "") || Objects.equals(this.getEndDate(), "")) {
            Toast.makeText(context, "Please complete all fields.", Toast.LENGTH_SHORT).show();
            valid = false;
        }
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
                Toast.makeText(context, "There is already a Term with that name.", Toast.LENGTH_SHORT).show();
                valid = false;
            }
        }
        return valid;
    }
}
