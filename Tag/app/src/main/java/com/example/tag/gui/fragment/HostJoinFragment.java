package com.example.tag.gui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tag.Data;
import com.example.tag.R;
import com.example.tag.gui.activity.LoginActivity;
import com.example.tag.gui.activity.MainActivity;

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
                Data.INSTANCE.setPlayer("Tikker");
                Data.INSTANCE.setPlayerId("123456");

                Data.INSTANCE.load();


                //TODO if connection failed, don't start next activity
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        this.joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.INSTANCE.setPlayer("Tikker");
                Data.INSTANCE.setPlayerId("123456");

                Data.INSTANCE.loadNewGame();

                //TODO if connection failed, don't start next activity
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}