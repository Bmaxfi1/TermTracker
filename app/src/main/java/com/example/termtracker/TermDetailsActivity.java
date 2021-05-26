package com.example.termtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.termtracker.Adapters.CoursesRecyclerViewAdapter;
import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.Misc.ConfirmationDialogFragment;
import com.example.termtracker.Misc.OnCourseClickListener;
import com.example.termtracker.Model.Assessment;
import com.example.termtracker.Model.Course;
import com.example.termtracker.Model.Term;

import java.util.ArrayList;
import java.util.List;

public class TermDetailsActivity extends AppCompatActivity implements ConfirmationDialogFragment.ConfirmationDialogFragmentListener {

    Term term;

    TextView termTitle;
    TextView termStart;
    TextView termEnd;
    TextView coursesLeftLabel;
    TextView coursesLeft;
    ImageView star;

    RecyclerView coursesRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);

        termTitle = (TextView) findViewById(R.id.term_details_title);
        termStart = (TextView) findViewById(R.id.term_details_start);
        termEnd = (TextView) findViewById(R.id.term_details_end);
        coursesLeft = (TextView) findViewById(R.id.term_details_courses_left);
        coursesLeftLabel = (TextView) findViewById(R.id.term_details_courses_left_label);
        star = (ImageView) findViewById(R.id.term_details_star);

        coursesRv = (RecyclerView)  findViewById(R.id.term_details_courses_rv);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras(); //get the key value pairs
        if (bundle != null) {
            int value = bundle.getInt("termId");

            DatabaseHelper helper = new DatabaseHelper(this);
            term = helper.getTermById(value);
        }

        DatabaseHelper helper = new DatabaseHelper(this);

        termTitle.setText(term.getTitle());
        termStart.setText(term.getStartDate());
        termEnd.setText(term.getEndDate());

        checkCompletionStatusAndUpdateForm(term);

        //Recyclerview
        List<Course> allCourses = helper.getAllCourses();
        List<Course> coursesInThisTerm = new ArrayList<>();
        for (Course course: allCourses) {
            if (course.getTermId() == term.getId()) {
                coursesInThisTerm.add(course);
            }
        }
        coursesRv.setAdapter(new CoursesRecyclerViewAdapter(coursesInThisTerm, new OnCourseClickListener() {
            @Override
            public void onItemClick(Course course) {
                Intent intent = new Intent(getBaseContext(), CourseDetailsActivity.class);
                intent.putExtra("courseId", course.getId());
                startActivity(intent);
            }
        }));
        coursesRv.setLayoutManager(new LinearLayoutManager(this));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_or_delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.appbar_edit) {
            Toast.makeText(getApplicationContext(), "Edit", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.appbar_delete) {
            Toast.makeText(getApplicationContext(), "Delete", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfirmDialogResolved(boolean result) {

    }

    public void checkCompletionStatusAndUpdateForm(Term term) {
        if (term.isCompleted()) {
            star.setImageResource(R.drawable.ic_baseline_star_24);
            coursesLeftLabel.setText("No courses remain!");
            coursesLeft.setText("");
        } else {
            star.setImageResource(R.drawable.ic_baseline_star_border_24);
            DatabaseHelper helper = new DatabaseHelper(this);
            List<Course> allCourses = helper.getAllCourses();
            int numOfCourses = 0;
            for (Course course: allCourses) {
                if (course.getTermId() == term.getId() && !course.isCompleted()) {
                    numOfCourses++;
                }
            }
            coursesLeft.setText(String.valueOf(numOfCourses));
        }
    }

}