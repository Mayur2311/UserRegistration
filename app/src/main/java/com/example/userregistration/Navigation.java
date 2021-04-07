package com.example.userregistration;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.TaskStackBuilder;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Navigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    public NavController navC;
    public Toolbar tool;
    public NavigationView navigationV;

    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;

    String bundlevalue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Bundle bundle = getIntent().getExtras();
        bundlevalue =  bundle.getString("Users");

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        setupNavigationView();

    }

    public void setupNavigationView()
    {
        //Setup Toolbar
        tool = findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = findViewById(R.id.drawer);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, tool, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();


        navigationV = findViewById(R.id.navigationView);
        navC = androidx.navigation.Navigation.findNavController(this, R.id.host_fragment);

        //NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout);
        NavigationUI.setupWithNavController(navigationV, navC);

        navigationV.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {

        return NavigationUI.navigateUp(androidx.navigation.Navigation.findNavController(this,R.id.host_fragment),drawerLayout);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        Class fragmentClass = null;
        item.setCheckable(true);
        drawerLayout.closeDrawers();

        int id = item.getItemId();

        switch (id)
        {
            case R.id.dashboard_menu:
                Toast.makeText(getApplicationContext(),"Dashboard Fragment Clicked!", Toast.LENGTH_LONG).show();
               // navC.navigate(R.id.dashboard_layout);
//                Intent i = new Intent(this, DrawerLayout.class);
//                startActivity(i);
//                Toast.makeText(this, "Intent worked", Toast.LENGTH_LONG).show();

                 fragmentClass = Dashboard.class;
                 break;

            case R.id.profile_menu:
                Toast.makeText(getApplicationContext(),"Profile Fragment Clicked!", Toast.LENGTH_LONG).show();
//                navC.navigate(R.id.profile_layout1);
////                navC.navigate(R.id.profile_menu);

                fragmentClass = Profile.class;

                break;

            case R.id.logout_menu:
                Toast.makeText(this, "Logout Clicked", Toast.LENGTH_SHORT).show();

                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }

        try {
            fragment  =(Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.dashboard_layout, fragment).commit();

        return true;
    }
}