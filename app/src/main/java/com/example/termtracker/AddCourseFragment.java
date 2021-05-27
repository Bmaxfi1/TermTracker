package com.example.termtracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.Misc.ImplementDatePickerDialog;
import com.example.termtracker.Misc.InstructorDetailsDialogFragment;
import com.example.termtracker.Model.CanBeAddedToDatabase;
import com.example.termtracker.Model.Course;
import com.example.termtracker.Model.CourseInstructor;
import com.example.termtracker.Model.Term;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.termtracker.Adapters.InstructorsRecyclerviewAdapter;

public class AddCourseFragment extends Fragment implements CanBeAddedToDatabase, InstructorDetailsDialogFragment.InstructorDetailsDialogFragmentListener {

    ArrayList<CourseInstructor> courseInstructors;
    RecyclerView rvCourseInstructors;
    InstructorsRecyclerviewAdapter adapter;


    public AddCourseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_course, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //date picker dialog setup
        EditText startEditText = (EditText) view.findViewById(R.id.course_start_date);
        ImplementDatePickerDialog.assign(view, startEditText);

        EditText endEditText = (EditText) view.findViewById(R.id.course_end_date);
        ImplementDatePickerDialog.assign(view, endEditText);

        //term spinner/drop down
        Spinner termSpinner = view.findViewById(R.id.term_spinner);
        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
        List<Term> allTerms = databaseHelper.getAllTerms();
        List<String> allTermsAsString = new ArrayList<>();  //need to turn object list into string list
        for (Term term: allTerms) {
            allTermsAsString.add(term.getTitle());
        }
        Collections.reverse(allTermsAsString); //this is done to show the most recent term at the top

        ArrayAdapter<String> termArrayAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.drop_down_item, R.id.drop_down_item_textview, allTermsAsString);
        termSpinner.setAdapter(termArrayAdapter);

        //recyclerview stuff
        courseInstructors = new ArrayList<CourseInstructor>();
        rvCourseInstructors = (RecyclerView) view.findViewById(R.id.instructorsRecyclerView);
        adapter = new InstructorsRecyclerviewAdapter(courseInstructors);
        rvCourseInstructors.setAdapter(adapter);
        rvCourseInstructors.setLayoutManager(new LinearLayoutManager(view.getContext()));

        //add new instructor button pressed/Dialog stuff
        Button addInstructorButton = (Button) view.findViewById(R.id.add_new_instructor_button);
        addInstructorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewInstructorDialog();
            }
        });
    }

    private void showNewInstructorDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        InstructorDetailsDialogFragment instructorDetailsDialogFragment = InstructorDetailsDialogFragment.newInstance("New Instructor");
        instructorDetailsDialogFragment.setTargetFragment(AddCourseFragment.this, 300); //used by the dialog to reference this "parent" fragment.
        instructorDetailsDialogFragment.show(fm, "instructor_details_dialog");
    }

    @Override
    public void onSaveInstructor(String newName, String newPhone, String newEmail) {
        //add new frag item
        CourseInstructor courseInstructor = new CourseInstructor(-1, newName, newPhone, newEmail, -1);
        Log.d("superdopetag", courseInstructor.getName());
        courseInstructors.add(courseInstructor);
        Log.d("superdopetag", courseInstructors.get(0).getName());
        Toast.makeText(getActivity(), "Course Instructor " + newName + " added.", Toast.LENGTH_SHORT).show();
        adapter.notifyItemInserted(0);
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

        Course courseToAdd = new Course(-1,
                titleEditText.getText().toString(),
                parsedStartDate,
                parsedEndDate,
                false,
                true,
                term.getId());
        if (courseToAdd.isValid(getContext())) {
            long idReturned = helper.addCourse(courseToAdd);
            if (idReturned < 0) {
                Toast.makeText(view.getContext(), "Something went wrong with the query.  Course not added.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view.getContext(), "Course added.  ID: " + idReturned, Toast.LENGTH_SHORT).show();


                //add course instructors
                for (CourseInstructor instructor: courseInstructors) {
                    instructor.setCourseId(idReturned);
                    long instructorIdReturned = helper.addInstructor(instructor);
                    if (instructorIdReturned < 0) {
                        Toast.makeText(view.getContext(), "Something went wrong with the query.  Instructor not added.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(view.getContext(), "Instructor added to CourseID: " + idReturned + ".  Instructor ID: " + instructorIdReturned, Toast.LENGTH_SHORT).show();
                    }


                }

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        }
    }
}