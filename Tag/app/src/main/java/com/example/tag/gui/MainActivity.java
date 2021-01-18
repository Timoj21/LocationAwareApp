package com.example.tag.gui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.tag.PlayFragment;
import com.example.tag.R;
import com.example.tag.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    private PlayFragment playFragment;
    private SettingsFragment settingsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    //Initialize all views
    public void initialize(){
        this.fragmentManager = getSupportFragmentManager();

        this.playFragment = new PlayFragment();
        this.settingsFragment = new SettingsFragment();

        fragmentManager.beginTransaction().replace(R.id.mainFragmentContainer, this.playFragment).commit();
    }

    public void toSettingsFragment(){
        fragmentManager.beginTransaction().replace(R.id.mainFragmentContainer, this.settingsFragment).commit();
    }

    public void toPlayFragment(){
        fragmentManager.beginTransaction().replace(R.id.mainFragmentContainer, this.playFragment).commit();
    }
}