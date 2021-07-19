package com.example.kowherbz.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kowherbz.R;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

public class GinamitSaAppFragment extends Fragment {
    private TextView link_gif;
    private TextView link_animation;
    private TextView link_gson;
    private TextView link_glide;

    public GinamitSaAppFragment() {
        // Required empty public constructor
    }


    public static GinamitSaAppFragment newInstance() {
        return new GinamitSaAppFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ginamit_sa_app, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        link_gif = view.findViewById(R.id.link_gif_drawable);
        link_animation = view.findViewById(R.id.link_android_view_animation);
        link_gson = view.findViewById(R.id.link_gson);
        link_glide = view.findViewById(R.id.link_glide);

        setLinkMovement(link_gif);
        setLinkMovement(link_animation);
        setLinkMovement(link_gson);
        setLinkMovement(link_glide);
    }

    private void setLinkMovement(TextView tv) {
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }
}