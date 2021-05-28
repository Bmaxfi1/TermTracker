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
import com.example.termtracker.Misc.OnCourseClickListener;
import com.example.termtracker.Model.Assessment;
import com.example.termtracker.Model.Course;
import com.example.termtracker.R;

import java.util.List;

public class CoursesRecyclerViewAdapter extends RecyclerView.Adapter<CoursesRecyclerViewAdapter.ViewHolder> {

    private List<Course> courseList;
    private OnCourseClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView startTextView;
        public TextView endTextView;
        public TextView assessmentsLeftTextView;
        public ImageView starImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.home_course_title);
            startTextView = (TextView) itemView.findViewById(R.id.home_course_start);
            endTextView = (TextView) itemView.findViewById(R.id.home_course_end);
            starImageView = (ImageView) itemView.findViewById(R.id.course_completion_star);
            assessmentsLeftTextView = (TextView) itemView.findViewById(R.id.home_course_assessments_left);
        }
        public void bind(final Course course, final OnCourseClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(course);
                }
            });

        }
    }



    public CoursesRecyclerViewAdapter(List<Course> courses, OnCourseClickListener listener) {
        this.listener = listener;
        courseList = courses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.home_course_recyclerview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = courseList.get(position);

        TextView titleView = holder.titleTextView;
        TextView startView = holder.startTextView;
        TextView endView = holder.endTextView;
        TextView leftView = holder.assessmentsLeftTextView;
        ImageView starView = holder.starImageView;

        titleView.setText(course.getTitle());
        startView.setText(course.getStartDate());
        endView.setText(course.getEndDate());

        //get assessments left and assign it to the view
        DatabaseHelper helper = new DatabaseHelper(leftView.getContext());
        int numAssessmentsLeft = 0;
        boolean noAssessmentsInCourse = true;
        List<Assessment> allAssessments = helper.getAllAssessments();
        for (Assessment assessment: allAssessments) {
            if (assessment.getCourseId() == course.getId()) {
                noAssessmentsInCourse = false;
                if (!assessment.isCompleted()) {
                    numAssessmentsLeft++;
                }
            }
        }
        if (noAssessmentsInCourse) {
            leftView.setText("N/A");
        } else {
            leftView.setText(String.valueOf(numAssessmentsLeft));
        }

        //set gold star image
        if (course.isCompleted()) {
            starView.setImageResource(R.drawable.ic_baseline_star_24);
        } else {
            starView.setImageResource(R.drawable.ic_baseline_star_border_24);
        }

        holder.bind(courseList.get(position), listener);

    }

    @Override
    public int getItemCount() {
        if (courseList != null) {
            return courseList.size();
        }
        else return -1;
    }

}
