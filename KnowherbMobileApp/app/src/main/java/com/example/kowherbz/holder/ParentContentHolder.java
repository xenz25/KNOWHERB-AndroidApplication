package com.example.kowherbz.holder;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParentContentHolder {
    //hold the instructions
    private List<ContentHolder> contentHolderList;
    private TitleHolder titleHolder;
    private String lower_case_name;

    public ParentContentHolder(TitleHolder titleHolder, List<ContentHolder> contentHolderList) {
        this.titleHolder = titleHolder;
        this.contentHolderList = contentHolderList;
    }

    public TitleHolder getTitleHolder() {
        return titleHolder;
    }

    public List<ContentHolder> getContentHolderList() {
        return contentHolderList;
    }

    public String getLower_case_name() {
        return lower_case_name;
    }

    public void setLower_case_name(String lower_case_name) {
        this.lower_case_name = lower_case_name;
    }
}

