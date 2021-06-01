package com.example.termtracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.Dialogs.ConfirmationDialogFragment;
import com.example.termtracker.Misc.DateTools;
import com.example.termtracker.Dialogs.EditAssessmentDialogFragment;
import com.example.termtracker.Model.Assessment;

public class AssessmentDetailsActivity extends AppCompatActivity implements ConfirmationDialogFragment.ConfirmationDialogFragmentListener {

    Assessment assessment;

    TextView assessmentTitle;
    TextView assessmentStart;
    TextView assessmentEnd;
    TextView assessmentType;
    TextView assessmentCourseName;
    ImageView star;
    TextView completionLabel;
    Button completeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);

        assessmentTitle = (TextView) findViewById(R.id.assessment_details_title);
        assessmentStart = (TextView) findViewById(R.id.assessment_details_start);
        assessmentEnd = (TextView) findViewById(R.id.assessment_details_end);
        assessmentType = (TextView) findViewById(R.id.assessment_details_type);
        assessmentCourseName = (TextView) findViewById(R.id.assessment_details_course_name);
        star = (ImageView) findViewById(R.id.assessment_details_star);
        completionLabel = (TextView) findViewById(R.id.assessment_complete_label);
        completeButton = (Button) findViewById(R.id.assessment_details_complete_button);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras(); //getting the passed in key/value pairs
        if (bundle != null) {
            int value = bundle.getInt("assessmentId");

            DatabaseHelper helper = new DatabaseHelper(this);
            assessment =  helper.getAssessmentById(value);
        }

        DatabaseHelper helper = new DatabaseHelper(this);

        assessmentTitle.setText(assessment.getTitle());
        assessmentStart.setText(DateTools.addHyphensToDate(assessment.getStartDate()));
        assessmentEnd.setText(DateTools.addHyphensToDate(assessment.getEndDate()));
        assessmentType.setText(assessment.getAssessmentType().toString());
        assessmentCourseName.setText(helper.getCourseById((int) assessment.getCourseId()).getTitle());

        checkCompletionStatusAndUpdateForm(assessment);

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper helper = new DatabaseHelper(v.getContext());

                if (assessment.isCompleted()) {
                    assessment.setCompleted(false);
                    long idReturned = helper.updateAssessment(assessment);
                    Toast.makeText(v.getContext(), "Assessment #" + idReturned + " updated.", Toast.LENGTH_SHORT).show();
                } else {
                    assessment.setCompleted(true);
                    long idReturned = helper.updateAssessment(assessment);
                    Toast.makeText(v.getContext(), "Assessment #" + idReturned + " updated.", Toast.LENGTH_SHORT).show();
                }
                checkCompletionStatusAndUpdateForm(assessment);
            }
        });
    }

    //overloaded default method, used to set the appbar menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_or_delete_menu, menu);
        return true;
    }

    // Overridden default method used to handle click events on appbar menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        // Display menu item's title by using a Toast.
        if (id == R.id.appbar_edit) {
            showNewEditDialog();
            return true;
        }
        if (id == R.id.appbar_delete) {
            showNewConfirmationDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkCompletionStatusAndUpdateForm(Assessment assessment) {
        if (assessment.isCompleted()) {
            star.setImageResource(R.drawable.ic_baseline_star_24);
            completeButton.setText("Wait, I'm not finished yet");
            completionLabel.setText("Complete!");
        } else {
            star.setImageResource(R.drawable.ic_baseline_star_border_24);
            completeButton.setText("Yep!");
            completionLabel.setText("Complete?");
        }
    }
    private void showNewEditDialog() {
        FragmentManager fm = this.getSupportFragmentManager();
        EditAssessmentDialogFragment editAssessmentDialogFragment = EditAssessmentDialogFragment.newInstance(String.valueOf(assessment.getId()));
        editAssessmentDialogFragment.show(fm, "edit_assessment_dialog");
    }

    private void showNewConfirmationDialog() {
        FragmentManager fm = this.getSupportFragmentManager();
        ConfirmationDialogFragment confirmationDialogFragment = ConfirmationDialogFragment.newInstance("Are you sure you want to delete this assessment?", "Confirm", "Cancel");
        confirmationDialogFragment.show(fm, "delete_assessment_dialog"); //todo see if this tag is right.  used to be "edit_assessment_dialog"
    }


    @Override
    public void onConfirmDialogResolved(boolean result) {
        if (result) {
            DatabaseHelper helper = new DatabaseHelper(this);
            helper.deleteAssessment(assessment);
            Toast.makeText(this, "Assessment '" + assessment.getTitle() + "' deleted.", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}