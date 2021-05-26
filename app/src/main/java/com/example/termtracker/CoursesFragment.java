package com.example.termtracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.termtracker.Adapters.CoursesRecyclerViewAdapter;
import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.Misc.OnCourseClickListener;
import com.example.termtracker.Model.Course;

import java.util.ArrayList;
import java.util.List;

public class CoursesFragment extends Fragment {

    List<Course> courses;
    RecyclerView completeCoursesRv;
    RecyclerView incompleteCoursesRv;
    CoursesRecyclerViewAdapter incompleteAdapter;
    CoursesRecyclerViewAdapter completeAdapter;

    public CoursesFragment() {
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
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //recyclerview
        DatabaseHelper helper = new DatabaseHelper(view.getContext());
        courses = helper.getAllCourses();

        List<Course> incompleteCourses = new ArrayList<>();
        List<Course> completeCourses = new ArrayList<>();

        for (Course course: courses) {
            boolean complete = course.isCompleted();
            if (!complete) {
                incompleteCourses.add(course);
            } else {
                completeCourses.add(course);
            }
        }

        incompleteCoursesRv = (RecyclerView) view.findViewById(R.id.home_incomplete_courses_rv);
        incompleteCoursesRv.setAdapter(new CoursesRecyclerViewAdapter(incompleteCourses, new OnCourseClickListener() {
            @Override
            public void onItemClick(Course course) {
                Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_SHORT).show();
                launchCourseDetails(course);
            }
        }));
        incompleteCoursesRv.setLayoutManager(new LinearLayoutManager(view.getContext()));

        completeCoursesRv = (RecyclerView) view.findViewById(R.id.home_complete_courses_rv);
        completeCoursesRv.setAdapter(new CoursesRecyclerViewAdapter(completeCourses, new OnCourseClickListener() {
            @Override
            public void onItemClick(Course course) {
                Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_SHORT).show();
                launchCourseDetails(course);
            }
        }));
        completeCoursesRv.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void launchCourseDetails(Course course) {
        Intent intent = new Intent(getContext(), CourseDetailsActivity.class);
        intent.putExtra("courseId", course.getId());
        startActivity(intent);
    }
}