package com.example.readyourresults;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.readyourresults.Camera.CamFragment;
import com.example.readyourresults.Help.HelpFragment;
import com.example.readyourresults.Home.HomeFragment;
import com.example.readyourresults.MySavedResults.MySavedResultsFragment;
import com.example.readyourresults.Settings.SettingsFragment;
import com.example.readyourresults.TestSelect.SelectFragment;

public class MainActivity extends AppCompatActivity 
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG =MainActivity.class.getSimpleName();

    Fragment fragment;
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content_main, homeFragment)
                .commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        Log.d(TAG,"Settings");
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            Log.d(TAG,"Home");
            fragment = new HomeFragment();
            launchFragment(fragmentManager, fragment);
        } else if (id == R.id.nav_interpret) {
            Log.d(TAG,"Interpret Results menu item selected.");
            fragment = new SelectFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content_main, fragment)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_saved) {
            Log.d(TAG,"My Saved Results menu item selected.");
            fragment = new MySavedResultsFragment();
            launchFragment(fragmentManager, fragment);
        } else if (id == R.id.nav_settings) {
            Log.d(TAG,"Settings menu item selected.");
            fragment = new SettingsFragment();
            launchFragment(fragmentManager, fragment);
        } else if (id == R.id.nav_help) {
            Log.d(TAG,"Help menu item selected.");
            fragment = new HelpFragment();
            launchFragment(fragmentManager, fragment);

        } //else if (id == R.id.nav_send) {

        //}
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void launchFragment(FragmentManager fragmentManager, Fragment launchFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_main, launchFragment)
                .addToBackStack(null)
                .commit();
    }
}
