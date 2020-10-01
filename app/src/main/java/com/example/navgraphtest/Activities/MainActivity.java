package com.example.navgraphtest.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.navgraphtest.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.navgraphtest.Utilities.ApplicationSettings.settingsExist;

/**
 * MainActivity.
 * <p>
 * I am using the "One-Activity-Multiple-Fragments" navigation pattern.
 * This is essentially the parent activity to display all the other fragments.
 * Contains a NavHostFragment, BottomNavigationView and a toolbar.
 * <p>
 * On fresh install, check to see if sharedPreferences settings exist,
 * if not launch the settings activity.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
        setUpNavigation();

        // Checked sharedPreferences setttings to see if user has completed profile + calorie goal.
        if (!settingsExist(this)) {
            // If the profile hasn't been completed, launch the settings activity and a simple prompt.
            Toast.makeText(getApplicationContext(), "Let's get started! " +
                    "Tap the Calculate button to begin.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        } else {
            // This is redundant.
            setUpNavigation();
        }
    }

    @Override
    protected void onRestart() {
        this.finish();
        this.startActivity(new Intent(this, this.getClass()));
        super.onRestart();

        // This is to recreate everything when leaving settingsActivity to change from light/dark theme.
        // Not yet implemented.
//        if(settingsExist(this)) {
//            setUpNavigation();
//        }
//        else
//        {
//            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
//            startActivity(intent);
//        }
//        setUpNavigation();
    }

    public void setUpNavigation() {
        // Find the toolbar view inside the activity layout
        Toolbar toolbar = findViewById(R.id.toolbar_main_navhostfragment);

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        // Set toolbar title
        toolbar.setTitle("Home Screen");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationview_main_navhostfragment);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentcontainerview_main_navhostfragment);

        // Set up Navcontroller with bottomNavigationView.
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.getNavController());

        // ItemReselectedListener for when current item is selected again on the BottomNavigationBar.
        BottomNavigationView.OnNavigationItemReselectedListener listener =
                new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
            // Do nothing here to prevent fragment being recreated when bottomNavigationView items are reselected.
            }
        };
        bottomNavigationView.setOnNavigationItemReselectedListener(listener);
    }

    // Simple method to update actionbar title from within a fragment.
    // I use this in most of the fragments in onResume:
    // ((MainActivity) getActivity()).setActionBarTitle("")
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    // Inflate options menu in toolbar.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Options menu items handled with a switch statement here.
    // References items in R.layout.settings_menu.xml and their relevant Activities/Java class files.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


