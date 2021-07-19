package com.example.kowherbz.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kowherbz.R;

public class GumawaFragment extends Fragment {


    public GumawaFragment() {
        // Required empty public constructor
    }


    public static GumawaFragment newInstance() {
        return new GumawaFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gumawa, container, false);
    }
}