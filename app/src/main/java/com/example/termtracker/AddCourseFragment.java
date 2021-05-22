package com.example.termtracker;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.termtracker.Misc.ImplementDatePickerDialog;
import com.example.termtracker.Misc.InstructorDetailsDialogFragment;
import com.example.termtracker.Model.CanBeAddedToDatabase;
import com.example.termtracker.Model.CourseInstructor;

import java.util.ArrayList;

import Adapters.InstructorsRecyclerviewAdapter;

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

        //recyclerview stuff
        courseInstructors = new ArrayList<CourseInstructor>();
        rvCourseInstructors = (RecyclerView) view.findViewById(R.id.instructorsRecyclerView);
        adapter = new InstructorsRecyclerviewAdapter(courseInstructors);
        rvCourseInstructors.setAdapter(adapter);
        rvCourseInstructors.setLayoutManager(new LinearLayoutManager(view.getContext()));



        //add new instructor button pressed/Dialog stuff
//        InstructorDetailsDialog dialog = new InstructorDetailsDialog(getActivity());
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
        CourseInstructor courseInstructor = new CourseInstructor(newName, newPhone, newEmail);
        Log.d("superdopetag", courseInstructor.getName());
        courseInstructors.add(courseInstructor);
        Log.d("superdopetag", courseInstructors.get(0).getName());
        Toast.makeText(getActivity(), "Course Instructor " + newName + "added.", Toast.LENGTH_SHORT).show();
        adapter.notifyItemInserted(0);
    }

    @Override
    public void addNewItem() {
        //todo
    }
}