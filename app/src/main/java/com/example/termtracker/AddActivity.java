package com.example.termtracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.Model.Course;
import com.example.termtracker.Model.Term;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    Boolean noTermsYet = false;
    Boolean noCoursesYet = false;

    Button note;
    Button assessment;
    Button course;
    Button term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();

        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        note = (Button) findViewById(R.id.add_note_button);
        assessment = (Button) findViewById(R.id.add_assessment_button);
        course = (Button) findViewById(R.id.add_course_button);
        term = (Button) findViewById(R.id.add_term_button);

        DatabaseHelper helper = new DatabaseHelper(this);

        List<Term> allTerms = helper.getAllTerms();
        if (allTerms.size() == 0) {
            noTermsYet = true;
        }
        List<Course> allCourses = helper.getAllCourses();
        if (allCourses.size() == 0) {
            noCoursesYet = true;
        }
    }


    public void launchAddActivity(View view) {
        if (view == findViewById(R.id.add_note_button) && !noCoursesYet) {
            Intent intent = new Intent(this, AddDetailsActivity.class);
            intent.putExtra("type", "note");
            startActivity(intent);
        } else if (view == findViewById(R.id.add_assessment_button) && !noCoursesYet) {
            Intent intent = new Intent(this, AddDetailsActivity.class);
            intent.putExtra("type", "assessment");
            startActivity(intent);
        } else if (view == findViewById(R.id.add_course_button) && !noTermsYet) {
            Intent intent = new Intent(this, AddDetailsActivity.class);
            intent.putExtra("type", "course");
            startActivity(intent);
        } else if (view == findViewById(R.id.add_term_button)) {
            Intent intent = new Intent(this, AddDetailsActivity.class);
            intent.putExtra("type", "term");
            startActivity(intent);
        } else if (noCoursesYet && noTermsYet) {
            Toast.makeText(this, "You'll need to add a term first!", Toast.LENGTH_SHORT).show();
        } else if (noCoursesYet) {
            Toast.makeText(this, "You'll need to add a course first!", Toast.LENGTH_SHORT).show();
        }
    }

}