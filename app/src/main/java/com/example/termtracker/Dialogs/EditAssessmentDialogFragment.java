package com.example.termtracker.Dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.MainActivity;
import com.example.termtracker.Misc.DateTools;
import com.example.termtracker.Model.Assessment;
import com.example.termtracker.Model.AssessmentType;
import com.example.termtracker.Model.Course;
import com.example.termtracker.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditAssessmentDialogFragment extends DialogFragment implements View.OnClickListener {

    private Assessment assessmentToModify;

    private DatabaseHelper helper;

    public Button save, cancel;
    public EditText title, start, end;
    public Spinner type;
    public Spinner coursePicker;

    @Override
    public void onClick(View v) {

        //database items are stored as a raw string of only integers
        String parsedStartDate = start.getText().toString().replaceAll("[^0-9.]", "");
        String parsedEndDate = end.getText().toString().replaceAll("[^0-9.]", "");

        assessmentToModify.setTitle(title.getText().toString());
        assessmentToModify.setStartDate(parsedStartDate);
        assessmentToModify.setEndDate(parsedEndDate);
        assessmentToModify.setAssessmentType(AssessmentType.get(type.getSelectedItem().toString()));
        assessmentToModify.setCourseId(helper.getCourseByName(coursePicker.getSelectedItem().toString()).getId());

        if (assessmentToModify.isValid(getContext()) && v.getId() == R.id.assessment_save_button) {
            long idReturned = helper.updateAssessment(assessmentToModify);
            Toast.makeText(getContext(), "Modifications to assessment '" + assessmentToModify.getTitle() + "' saved.", Toast.LENGTH_SHORT).show();




//          todo do I actually need this? probably gonna just delete it after testing.

//            EditAssessmentDialogFragment.EditAssessmentDialogFragmentListener listener = new EditAssessmentDialogFragmentListener() {
//                @Override
//                public void onSaveAssessment(String newTitle, String newType, String newStart, String newEnd, String courseId) {
//
//                }
//            listener.onSaveAssessment(title.getText().toString(), type.getSelectedItem().toString(), start.getText().toString(), end.getText().toString(), coursePicker.getSelectedItem().toString() );
//            };
//
            dismiss();
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            startActivity(intent);

        }
        if (v.getId() == R.id.assessment_cancel_button) {
            dismiss();
        }

    }
//
//    public interface EditAssessmentDialogFragmentListener{
//        void onSaveAssessment(String newTitle, String newType, String newStart, String newEnd, String courseId);
//    }

    public EditAssessmentDialogFragment() {
        // Empty constructor is required for DialogFragment
    } //if you need to pass something in, use 'newInstance'

    public static EditAssessmentDialogFragment newInstance(String id) {
        EditAssessmentDialogFragment dialogFragment = new EditAssessmentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_assessment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        title = (EditText) view.findViewById(R.id.assessment_title);
        save = (Button) view.findViewById(R.id.assessment_save_button);
        cancel = (Button) view.findViewById(R.id.assessment_cancel_button);
        start = (EditText) view.findViewById(R.id.assessment_start);
        end = (EditText) view.findViewById(R.id.assessment_end);
        type = (Spinner) view.findViewById(R.id.assessment_type_spinner);
        coursePicker = (Spinner) view.findViewById(R.id.assessment_course_spinner);

        //get our database manager object
        helper = new DatabaseHelper(view.getContext());

        //get the assessment we are changing
        int id = Integer.parseInt(getArguments().getString("id"));
        assessmentToModify = helper.getAssessmentById(id);

        //date picker dialog setup
        ImplementDatePickerDialog.assign(view, start);
        ImplementDatePickerDialog.assign(view, end);

        //Course spinner/dropdown menu
        List<Course> allCourses = helper.getAllCourses();
        List<String> allCoursesAsString = new ArrayList<>();
        for (Course course: allCourses) {
            allCoursesAsString.add(course.getTitle());
        }
        Collections.reverse(allCoursesAsString); //this is done to show the most recent course at the top
        allCoursesAsString.add(0 ,helper.getCourseById((int) assessmentToModify.getCourseId()).getTitle()); //this adds a duplicate course name at the top of the list.  this allows the spinner to select the current item, and should not cause trouble.

        ArrayAdapter<String> courseArrayAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.drop_down_item, R.id.drop_down_item_textview, allCoursesAsString);
        coursePicker.setAdapter(courseArrayAdapter);

        //Type spinner/drop down menu
        List<String> typesAsStrings = new ArrayList<>();

        for (AssessmentType type: AssessmentType.values()) {
            typesAsStrings.add(type.toString());
        }
        ArrayAdapter<String> typeArrayAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.drop_down_item, R.id.drop_down_item_textview, typesAsStrings);
        type.setAdapter(typeArrayAdapter);

        //set up the fields default values
        title.setText(assessmentToModify.getTitle());
        start.setText(DateTools.addHyphensToDate(assessmentToModify.getStartDate()));
        end.setText(DateTools.addHyphensToDate(assessmentToModify.getEndDate()));
        type.setSelection(assessmentToModify.getAssessmentType().ordinal());
        coursePicker.setSelection(0); //gets the duplicate course name from earlier.

        //enable buttons
        save.setClickable(true);
        save.setVisibility(View.VISIBLE);
        cancel.setClickable(true);
        cancel.setVisibility(View.VISIBLE);

        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }


}
