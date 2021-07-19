package com.example.kowherbz.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kowherbz.R;

public class MayAkdaFragment extends Fragment {

    public MayAkdaFragment() {
        // Required empty public constructor
    }

    public static MayAkdaFragment newInstance() {
        return new MayAkdaFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_may_akda, container, false);
    }
}