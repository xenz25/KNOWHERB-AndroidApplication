package com.example.kowherbz.holder;

import java.io.Serializable;

public class HydroterapiItemHolder implements Serializable {
    private final String title;
    private final String lower_case_title;
    private final String description;

    public HydroterapiItemHolder(String title_lower_case, String title, String description) {
        this.title = title;
        this.lower_case_title = title_lower_case;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getLower_case_title() {
        return lower_case_title;
    }

    public String getDescription() {
        return description;
    }
}
