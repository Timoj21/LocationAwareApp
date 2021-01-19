package com.example.tag;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

    public class MainViewModel extends ViewModel {

        private MutableLiveData<String> player = new MutableLiveData<>();

        public MutableLiveData<String> getPlayer() {
            return player;
        }

        public void setPlayer(MutableLiveData<String> player) {
            this.player = player;
        }
    }
