package com.example.termtracker.Dialogs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.termtracker.MainActivity;
import com.example.termtracker.R;

public class ShareNotesDialogFragment extends DialogFragment implements View.OnClickListener{

    public Button sendButton, cancelButton;
    public EditText emailEditText;
    public TextView messageTextView;

    public String emailString;
    public String message;
    public String contents;

    public ShareNotesDialogFragment() {
        //leave empty.
    }

    public static ShareNotesDialogFragment newInstance(String message, String contents) {
        ShareNotesDialogFragment shareNotesDialogFragment = new ShareNotesDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putString("contents", contents);
        shareNotesDialogFragment.setArguments(bundle);
        return shareNotesDialogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_share_notes, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sendButton = (Button) view.findViewById(R.id.share_dialog_send_button);
        cancelButton = (Button) view.findViewById(R.id.share_dialog_cancel_button);
        emailEditText = (EditText) view.findViewById(R.id.share_dialog_recipient_email);
        messageTextView = (TextView) view.findViewById(R.id.share_notes_message_textview);

        message = getArguments().getString("message");
        contents = getArguments().getString("contents");
        messageTextView.setText(message);

        sendButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        emailString = emailEditText.getText().toString();
        if (v.getId() == R.id.share_dialog_cancel_button) {
            dismiss();
        } else if (validEmail(emailString) && v.getId() == R.id.share_dialog_send_button) {
            sendEmail();
            Toast.makeText(getContext(), "Email sent to " + emailString, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), "That's not a valid email address.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validEmail(String emailString){
        if (emailString == null) {
            return false;
        }
        if (!emailString.contains("@")) {
            return false;
        }
        if (!emailString.contains(".")) {
            return false;
        }
        Log.d("superdopetag", "email is valid.");
        return true;
    }

    public void sendEmail() {
        //send email
        Intent emailToSend = new Intent(Intent.ACTION_SENDTO);
        emailToSend.setData(Uri.parse("mailto:")); //only for email apps
        emailToSend.putExtra(Intent.EXTRA_EMAIL, new String[]{emailEditText.getText().toString()});
        emailToSend.putExtra(Intent.EXTRA_SUBJECT, "Notes shared with you via TermTracker");
        emailToSend.putExtra(Intent.EXTRA_TEXT, contents);

        //this will let you share with more than just email clients.
//        Intent i = new Intent(Intent.ACTION_SEND);
//        i.setType("message/rfc822");
//        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
//        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
//        i.putExtra(Intent.EXTRA_TEXT   , "body of email");

        try {
            startActivity(Intent.createChooser(emailToSend, "Send with..."));
            getActivity().finish();
            Log.d("superdopetag", "email sent successfully");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(),
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
