package com.example.tag.gui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.tag.Data;
import com.example.tag.gui.fragment.HostFragment;
import com.example.tag.gui.fragment.HostJoinFragment;
import com.example.tag.gui.fragment.JoinFragment;
import com.example.tag.R;

public class LoginActivity extends AppCompatActivity{
    private static final String TAG = LoginActivity.class.getSimpleName();

    private FragmentManager fragmentManager;

    private HostJoinFragment hostJoinFragment;
    private HostFragment hostFragment;
    private JoinFragment joinFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Data.INSTANCE.setContext(this);

        initialize();
    }

    //Initialize all views
    public void initialize(){
        this.fragmentManager = getSupportFragmentManager();

        this.hostJoinFragment = new HostJoinFragment();
        this.hostFragment = new HostFragment();
        this.joinFragment = new JoinFragment();

        fragmentManager.beginTransaction().replace(R.id.loginFragmentContainer, this.hostJoinFragment).commit();
    }

    public void toHostFragment(){
        fragmentManager.beginTransaction().replace(R.id.loginFragmentContainer, this.hostFragment).commit();
    }

    public void toJoinFragment(){
        fragmentManager.beginTransaction().replace(R.id.loginFragmentContainer, this.joinFragment).commit();
    }
}