package com.example.termtracker;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.Adapters.InstructorsRecyclerviewAdapter;
import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.Misc.ImplementDatePickerDialog;
import com.example.termtracker.Model.Course;
import com.example.termtracker.Model.CourseInstructor;
import com.example.termtracker.Model.Term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditCourseDialogFragment extends DialogFragment implements View.OnClickListener {
    private Course courseToModify;

    private DatabaseHelper helper;
    public ArrayList<CourseInstructor> instructors;

    public Button save, cancel, addInstructorButton;
    public EditText title, start, end;
    public Spinner term;
    public RecyclerView instructorsRv;

    @Override
    public void onClick(View v) {
        String parsedStartDate = start.getText().toString().replaceAll("[^0-9.]", "");
        String parsedEndDate = end.getText().toString().replaceAll("[^0-9.]", "");

        courseToModify.setTitle(title.getText().toString());
        courseToModify.setStartDate(parsedStartDate);
        courseToModify.setEndDate(parsedEndDate);
        courseToModify.setTermId(helper.getTermByName(term.getSelectedItem().toString()).getId());

        if (courseToModify.isValidEdit(getContext())) {
            long idReturned = helper.updateCourse(courseToModify);
            Toast.makeText(getContext(), "Modifications to Course #" + idReturned + " saved.", Toast.LENGTH_SHORT).show();

            dismiss();
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    public EditCourseDialogFragment() {
        //leave this empty
    }

    public static EditCourseDialogFragment newInstance(String id) {
        EditCourseDialogFragment dialogFragment = new EditCourseDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_course, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = (EditText) view.findViewById(R.id.course_title);
        start = (EditText) view.findViewById(R.id.course_start_date);
        end = (EditText) view.findViewById(R.id.course_end_date);
        term = (Spinner) view.findViewById(R.id.term_spinner);
        instructorsRv = (RecyclerView) view.findViewById(R.id.instructorsRecyclerView);
        addInstructorButton = (Button) view.findViewById(R.id.add_new_instructor_button);
        save = (Button) view.findViewById(R.id.edit_course_save);
        cancel = (Button) view.findViewById(R.id.edit_course_cancel);

        helper = new DatabaseHelper(view.getContext());

        int id = Integer.parseInt(getArguments().getString("id"));
        courseToModify = helper.getCourseById(id);

        ImplementDatePickerDialog.assign(view, start);
        ImplementDatePickerDialog.assign(view, end);

        List<Term> allTerms = helper.getAllTerms();
        ArrayList<String> allTermsAsStrings = new ArrayList<>();

        for (Term term: allTerms) {
            allTermsAsStrings.add(term.getTitle());
        }
        Collections.reverse(allTermsAsStrings); //shows newer terms towards the top
        allTermsAsStrings.add(0, helper.getTermById((int) courseToModify.getTermId()).getTitle()); //adds a duplicate term name to the dropdown, for showing the existing term in the spinner by default.

        ArrayAdapter<String> termArrayAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.drop_down_item, R.id.drop_down_item_textview, allTermsAsStrings);
        term.setAdapter(termArrayAdapter);

        title.setText(courseToModify.getTitle());
        start.setText(courseToModify.getStartDate());
        end.setText(courseToModify.getEndDate());
        term.setSelection(0); //selects the duplicate string from earlier.
        // todo instructorsRv.setAdapter(InstructorsRecyclerviewAdapter);

        save.setClickable(true);
        save.setVisibility(View.VISIBLE);
        cancel.setClickable(true);
        cancel.setVisibility(View.VISIBLE);

        save.setOnClickListener(this);

    }
}
