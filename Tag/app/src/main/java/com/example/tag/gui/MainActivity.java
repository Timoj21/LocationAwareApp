package com.example.tag.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.example.tag.ChooseFragment;
import com.example.tag.PlayFragment;
import com.example.tag.R;
import com.example.tag.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private ChooseFragment chooseFragment;
    private PlayFragment playFragment;
    private SettingsFragment settingsFragment;

    private AppCompatButton chooseButton;
    private AppCompatButton playButton;
    private AppCompatButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        setClickListeners();
    }

    //Initialize all views
    public void initialize(){
        this.fragmentManager = fragmentManager = getSupportFragmentManager();

        this.chooseFragment = new ChooseFragment();
        this.playFragment = new PlayFragment();
        this.settingsFragment = new SettingsFragment();

        this.chooseButton = findViewById(R.id.chooseButton);
        this.playButton = findViewById(R.id.homeButton);
        this.settingsButton = findViewById(R.id.settingsButton);

        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, this.chooseFragment).commit();
    }

    //Set all on-click listeners
    public void setClickListeners(){
        this.chooseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                toChooseView();
            }
        });

        this.playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                toPlayView();
            }
        });

        this.settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSettingsView();
            }
        });
    }

    public void toChooseView(){
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, this.chooseFragment).commit();
    }

    public void toPlayView(){
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, this.playFragment).commit();
    }

    public void toSettingsView(){
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, this.settingsFragment).commit();
    }
}