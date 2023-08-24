package com.example.userapp.activity.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.userapp.R;
import com.example.userapp.activity.main.fragments.buytickets.BuyTicketFragment;
import com.example.userapp.activity.main.fragments.profile.ProfileFragment;
import com.example.userapp.activity.main.fragments.ticketrequests.RequestsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    ProfileFragment profileFragment;
    BuyTicketFragment buyTicketFragment;
    BottomNavigationView bottomNavigationView;
    MainController mainController;
    FragmentManager fragmentManager;
    RequestsFragment requestsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profileFragment = new ProfileFragment();
        buyTicketFragment = new BuyTicketFragment();
        requestsFragment = new RequestsFragment();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.fragment_container_view,profileFragment).commit();


        mainController = new MainController(this);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                mainController.switchToFragment(item);
                return true;
            }
        });
    }
}