package com.example.termtracker.Dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.MainActivity;
import com.example.termtracker.Misc.DateTools;
import com.example.termtracker.Model.Term;
import com.example.termtracker.R;

public class EditTermDialogFragment extends DialogFragment implements View.OnClickListener {

    private Term termToModify;
    private DatabaseHelper helper;

    public Button save, cancel;
    public EditText title, start, end;

    @Override
    public void onClick(View v) {
        String parsedStartDate = start.getText().toString().replaceAll("[^0-9.]", "");
        String parsedEndDate = end.getText().toString().replaceAll("[^0-9.]", "");

        termToModify.setTitle(title.getText().toString());
        termToModify.setStartDate(parsedStartDate);
        termToModify.setEndDate(parsedEndDate);

        if (termToModify.isValidEdit(getContext()) && v.getId() == R.id.edit_term_save) {
            long idReturned = helper.updateTerm(termToModify);
            Toast.makeText(getContext(), "Modifications to Term #" + idReturned + " saved.", Toast.LENGTH_SHORT).show();

            dismiss();
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.edit_term_cancel) {
            dismiss();
        }
    }

    public static EditTermDialogFragment newInstance(String id) {
        EditTermDialogFragment dialogFragment = new EditTermDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_term, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = (EditText) view.findViewById(R.id.termName);
        start = (EditText) view.findViewById(R.id.startDate);
        end = (EditText) view.findViewById(R.id.endDate);
        save = (Button) view.findViewById(R.id.edit_term_save);
        cancel = (Button) view.findViewById(R.id.edit_term_cancel);

        helper = new DatabaseHelper(view.getContext());

        int id = Integer.parseInt(getArguments().getString("id"));
        termToModify = helper.getTermById(id);

        ImplementDatePickerDialog.assign(view, start);
        ImplementDatePickerDialog.assign(view, end);

        title.setText(termToModify.getTitle());
        start.setText(DateTools.addHyphensToDate(termToModify.getStartDate()));
        end.setText(DateTools.addHyphensToDate(termToModify.getEndDate()));

        save.setClickable(true);
        save.setVisibility(View.VISIBLE);
        cancel.setClickable(true);
        cancel.setVisibility(View.VISIBLE);

        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }
}
