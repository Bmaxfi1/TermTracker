package com.example.termtracker.Misc;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.termtracker.Model.CourseInstructor;
import com.example.termtracker.R;

public class InstructorDetailsDialogFragment extends DialogFragment implements View.OnClickListener {

    public Button save, cancel;
    public EditText name, phone, email;

    @Override
    public void onClick(View v) {
        if (getTargetFragment().getActivity().getCurrentFocus() != null) {
            getTargetFragment().getActivity().getCurrentFocus().clearFocus();
        }
        CourseInstructor courseInstructor = new CourseInstructor(-1,
                name.getText().toString(), phone.getText().toString(), email.getText().toString(), -1
        );

        if (courseInstructor.isValid(getContext())) {

            InstructorDetailsDialogFragmentListener listener = (InstructorDetailsDialogFragmentListener) getTargetFragment();
            listener.onSaveInstructor(name.getText().toString(), phone.getText().toString(), email.getText().toString());
            dismiss();
        }

    }

    public interface InstructorDetailsDialogFragmentListener{
        void onSaveInstructor(String newName, String newPhone, String newEmail);
    }

    public InstructorDetailsDialogFragment() {
        // Empty constructor is required for DialogFragment
    }

    public static InstructorDetailsDialogFragment newInstance(String title) {
        InstructorDetailsDialogFragment dialogFragment = new InstructorDetailsDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.instructor_details_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        name = (EditText) view.findViewById(R.id.instructor_name_edit_text);
        save = (Button) view.findViewById(R.id.instructor_dialog_save_button);
        cancel = (Button) view.findViewById(R.id.instructor_dialog_cancel_button);
        phone = (EditText) view.findViewById(R.id.instructor_phone_edit_text);
        email = (EditText) view.findViewById(R.id.instructor_email_edit_text);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        save.setOnClickListener(this);

    }


}
