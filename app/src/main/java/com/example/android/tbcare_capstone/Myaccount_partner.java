package com.example.android.tbcare_capstone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class Myaccount_partner extends AppCompatActivity {
        private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acct_tbpartner);

        Toolbar toolbar1 = findViewById(R.id.my_toolbar);
             setSupportActionBar(toolbar1);




        }

    }




