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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.Adapters.InstructorsRecyclerviewAdapter;
import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.MainActivity;
import com.example.termtracker.Misc.DateTools;
import com.example.termtracker.Listeners.OnInstructorDeleteButtonPressedListener;
import com.example.termtracker.Model.CanBeAddedToDatabase;
import com.example.termtracker.Model.Course;
import com.example.termtracker.Model.CourseInstructor;
import com.example.termtracker.Model.Term;
import com.example.termtracker.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditCourseDialogFragment extends DialogFragment implements View.OnClickListener, InstructorDetailsDialogFragment.InstructorDetailsDialogFragmentListener, CanBeAddedToDatabase {
    private Course courseToModify;

    private DatabaseHelper helper;
    public ArrayList<CourseInstructor> instructors;

    public Button save, cancel, addInstructorButton;
    public EditText title, start, end;
    public Spinner term;
    public RecyclerView instructorsRv;

    public InstructorsRecyclerviewAdapter adapter;

    @Override
    public void onClick(View v) {

        String parsedStartDate = start.getText().toString().replaceAll("[^0-9.]", "");
        String parsedEndDate = end.getText().toString().replaceAll("[^0-9.]", "");

        courseToModify.setTitle(title.getText().toString());
        courseToModify.setStartDate(parsedStartDate);
        courseToModify.setEndDate(parsedEndDate);
        courseToModify.setTermId(helper.getTermByName(term.getSelectedItem().toString()).getId());

        if (courseToModify.isValidEdit(getContext()) && v.getId() == R.id.edit_course_save) {
            addNewItem();
//            Toast.makeText(getContext(), "Modifications saved.", Toast.LENGTH_SHORT).show();
//
//            dismiss();
//            Intent intent = new Intent(v.getContext(), MainActivity.class);
//            startActivity(intent);
        }
        if (v.getId() == R.id.edit_course_cancel) {
            dismiss();
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

        instructors = new ArrayList<>();

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
        start.setText(DateTools.addHyphensToDate(courseToModify.getStartDate()));
        end.setText(DateTools.addHyphensToDate(courseToModify.getEndDate()));
        term.setSelection(0); //selects the duplicate string from earlier.

        //sets up the list of existing instructors
        List<CourseInstructor> allInstructors = helper.getAllInstructors();
        for (CourseInstructor courseInstructor: allInstructors) {
            if (courseInstructor.getCourseId() == courseToModify.getId()) {
                instructors.add(courseInstructor);
            };
        }

        adapter = new InstructorsRecyclerviewAdapter(instructors, new OnInstructorDeleteButtonPressedListener() {
            @Override
            public void onInstructorDeleteClicked(CourseInstructor instructor, int position) {
                instructors.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        instructorsRv.setAdapter(adapter);
        instructorsRv.setLayoutManager(new LinearLayoutManager(view.getContext()));

        save.setClickable(true);
        save.setVisibility(View.VISIBLE);
        cancel.setClickable(true);
        cancel.setVisibility(View.VISIBLE);

        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onSaveInstructor(String newName, String newPhone, String newEmail) {
        //add new frag item
        CourseInstructor courseInstructor = new CourseInstructor(-1, newName, newPhone, newEmail, -1);
        instructors.add(courseInstructor);
        Toast.makeText(getActivity(), "Course Instructor " + newName + " added.", Toast.LENGTH_SHORT).show();
        adapter.notifyItemInserted(instructors.size() - 1);
    }

    @Override
    public void addNewItem() {
        View view = getView();
        EditText titleEditText = (EditText) view.findViewById(R.id.course_title);
        EditText startEditText = (EditText) view.findViewById(R.id.course_start_date);
        EditText endEditText = (EditText) view.findViewById(R.id.course_end_date);
        Spinner termSpinner = (Spinner) view.findViewById(R.id.term_spinner);

        String parsedStartDate = startEditText.getText().toString().replaceAll("[^0-9.]", "");
        String parsedEndDate = endEditText.getText().toString().replaceAll("[^0-9.]", "");

        DatabaseHelper helper = new DatabaseHelper(view.getContext());
        Term term = helper.getTermByName(termSpinner.getSelectedItem().toString());

        Course courseToEdit = new Course(courseToModify.getId(),
                titleEditText.getText().toString(),
                parsedStartDate,
                parsedEndDate,
                false,
                true,
                term.getId());
        if (courseToEdit.isValidEdit(getContext())) {
            long idReturned = helper.updateCourse(courseToEdit);
            if (idReturned < 0) {
                Toast.makeText(view.getContext(), "Something went wrong with the query.  Course not modified.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view.getContext(), "Course modified.  ID: " + idReturned, Toast.LENGTH_SHORT).show();


                //delete old copies of instructors
                List<CourseInstructor> allInstructors = helper.getAllInstructors();
                for (CourseInstructor courseInstructor: allInstructors) {
                    if (courseInstructor.getCourseId() == courseToEdit.getId()) {
                        helper.deleteInstructor(courseInstructor);
                    }
                }

                //add course instructors
                for (CourseInstructor instructor: instructors) {
                    instructor.setCourseId(idReturned);
                    long instructorIdReturned = helper.addInstructor(instructor);
                    if (instructorIdReturned < 0) {
                        Toast.makeText(view.getContext(), "Something went wrong with the query.  Instructor not modified.", Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(view.getContext(), "Instructor added to CourseID: " + idReturned + ".  Instructor ID: " + instructorIdReturned, Toast.LENGTH_SHORT).show();
                    }
                }
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }

}
