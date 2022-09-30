package com.wingsmight.pushwords;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.stores.WordPairStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.wingsmight.pushwords.handlers.AppCycle;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wingsmight.pushwords.ui.dictionaryTab.DictionaryTab;
import com.wingsmight.pushwords.ui.learnedTab.LearnedTab;
import com.wingsmight.pushwords.ui.learningTab.LearningTab;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DictionaryTab dictionaryTab = new DictionaryTab();
    private LearningTab learningTab = new LearningTab();
    private LearnedTab learnedTab = new LearnedTab();
    private Fragment activeFragment = dictionaryTab;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        WordPairStore wordPairStore = WordPairStore.getInstance(this);

        BottomNavigationView navigationView = findViewById(R.id.nav_view);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, learningTab, "learningTab")
                .hide(learningTab)
                .commit();
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, learnedTab, "learnedTab")
                .hide(learnedTab)
                .commit();
        fragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, dictionaryTab, "dictionaryTab")
                .commit();

        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.dictionary_tab: {
                    getSupportFragmentManager().beginTransaction()
                            .hide(activeFragment)
                            .show(dictionaryTab)
                            .commit();
                    activeFragment = dictionaryTab;
                    return true;
                }
                case R.id.learning_tab: {
                    getSupportFragmentManager().beginTransaction()
                            .hide(activeFragment)
                            .show(learningTab)
                            .commit();
                    activeFragment = learningTab;
                    return true;
                }
                case R.id.learned_tab: {
                    getSupportFragmentManager().beginTransaction()
                            .hide(activeFragment)
                            .show(learnedTab)
                            .commit();
                    activeFragment = learnedTab;
                    return true;
                }
                default: {
                    return false;
                }
            }
        });

        BadgeDrawable learningBadge = navigationView.getOrCreateBadge(R.id.learning_tab);
        learningBadge.setBackgroundColor(getResources().getColor(R.color.appAmber));
        ArrayList<WordPair> learningWordPairs = wordPairStore.getLearningOnly();
        learningBadge.setNumber(learningWordPairs.size());
        learningBadge.setVisible(learningWordPairs.size() > 0);

        BadgeDrawable learnedBadge = navigationView.getOrCreateBadge(R.id.learned_tab);
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
    }
    @Override
    public void onBackPressed() { }
}