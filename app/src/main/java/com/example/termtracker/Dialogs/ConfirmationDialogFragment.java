package com.example.termtracker.Dialogs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.termtracker.R;

public class ConfirmationDialogFragment extends DialogFragment implements View.OnClickListener {

    public TextView message;
    public Button confirm, cancel;

    @Override
    public void onClick(View v) {

        ConfirmationDialogFragmentListener listener = (ConfirmationDialogFragmentListener) getActivity();
        listener.onConfirmDialogResolved(v == (View) confirm);
        dismiss();

    }

    public interface ConfirmationDialogFragmentListener{
        void onConfirmDialogResolved(boolean result);
    }

    public ConfirmationDialogFragment() {
        // Empty constructor is required for DialogFragment
    }

    public static ConfirmationDialogFragment newInstance(String message, String confirmText, String cancelText) {
        ConfirmationDialogFragment dialogFragment = new ConfirmationDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putString("confirmText", confirmText);
        bundle.putString("cancelText", cancelText);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.confirmation_dialog_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        message = (TextView) view.findViewById(R.id.confirmation_dialog_message);
        confirm = (Button) view.findViewById(R.id.confirmation_dialog_confirm_button);
        cancel = (Button) view.findViewById(R.id.confirmation_dialog_cancel_button);

        message.setText(getArguments().getString("message"));
        confirm.setText(getArguments().getString("confirmText"));
        cancel.setText(getArguments().getString("cancelText"));

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        confirm.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }
}
