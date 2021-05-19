package com.example.termtracker.Model;

import java.time.LocalDate;

public class Note implements Validatable{
    private String title;
    private String content;
    private LocalDate createDate;

    @Override
    public boolean isValid() {
        return false;
    }
}
