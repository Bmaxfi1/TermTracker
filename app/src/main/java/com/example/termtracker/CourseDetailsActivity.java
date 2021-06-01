package com.example.termtracker;

import androidx.annotation.Nullable;
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

import com.example.termtracker.Adapters.AssessmentsRecyclerviewAdapter;
import com.example.termtracker.Adapters.InstructorsRecyclerviewAdapterReadOnly;
import com.example.termtracker.Adapters.NotesRecyclerViewAdapter;
import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.Dialogs.EditCourseDialogFragment;
import com.example.termtracker.Dialogs.ConfirmationDialogFragment;
import com.example.termtracker.Dialogs.ShareNotesDialogFragment;
import com.example.termtracker.Misc.DateTools;
import com.example.termtracker.Listeners.OnAssessmentClickListener;
import com.example.termtracker.Listeners.OnNoteClickListener;
import com.example.termtracker.Model.Assessment;
import com.example.termtracker.Model.Course;
import com.example.termtracker.Model.CourseInstructor;
import com.example.termtracker.Model.Note;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailsActivity extends AppCompatActivity implements ConfirmationDialogFragment.ConfirmationDialogFragmentListener {

    Course course;

    TextView courseTitle;
    TextView courseStart;
    TextView courseEnd;
    TextView courseTerm;
    TextView courseAssessmentsLeft;
    TextView courseAssessmentsLeftLabel;
    ImageView star;

    RecyclerView instructorsRv;
    RecyclerView notesRv;
    RecyclerView assessmentsRv;

    //on note back pressed
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                int courseId = bundle.getInt("courseId");

                DatabaseHelper helper = new DatabaseHelper(this);
                course = helper.getCourseById(courseId);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        courseTitle = (TextView) findViewById(R.id.term_details_title);
        courseStart = (TextView) findViewById(R.id.term_details_start);
        courseEnd = (TextView) findViewById(R.id.term_details_end);
        courseTerm = (TextView) findViewById(R.id.course_details_term_title);
        courseAssessmentsLeft = (TextView) findViewById(R.id.term_details_courses_left);
        courseAssessmentsLeftLabel = (TextView) findViewById(R.id.course_details_assessments_left_label);
        star = (ImageView) findViewById(R.id.course_details_star);

        instructorsRv = (RecyclerView) findViewById(R.id.term_details_courses_rv) ;
        notesRv = (RecyclerView) findViewById(R.id.course_details_notes_rv) ;
        assessmentsRv = (RecyclerView) findViewById(R.id.course_details_assessments_rv);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras(); //get the key value pairs from the intent
        if (bundle != null) {
            int value = bundle.getInt("courseId");

            DatabaseHelper helper = new DatabaseHelper(this);
            course = helper.getCourseById(value);
        }

        DatabaseHelper helper = new DatabaseHelper(this);

        courseTitle.setText(course.getTitle());
        courseStart.setText(DateTools.addHyphensToDate(course.getStartDate()));
        courseEnd.setText(DateTools.addHyphensToDate(course.getEndDate()));
        courseTerm.setText(helper.getTermById(course.getTermId()).getTitle());

        checkCompletionStatusAndUpdateForm(course);

        //RecyclerView stuff
        List<CourseInstructor> allInstructors = helper.getAllInstructors();
        List<CourseInstructor> allInstructorsInCourse = new ArrayList<>();
        for (CourseInstructor instructor: allInstructors) {
            if (instructor.getCourseId() == course.getId()) {
                allInstructorsInCourse.add(instructor);
            }
        }
        instructorsRv.setAdapter(new InstructorsRecyclerviewAdapterReadOnly(allInstructorsInCourse));
        instructorsRv.setLayoutManager(new LinearLayoutManager(this));

        List<Note> allNotes = helper.getAllNotes();
        List<Note> allNotesInCourse = new ArrayList<>();
        for (Note note: allNotes) {
            if (note.getCourseId() == course.getId()) {
                allNotesInCourse.add(note);
            }
        }
        notesRv.setAdapter(new NotesRecyclerViewAdapter(allNotesInCourse, new OnNoteClickListener() {
            @Override
            public void onItemClick(Note note) {
                Toast.makeText(getBaseContext(), "Note Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), NoteDetailsActivity.class);
                intent.putExtra("noteId", note.getId());
                startActivityForResult(intent, 1);


            }
        }));
        notesRv.setLayoutManager(new LinearLayoutManager(this));

        List<Assessment> allAssessments = helper.getAllAssessments();
        List<Assessment> assessmentsInCourse = new ArrayList<>();
        for (Assessment assessment: allAssessments) {
            if (assessment.getCourseId() == course.getId()) {
                assessmentsInCourse.add(assessment);
            }
        }
        assessmentsRv.setAdapter(new AssessmentsRecyclerviewAdapter(assessmentsInCourse, new OnAssessmentClickListener() {
            @Override
            public void onItemClick(Assessment assessment) {
                Intent intent = new Intent(getBaseContext(), AssessmentDetailsActivity.class);
                intent.putExtra("assessmentId", assessment.getId());
                startActivity(intent);
            }
        }));
        assessmentsRv.setLayoutManager(new LinearLayoutManager(this));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_delete_or_share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.appbar_edit) {
            showNewEditDialog();
            return true;
        }
        if (id == R.id.appbar_delete) {
            showNewConfirmationDialog();
            return true;
        }
        if (id == R.id.appbar_share) {
            showNewShareNotesDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkCompletionStatusAndUpdateForm(Course course) {
        if (course.isCompleted()) {
            star.setImageResource(R.drawable.ic_baseline_star_24);
            courseAssessmentsLeftLabel.setText("No assessments remain!");
            courseAssessmentsLeft.setText("");
        } else {
            star.setImageResource(R.drawable.ic_baseline_star_border_24);
            DatabaseHelper helper = new DatabaseHelper(this);
            List<Assessment> allAssessments = helper.getAllAssessments();
            int numOfAssessments = 0;
            boolean noAssessmentsInCourse = true;
            for (Assessment assessment: allAssessments) {
                if (assessment.getCourseId() == course.getId()) {
                    noAssessmentsInCourse = false;
                    if (!assessment.isCompleted()) {
                        numOfAssessments++;
                    }
                }
            }
            if (noAssessmentsInCourse) {
                courseAssessmentsLeft.setText("N/A");
            } else {
                courseAssessmentsLeft.setText(String.valueOf(numOfAssessments));
            }
        }
    }

    public void showNewEditDialog() {
        FragmentManager fm = this.getSupportFragmentManager();
        EditCourseDialogFragment editCourseDialogFragment = EditCourseDialogFragment.newInstance(String.valueOf(course.getId()));
        editCourseDialogFragment.show(fm, "edit_course_dialog"); //todo what is this tag for??
    }

    public void showNewConfirmationDialog() {
        FragmentManager fm = this.getSupportFragmentManager();
        ConfirmationDialogFragment confirmationDialogFragment = ConfirmationDialogFragment.newInstance("Are you sure you want to delete this course and all associated instructors, assessments, and notes?", "Confirm", "Cancel");
        confirmationDialogFragment.show(fm, "edit_course_dialog");  //todo what tag should go here, and for what purpose?
    }

    @Override
    public void onConfirmDialogResolved(boolean result) {
        if (result) {
            DatabaseHelper helper = new DatabaseHelper(this);
            helper.deleteCourse(course);
            Toast.makeText(this, "Course '" + course.getTitle() + "' deleted.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void showNewShareNotesDialog() {
        Toast.makeText(getApplicationContext(), "share", Toast.LENGTH_SHORT).show();

        DatabaseHelper helper = new DatabaseHelper(this);
        List<Note> allNotes = helper.getAllNotes();
        StringBuilder allNotesInThisCourseAsString = new StringBuilder();
        for (Note note: allNotes) {
            if (note.getCourseId() == course.getId()) {
                allNotesInThisCourseAsString.append(note.getTitle()).append("\n").append(note.getContent()).append("\n\n");
            }
        }


        FragmentManager fm = this.getSupportFragmentManager();
        ShareNotesDialogFragment shareNotesDialogFragment = ShareNotesDialogFragment.newInstance(
                "Enter the email of the person you would like to send all notes for course '" +
                        course.getTitle() +"' to.", allNotesInThisCourseAsString.toString());
        shareNotesDialogFragment.show(fm, "share_notes_dialog");//todo unnecessary?
    }
}