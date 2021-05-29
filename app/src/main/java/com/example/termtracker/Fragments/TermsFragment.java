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

import com.example.termtracker.Adapters.TermsRecyclerViewAdapter;
import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.Listeners.OnTermClickListener;
import com.example.termtracker.Model.Term;
import com.example.termtracker.R;
import com.example.termtracker.TermDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class TermsFragment extends Fragment {

    List<Term> terms;
    RecyclerView completeTermsRv;
    RecyclerView incompleteTermsRv;
    TermsRecyclerViewAdapter completeAdapter;
    TermsRecyclerViewAdapter incompleteAdapter;

    public TermsFragment() {
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
        return inflater.inflate(R.layout.fragment_terms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //recyclerview
        DatabaseHelper helper = new DatabaseHelper(view.getContext());
        terms = helper.getAllTerms();

        List<Term> incompleteTerms = new ArrayList<>();
        List<Term> completeTerms = new ArrayList<>();

        for (Term term: terms) {
            boolean complete = term.isCompleted();
            if (!complete) {
                incompleteTerms.add(term);
            } else {
                completeTerms.add(term);
            }
        }

        incompleteTermsRv = (RecyclerView) view.findViewById(R.id.home_incomplete_terms_rv);
        incompleteTermsRv.setAdapter(new TermsRecyclerViewAdapter(incompleteTerms, new OnTermClickListener() {
            @Override
            public void onItemClick(Term term) {
                launchTermDetails(term);
            }
        }));
        incompleteTermsRv.setLayoutManager(new LinearLayoutManager(view.getContext()));

        completeTermsRv = (RecyclerView) view.findViewById(R.id.home_complete_terms_rv);
        completeTermsRv.setAdapter(new TermsRecyclerViewAdapter(completeTerms, new OnTermClickListener() {
            @Override
            public void onItemClick(Term term) {
                launchTermDetails(term);
            }
        }));
        completeTermsRv.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    public void launchTermDetails(Term term) {
        Intent intent = new Intent(getContext(), TermDetailsActivity.class);
        intent.putExtra("termId", term.getId());
        startActivity(intent);

    }
}