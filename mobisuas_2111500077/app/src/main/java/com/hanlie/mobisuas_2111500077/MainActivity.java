package com.hanlie.mobisuas_2111500077;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        navView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toolbar);

        // Set toolbar as ActionBar
        setSupportActionBar(toolbar);

        // Hamburger icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        // Menu click listener
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.menu_submit) {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                } else if (id == R.id.menu_riwayat) {
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    // buka drawer saat icon hamburger tap
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(navView);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
