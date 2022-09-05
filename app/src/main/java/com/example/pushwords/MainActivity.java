package com.example.pushwords;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.pushwords.data.WordPairStore;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.pushwords.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
    @Override
    protected void onPause() {
        super.onPause();

        WordPairStore.getInstance(this).save();
    }
}