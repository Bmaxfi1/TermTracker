package com.example.termtracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termtracker.Data.DatabaseHelper;
import com.example.termtracker.Misc.OnTermClickListener;
import com.example.termtracker.Model.Course;
import com.example.termtracker.Model.Term;
import com.example.termtracker.R;

import java.util.List;

public class TermsRecyclerViewAdapter extends RecyclerView.Adapter<TermsRecyclerViewAdapter.ViewHolder> {



    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView startTextView;
        public TextView endTextView;
        public TextView coursesLeftTextView;
        public ImageView starImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.home_term_title);
            startTextView = (TextView) itemView.findViewById(R.id.home_term_start);
            endTextView = (TextView) itemView.findViewById(R.id.home_term_end);
            coursesLeftTextView = (TextView) itemView.findViewById(R.id.home_term_courses_left);
            starImageView = (ImageView) itemView.findViewById(R.id.term_completion_star);
        }
        public void bind(final Term term, final OnTermClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(term);
                }
            });
        }

    }

    private List<Term> termList;
    private OnTermClickListener listener;

    public TermsRecyclerViewAdapter(List<Term> terms, OnTermClickListener listener) {
        this.listener = listener;
        termList = terms;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.home_term_recyclerview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Term term = termList.get(position);

        TextView titleView = holder.titleTextView;
        TextView startView = holder.startTextView;
        TextView endView = holder.endTextView;
        TextView leftView = holder.coursesLeftTextView;
        ImageView starView = holder.starImageView;

        titleView.setText(term.getTitle());
        startView.setText(term.getStartDate());
        endView.setText(term.getEndDate());

        //get num courses left
        DatabaseHelper helper = new DatabaseHelper(leftView.getContext());
        int numCoursesLeft = 0;
        boolean noCoursesInTerm = true;
        List<Course> allCourses = helper.getAllCourses();
        for (Course course: allCourses) {
            if (course.getTermId() == term.getId()) {
                noCoursesInTerm = false;
                if (!course.isCompleted()) {
                    numCoursesLeft++;
                }
            }
        }
        if (noCoursesInTerm) {
            leftView.setText("N/A");
        } else {
            leftView.setText(String.valueOf(numCoursesLeft));
        }

        //set gold star image
        if (term.isCompleted()) {
            starView.setImageResource(R.drawable.ic_baseline_star_24);
        } else {
            starView.setImageResource(R.drawable.ic_baseline_star_border_24);
        }

        holder.bind(termList.get(position), listener);


    }

    @Override
    public int getItemCount() {
        if (termList != null) {
            return termList.size();
        }
        else return -1;
    }


}
