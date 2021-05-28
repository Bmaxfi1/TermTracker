package com.example.termtracker.Model;

import android.content.Context;
import android.widget.Toast;

import java.util.Objects;

public class CourseInstructor implements Validatable{
    private int id;
    private String name;
    private String phone;
    private String email;
    private long courseId;

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public CourseInstructor(int id, String name, String phone, String email, int courseId) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isValid(Context context) {
        if (Objects.equals(this.getName(), "") || Objects.equals(this.getPhone(), "") || Objects.equals(this.getEmail(), "")) {
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
