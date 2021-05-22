package com.example.termtracker.Misc;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.termtracker.Model.CourseInstructor;
import com.example.termtracker.R;

@Deprecated
public class InstructorDetailsDialog extends Dialog implements View.OnClickListener {


    public Activity a;
    public Dialog dialog;
    public Button save, cancel;
    public EditText name, phone, email;

    public InstructorDetailsDialog(Activity activity) {
        super(activity);
        this.a = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.instructor_details_dialog);
        save = (Button) findViewById(R.id.instructor_dialog_save_button);
        cancel = (Button) findViewById(R.id.instructor_dialog_cancel_button);
        name = (EditText) findViewById(R.id.instructor_name_edit_text);
        phone = (EditText) findViewById(R.id.instructor_phone_edit_text);
        email = (EditText) findViewById(R.id.instructor_email_edit_text);

        save.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.instructor_dialog_save_button:
                CourseInstructor courseInstructorToAdd = new CourseInstructor(
                        name.getText().toString(),
                        phone.getText().toString(),
                        email.getText().toString()
                );
                if (courseInstructorToAdd.isValid()) {
                    //add it to the recyclerview


                    dismiss();
                    break;
                }
            case R.id.instructor_dialog_cancel_button:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
