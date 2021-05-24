package com.example.termtracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.Model.CourseInstructor;
import com.example.termtracker.R;

import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class InstructorsRecyclerviewAdapter extends
        RecyclerView.Adapter<InstructorsRecyclerviewAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView phoneTextView;
        public TextView emailTextView;
        public Button deleteButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.instructor_name);
            phoneTextView = (TextView) itemView.findViewById(R.id.instructor_phone);
            emailTextView = (TextView) itemView.findViewById(R.id.instructor_email);
            deleteButton = (Button) itemView.findViewById(R.id.delete_instructor_button);
        }
    }


    private List<CourseInstructor> instructorList;

    public InstructorsRecyclerviewAdapter(List<CourseInstructor> instructors) {
        instructorList = instructors;
    }


    @NonNull
    @Override
    //used to inflate an xml layout and returns it to the holder
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.instructors_recyclerview_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;    }

    @Override
    //puts data into the item through holder
    public void onBindViewHolder(@NonNull InstructorsRecyclerviewAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        CourseInstructor courseInstructor = instructorList.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        TextView textView1 = holder.phoneTextView;
        TextView textView2 = holder.emailTextView;
        textView.setText(courseInstructor.getName());
        textView1.setText(courseInstructor.getPhone());
        textView2.setText(courseInstructor.getEmail());

    }

    @Override
    public int getItemCount() {
        if (instructorList != null) {
            return instructorList.size();
        }
        else return -1;
    }


}
