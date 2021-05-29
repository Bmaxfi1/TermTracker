package com.example.termtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
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
import com.example.termtracker.Dialogs.EditTermDialogFragment;
import com.example.termtracker.Dialogs.ConfirmationDialogFragment;
import com.example.termtracker.Misc.DateTools;
import com.example.termtracker.Listeners.OnCourseClickListener;
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
        termStart.setText(DateTools.addHyphensToDate(term.getStartDate()));
        termEnd.setText(DateTools.addHyphensToDate(term.getEndDate()));

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
            showNewEditDialog();
            return true;
        }
        if (id == R.id.appbar_delete) {
            if (coursesRv.getChildAt(0) == null) {
                showNewConfirmationDialog();
            } else {
                Toast.makeText(getApplicationContext(), "For your safety, terms may not be deleted unless all associated courses are deleted first.", Toast.LENGTH_LONG).show();
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void showNewConfirmationDialog() {
        FragmentManager fm = this.getSupportFragmentManager();
        ConfirmationDialogFragment confirmationDialogFragment = ConfirmationDialogFragment.newInstance("Are you sure you want to delete this term?", "Confirm", "Cancel");
        confirmationDialogFragment.show(fm, "term_delete_dialog");  //todo is this right?  not sure why I need this tag.
    }

    @Override
    public void onConfirmDialogResolved(boolean result) {

        if (result) {
            DatabaseHelper helper = new DatabaseHelper(this);
            helper.deleteTerm(term);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void showNewEditDialog() {
        FragmentManager fm = this.getSupportFragmentManager();
        EditTermDialogFragment editTermDialogFragment = EditTermDialogFragment.newInstance(String.valueOf(term.getId()));
        editTermDialogFragment.show(fm, "edit_term_dialog");  //todo not sure what the tag is for.
    }
}