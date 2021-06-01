package com.example.termtracker.Fragments;

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
import com.example.termtracker.MainActivity;
import com.example.termtracker.Model.CanBeAddedToDatabase;
import com.example.termtracker.Model.Course;
import com.example.termtracker.Model.Note;
import com.example.termtracker.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddNoteFragment extends Fragment implements CanBeAddedToDatabase {

    public AddNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Course spinner/dropdown menu
        Spinner courseSpinner = view.findViewById(R.id.note_course_list_spinner);
        DatabaseHelper helper = new DatabaseHelper(view.getContext());
        List<Course> allCourses = helper.getAllCourses();
        List<String> allCoursesAsString = new ArrayList<>();
        for (Course course: allCourses) {
            allCoursesAsString.add(course.getTitle());
        }
        Collections.reverse(allCoursesAsString); //this is done to show the most recent course at the top

        ArrayAdapter<String> courseArrayAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.drop_down_item, R.id.drop_down_item_textview, allCoursesAsString);
        courseSpinner.setAdapter(courseArrayAdapter);
    }

    @Override
    public void addNewItem() {
        View view = getView();
        EditText titleEditText = (EditText) view.findViewById(R.id.note_title_edit_text);
        Spinner spinner = (Spinner) view.findViewById(R.id.note_course_list_spinner);
        EditText contentEditText = (EditText) view.findViewById(R.id.note_content_box);

        DatabaseHelper helper = new DatabaseHelper(view.getContext());
        Course course = helper.getCourseByName(spinner.getSelectedItem().toString());

        Note noteToAdd = new Note(-1, titleEditText.getText().toString(), contentEditText.getText().toString(), course.getId());

        if (noteToAdd.isValid(getContext())) {
            DatabaseHelper databaseHelper;
            databaseHelper = new DatabaseHelper(view.getContext());
            long idReturned = databaseHelper.addNote(noteToAdd);
            if (idReturned < 0) {
                Toast.makeText(view.getContext(), "Something went wrong with the query.  Note not added.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view.getContext(), "Note Added.  Title: " + noteToAdd.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        }
    }
}
