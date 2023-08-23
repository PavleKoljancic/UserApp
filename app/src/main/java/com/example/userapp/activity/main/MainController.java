package com.example.userapp.activity.main;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.example.userapp.R;

public class MainController {

    private MainActivity mainActivity;
    private Fragment currentFragment;

    protected MainController(MainActivity mainActivity) {
        this.mainActivity=mainActivity;
        currentFragment = mainActivity.profileFragment;
    }

    public void switchToFragment(MenuItem item) {

        if (item.getItemId() == R.id.profile) {
            currentFragment = mainActivity.profileFragment;
        } else if (item.getItemId() == R.id.request) {


        } else if (item.getItemId() == R.id.new_request) {
            currentFragment = mainActivity.buyTicketFragment;

        } else if (item.getItemId() == R.id.documents) {
        } else if (item.getItemId() == R.id.settings) {
        }
        mainActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view,currentFragment).commit();
    }
}
