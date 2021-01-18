package com.example.tag;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tag.gui.LoginActivity;
import com.example.tag.gui.MainActivity;

public class JoinFragment extends Fragment {
    private static final String TAG = JoinFragment.class.getSimpleName();

    private AppCompatButton joinButton;

    private EditText gamePinEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join, container, false);

        this.gamePinEditText = view.findViewById(R.id.gamePinEditText);

        this.joinButton = view.findViewById(R.id.joinButton);
        this.joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gamePinEditText.getText().length() > 0) {
                    //TODO connect to server as player

                    //TODO if connection failed, don't start next activity
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                } else{
                    Toast.makeText(getActivity(), R.string.valid_game_pin,
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}