package com.example.termtracker.Model;

import android.content.Context;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.Objects;

public class Note implements Validatable{
    private int id;
    private String title;
    private String content;
    private LocalDate createDate;
    private long courseId;

    public Note(int id, String title, String content, long courseId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }



    @Override
    public boolean isValid(Context context) {
        if (Objects.equals(this.getTitle(), "") || Objects.equals(this.getContent(), "")) {
            Toast.makeText(context, "Please complete all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
