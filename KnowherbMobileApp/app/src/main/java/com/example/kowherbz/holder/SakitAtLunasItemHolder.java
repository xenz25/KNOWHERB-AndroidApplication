package com.example.kowherbz.holder;

import java.io.Serializable;

public class SakitAtLunasItemHolder implements Serializable {
    private final int imageResource;
    private final String title;
    private final String description;
    private final String lower_case_title;

    public SakitAtLunasItemHolder(int imageResource, String title, String description, String lower_case_title) {
        this.imageResource = imageResource;
        this.title = title;
        this.description = description;
        this.lower_case_title = lower_case_title;
    }

    public String getLower_case_title() {
        return lower_case_title;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
