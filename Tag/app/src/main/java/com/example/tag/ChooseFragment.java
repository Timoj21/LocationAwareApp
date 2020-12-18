package com.example.tag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ChooseFragment extends Fragment {
    private static final String TAG = ChooseFragment.class.getSimpleName();

    private AppCompatButton hunterButton;
    private AppCompatButton runnerButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose, container, false);

        this.hunterButton = view.findViewById(R.id.hunterbutton);
        this.hunterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Hunter button clicked");

            }
        });

        this.runnerButton = view.findViewById(R.id.runnerButton);
        this.runnerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Runner button clicked");

            }
        });

        return view;
    }
}