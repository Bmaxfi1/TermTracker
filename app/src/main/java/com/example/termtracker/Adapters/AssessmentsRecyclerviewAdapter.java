package com.example.termtracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.Misc.OnAssessmentClickListener;
import com.example.termtracker.Model.Assessment;
import com.example.termtracker.R;

import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class AssessmentsRecyclerviewAdapter extends
        RecyclerView.Adapter<AssessmentsRecyclerviewAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView titleTextView;
        public TextView startTextView;
        public TextView endTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.home_assessment_title);
            startTextView = (TextView) itemView.findViewById(R.id.home_assessment_start);
            endTextView = (TextView) itemView.findViewById(R.id.home_assessment_end);

        }
        public void bind(final Assessment assessment, final OnAssessmentClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(assessment);
                }
            });
        }


    }
    private List<Assessment> assessmentList;
    private OnAssessmentClickListener listener;

    public AssessmentsRecyclerviewAdapter(List<Assessment> assessments, OnAssessmentClickListener listener) {
        this.listener = listener;
        assessmentList = assessments;
    }


    @NonNull
    @Override
    //used to inflate an xml layout and returns it to the holder
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.home_assessment_recyclerview_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;    }

    @Override
    //puts data into the item through holder
    public void onBindViewHolder(@NonNull AssessmentsRecyclerviewAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Assessment assessment = assessmentList.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.titleTextView;
        TextView textView1 = holder.startTextView;
        TextView textView2 = holder.endTextView;
        textView.setText(assessment.getTitle());
        textView1.setText(assessment.getStartDate());
        textView2.setText(assessment.getEndDate());

        holder.bind(assessmentList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        if (assessmentList != null) {
            return assessmentList.size();
        }
        else return -1;
    }




}
