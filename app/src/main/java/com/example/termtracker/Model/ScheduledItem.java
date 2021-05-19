package com.example.termtracker.Model;

import java.time.LocalDate;

public abstract class ScheduledItem {
    private String title;
    private String startDate;
    private String endDate;
    private boolean completed;
    private boolean canBeCheckedOff;
    private boolean canBeDeleted;

    public ScheduledItem(String title, String startDate, String endDate, boolean completed, boolean canBeCheckedOff, boolean canBeDeleted) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.completed = completed;
        this.canBeCheckedOff = canBeCheckedOff;
        this.canBeDeleted = canBeDeleted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCanBeCheckedOff() {
        return canBeCheckedOff;
    }

    public void setCanBeCheckedOff(boolean canBeCheckedOff) {
        this.canBeCheckedOff = canBeCheckedOff;
    }

    public boolean isCanBeDeleted() {
        return canBeDeleted;
    }

    public void setCanBeDeleted(boolean canBeDeleted) {
        this.canBeDeleted = canBeDeleted;
    }
}
