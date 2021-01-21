package com.example.tag.gui.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tag.Data;
import com.example.tag.R;
import com.example.tag.gui.activity.LoginActivity;
import com.example.tag.gui.activity.MainActivity;


public class SettingsFragment extends Fragment {
    private static final String TAG = SettingsFragment.class.getSimpleName();

    private AppCompatButton backButton;
    private AppCompatButton newGameButton;

    private AppCompatButton yesButton;
    private AppCompatButton noButton;

    private TextView gamePin;

    private MainActivity mainActivity;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        this.mainActivity = (MainActivity) getActivity();

        //this.gamePin = view.findViewById(R.id.settingsGamePin);

        //this.gamePin.setText("need to set game pin");

        this.backButton = view.findViewById(R.id.settingsBackButton);
        //this.newGameButton = view.findViewById(R.id.newGameButton);

        //this.dialogBuilder = new AlertDialog.Builder(getContext());

        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.toPlayFragment();
            }
        });

        /*this.newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewGamePopup();
            }
        });*/

        return view;
    }

    /*public void createNewGamePopup(){
        View popup = getLayoutInflater().inflate(R.layout.popup_new_game, null);

        this.yesButton = popup.findViewById(R.id.yesNewGameButton);
        this.noButton = popup.findViewById(R.id.noNewGameButton);

        this.yesButton.setOnClickListener(new View.OnClickListener() {
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

        this.noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialogBuilder.setView(popup);
        dialog = dialogBuilder.create();
        dialog.show();
    }*/

    /*@Override
    public void onDestroy() {
        super.onDestroy();
        if(dialog != null){
            dialog.dismiss();
        }
    }*/
}