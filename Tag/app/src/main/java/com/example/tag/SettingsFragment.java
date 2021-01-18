package com.example.tag;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tag.gui.LoginActivity;
import com.example.tag.gui.MainActivity;


public class SettingsFragment extends Fragment {
    private static final String TAG = SettingsFragment.class.getSimpleName();

    private AppCompatButton backButton;
    private AppCompatButton newGameButton;

    private AppCompatButton yesButton;
    private AppCompatButton noButton;

    private MainActivity mainActivity;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        this.mainActivity = (MainActivity) getActivity();

        this.backButton = view.findViewById(R.id.settingsBackButton);
        this.newGameButton = view.findViewById(R.id.newGameButton);

        this.dialogBuilder = new AlertDialog.Builder(getContext());

        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.toPlayFragment();
            }
        });

        this.newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewGamePopup();
            }
        });

        return view;
    }

    public void createNewGamePopup(){
        View popup = getLayoutInflater().inflate(R.layout.popup_new_game, null);

        this.yesButton = popup.findViewById(R.id.yesNewGameButton);
        this.noButton = popup.findViewById(R.id.noNewGameButton);

        this.yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        this.noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();
    }
}