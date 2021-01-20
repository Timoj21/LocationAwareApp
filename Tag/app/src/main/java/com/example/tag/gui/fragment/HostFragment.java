package com.example.tag.gui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tag.Data;
import com.example.tag.R;
import com.example.tag.Service;
import com.example.tag.gui.activity.MainActivity;

public class HostFragment extends Fragment {
    private static final String TAG = HostFragment.class.getSimpleName();

    private AppCompatButton hostButton;
    private EditText playerAmountEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_host, container, false);

        this.playerAmountEditText = view.findViewById(R.id.playerAmountEditText);

        this.hostButton = view.findViewById(R.id.hostButton);
        this.hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playerAmountEditText.getText().length() > 0) {
                    Data.INSTANCE.setPlayer("Tikker");
                    Data.INSTANCE.setPlayerId("123456");

                    //TODO if connection failed, don't start next activity
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                    getActivity().startService(new Intent(getActivity(), Service.class));
                } else {
                    Toast.makeText(getActivity(), R.string.valid_player_amount,
                            Toast.LENGTH_LONG).show();
                }
            }
        });


        // Inflate the layout for this fragment
        return view;
    }
}