package com.example.termtracker.Model;

import java.time.LocalDate;

public class ScheduledItem {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean completed;
    private boolean canBeCheckedOff;
    private boolean canBeDeleted;
}
