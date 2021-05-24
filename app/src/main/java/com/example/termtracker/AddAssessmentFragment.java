package com.example.termtracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.Misc.ImplementDatePickerDialog;
import com.example.termtracker.Model.Assessment;
import com.example.termtracker.Model.AssessmentType;
import com.example.termtracker.Model.CanBeAddedToDatabase;
import com.example.termtracker.Model.Course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddAssessmentFragment extends Fragment implements CanBeAddedToDatabase {
    public AddAssessmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_assessment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //date picker dialog setup
        EditText startEditText = (EditText) view.findViewById(R.id.assessment_start);
        ImplementDatePickerDialog.assign(view, startEditText);

        EditText endEditText = (EditText) view.findViewById(R.id.assessment_end);
        ImplementDatePickerDialog.assign(view, endEditText);

        //Course spinner/dropdown menu
        Spinner courseSpinner = view.findViewById(R.id.assessment_course_spinner);
        DatabaseHelper helper = new DatabaseHelper(view.getContext());
        List<Course> allCourses = helper.getAllCourses();
        List<String> allCoursesAsString = new ArrayList<>();
        for (Course course: allCourses) {
            allCoursesAsString.add(course.getTitle());
        }
        Collections.reverse(allCoursesAsString); //this is done to show the most recent course at the top

        ArrayAdapter<String> courseArrayAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.drop_down_item, R.id.drop_down_item_textview, allCoursesAsString);
        courseSpinner.setAdapter(courseArrayAdapter);

        //Type spinner/drop down menu
        Spinner typeSpinner = view.findViewById(R.id.assessment_type_spinner);
        List<String> typesAsStrings = new ArrayList<>();

        for (AssessmentType type: AssessmentType.values()) {
            typesAsStrings.add(type.toString());
        }
        ArrayAdapter<String> typeArrayAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.drop_down_item, R.id.drop_down_item_textview, typesAsStrings);
        typeSpinner.setAdapter(typeArrayAdapter);
    }

    @Override
    public void addNewItem() {
        View view = getView();
        EditText titleEditText = (EditText) view.findViewById(R.id.assessment_title);
        EditText startEditText = (EditText) view.findViewById(R.id.assessment_start);
        EditText endEditText = (EditText) view.findViewById(R.id.assessment_end);
        Spinner typeSpinner = (Spinner) view.findViewById(R.id.assessment_type_spinner);
        Spinner courseSpinner = (Spinner) view.findViewById(R.id.assessment_course_spinner);

        String parsedStartDate = startEditText.getText().toString().replaceAll("[^0-9.]", "");
        String parsedEndDate = endEditText.getText().toString().replaceAll("[^0-9.]", "");

        DatabaseHelper helper = new DatabaseHelper(view.getContext());
        Course course = helper.getCourseByName(courseSpinner.getSelectedItem().toString());

        AssessmentType type = AssessmentType.get(typeSpinner.getSelectedItem().toString());

        Assessment assessmentToAdd = new Assessment(-1,
                titleEditText.getText().toString(),
                parsedStartDate,
                parsedEndDate,
                false,
                true,
                type,
                course.getId());

        if (assessmentToAdd.isValid(getContext())) {
            long idReturned = helper.addAssessment(assessmentToAdd);
            if (idReturned < 0) {
                Toast.makeText(view.getContext(), "Something went wrong with the query.  Assessment not added.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view.getContext(), "Assessment added.  ID: " + idReturned, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }
}