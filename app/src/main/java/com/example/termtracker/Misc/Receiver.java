package com.example.termtracker.Misc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.MainActivity;
import com.example.termtracker.Model.Assessment;
import com.example.termtracker.Model.Course;
import com.example.termtracker.Model.Term;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Receiver extends BroadcastReceiver {
    public Receiver() {

    }

    @Override
    public void onReceive(Context context, Intent i) {

        //get today's date and format it to match how it is stored in the database
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String formattedToday = format.format(today);

        //check to see if there is an important date today
        DatabaseHelper helper = new DatabaseHelper(context);
        List<Course> allCourses = helper.getAllCourses();
        List<Assessment> allAssessments = helper.getAllAssessments();
        int datesToday = 0;
        if (allCourses != null) {
            for (Course course: allCourses) {
                if (course.getStartDate().equals(formattedToday)) {
                    Notifications.makeNotification(context, "Course starts today", course.getTitle() + " is starting today.");
                    datesToday++;
                }
                if (course.getEndDate().equals(formattedToday)) {
                    Notifications.makeNotification(context, "Course ends today", course.getTitle() + " is ending today.");
                    datesToday++;
                }
            }
        }
        if (allAssessments != null) {
            for (Assessment assessment: allAssessments) {
                boolean thisAssessmentStartsToday = false;  //notifying of the end date is useless if the assessment end date is the same as the start date.
                if (assessment.getStartDate().equals(formattedToday)) {
                    thisAssessmentStartsToday = true;
                    Notifications.makeNotification(context, "Assessment starts today", assessment.getTitle() + " is starting today.");
                    datesToday++;
                }
                if (assessment.getEndDate().equals(formattedToday) && !thisAssessmentStartsToday) {
                    Notifications.makeNotification(context, "Assessment ends today", assessment.getTitle() + " is starting today.");
                    datesToday++;
                }
            }
        }

        if (datesToday > 1) {
            Notifications.makeNotification(context, "Multiple important dates today", "Check the app and look for key start/end dates.");
        } else if (datesToday == 0) {
            Notifications.makeNotification(context, "No important dates today", "We'll check again for you tomorrow!");
        }


        //garbage, need to update
        Bundle bundle = i.getExtras();
        String title = bundle.getString("title");
        String message = bundle.getString("message");

        Log.d("superdopetag", "receiver got intent");
        Log.d("superdopetag", formattedToday);
        Log.d("superdopetag", allAssessments.get(0).getStartDate());

//        Notifications.makeNotification(context, "title", "message");
//        Notifications.makeNotification(context, "title", "message2:the sequel");
    }
}
