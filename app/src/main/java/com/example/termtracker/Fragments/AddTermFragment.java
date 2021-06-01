package com.example.termtracker.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.MainActivity;
import com.example.termtracker.Dialogs.ImplementDatePickerDialog;
import com.example.termtracker.Model.CanBeAddedToDatabase;
import com.example.termtracker.Model.Term;
import com.example.termtracker.R;

public class AddTermFragment extends Fragment implements CanBeAddedToDatabase {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_term, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText startEditText = (EditText) view.findViewById(R.id.startDate);
        ImplementDatePickerDialog.assign(view, startEditText);

        EditText endEditText = (EditText) view.findViewById(R.id.endDate);
        ImplementDatePickerDialog.assign(view, endEditText);
    }



    @Override
    public void addNewItem() {
        View view = getView();
        EditText titleEditText = (EditText) view.findViewById(R.id.termName);
        EditText startEditText = (EditText) view.findViewById(R.id.startDate);
        EditText endEditText = (EditText) view.findViewById(R.id.endDate);

        String parsedStartDate = startEditText.getText().toString().replaceAll("[^0-9.]", "");
        String parsedEndDate = endEditText.getText().toString().replaceAll("[^0-9.]", "");


        Term termToAdd = new Term(-1,
                titleEditText.getText().toString(),
                parsedStartDate,
                parsedEndDate,
                false,
                true);
        if (termToAdd.isValid(getContext())) {
            DatabaseHelper databaseHelper;
            databaseHelper = new DatabaseHelper(view.getContext());
            long idReturned = databaseHelper.addTerm(termToAdd);
            if (idReturned < 0) {
                Toast.makeText(view.getContext(), "Something went wrong with the query.  Term not added.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(view.getContext(), "Term Added.  Title: " + termToAdd.getTitle(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        }


    }
}
