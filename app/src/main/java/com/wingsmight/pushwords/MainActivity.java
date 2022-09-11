package com.wingsmight.pushwords;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.stores.WordPairStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.wingsmight.pushwords.databinding.ActivityMainBinding;
import com.wingsmight.pushwords.handlers.AppCycle;
import com.wingsmight.pushwords.handlers.NotificationService;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);

        BottomNavigationView navBar = findViewById(R.id.nav_view);

        WordPairStore wordPairStore = WordPairStore.getInstance(this);

        BadgeDrawable learningBadge = navBar.getOrCreateBadge(R.id.learning_tab);
        learningBadge.setBackgroundColor(getResources().getColor(R.color.appAmber));
        ArrayList<WordPair> learningWordPairs = wordPairStore.getLearningOnly();
        learningBadge.setNumber(learningWordPairs.size());
        learningBadge.setVisible(learningWordPairs.size() > 0);

        BadgeDrawable learnedBadge = navBar.getOrCreateBadge(R.id.learned_tab);
        learnedBadge.setBackgroundColor(getResources().getColor(R.color.appGreen));
        ArrayList<WordPair> learnedWordPairs = wordPairStore.getLearnedOnly();
        learnedBadge.setNumber(learnedWordPairs.size());
        learnedBadge.setVisible(learnedWordPairs.size() > 0);

        wordPairStore.setOnStateChanged(wordPairState -> {
            ArrayList<WordPair> localLearningWordPairs = wordPairStore.getLearningOnly();
            learningBadge.setNumber(localLearningWordPairs.size());
            learningBadge.setVisible(localLearningWordPairs.size() > 0);

            ArrayList<WordPair> localLearnedWordPairs = wordPairStore.getLearnedOnly();
            learnedBadge.setNumber(localLearnedWordPairs.size());
            learnedBadge.setVisible(localLearnedWordPairs.size() > 0);
        });

        // Action bar
        getSupportActionBar().hide();

        // Permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] { WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE },1);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();

        AppCycle.quit(this);
    }
    @Override
    protected void onStop() {
        super.onStop();

        startService(new Intent(this, NotificationService.class));
    }
    @Override
    public void onBackPressed() { }
}