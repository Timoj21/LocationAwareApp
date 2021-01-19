package com.example.tag;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tag.gui.LoginActivity;

public class HostJoinFragment extends Fragment {
    private static final String TAG = HostJoinFragment.class.getSimpleName();

    private AppCompatButton hostGameButton;
    private AppCompatButton joinGameButton;

    private LoginActivity loginActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_host_join, container, false);

        this.hostGameButton = view.findViewById(R.id.hostGameButton);
        this.joinGameButton = view.findViewById(R.id.joinGameButton);

        this.loginActivity = (LoginActivity) getContext();

        this.hostGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginActivity.toHostFragment();
            }
        });

        this.joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginActivity.toJoinFragment();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}