package com.example.termtracker.Fragments;

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

import com.example.termtracker.AssessmentDetailsActivity;
import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.Listeners.OnAssessmentClickListener;
import com.example.termtracker.Model.Assessment;

import java.util.ArrayList;
import java.util.List;

import com.example.termtracker.Adapters.AssessmentsRecyclerviewAdapter;
import com.example.termtracker.R;

public class AssessmentsFragment extends Fragment {

    List<Assessment> assessments;
    RecyclerView completeAssessmentsRV;
    RecyclerView incompleteAssessmentsRV;
    AssessmentsRecyclerviewAdapter incompleteAdapter;
    AssessmentsRecyclerviewAdapter completeAdapter;

    public AssessmentsFragment() {
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
        return inflater.inflate(R.layout.fragment_assessments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //recyclerview stuff
        DatabaseHelper helper = new DatabaseHelper(view.getContext());
        assessments = helper.getAllAssessments();

        List<Assessment> incompleteAssessments = new ArrayList<>();
        List<Assessment> completeAssessments = new ArrayList<>();

        for (Assessment assessment: assessments) {
            boolean complete = assessment.isCompleted();
            if (!complete) {
                incompleteAssessments.add(assessment);
            } else {
                completeAssessments.add(assessment);
            }
        }

        incompleteAssessmentsRV = (RecyclerView) view.findViewById(R.id.home_incomplete_assessments_rv);
        incompleteAssessmentsRV.setAdapter(new AssessmentsRecyclerviewAdapter(incompleteAssessments, new OnAssessmentClickListener() {
            @Override
            public void onItemClick(Assessment assessment) {
                Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_SHORT).show();
                launchAssessmentDetails(assessment);
            }
        }));
        incompleteAssessmentsRV.setLayoutManager(new LinearLayoutManager(view.getContext()));

        completeAssessmentsRV = (RecyclerView) view.findViewById(R.id.home_complete_assessments_rv);
        completeAssessmentsRV.setAdapter(new AssessmentsRecyclerviewAdapter(completeAssessments, new OnAssessmentClickListener() {
            @Override
            public void onItemClick(Assessment assessment) {
                Toast.makeText(getContext(), "Item Clicked", Toast.LENGTH_SHORT).show();
                launchAssessmentDetails(assessment);
            }
        }));
        completeAssessmentsRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void launchAssessmentDetails(Assessment assessment) {
        Intent intent = new Intent(getContext(), AssessmentDetailsActivity.class);
        intent.putExtra("assessmentId", assessment.getId());
        startActivity(intent);
    }
}