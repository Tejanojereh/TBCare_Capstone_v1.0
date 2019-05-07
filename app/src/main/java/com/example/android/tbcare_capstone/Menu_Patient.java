package com.example.android.tbcare_capstone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.Toast;

public class Menu_Patient extends AppCompatActivity {
    NavigationView navigationView;
    Intent intent;
    DrawerLayout dLayout;

    String id;
    Bundle bundle1;
    TextView user;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_patienttest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar

        // implement setNavigationOnClickListener event
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });
        setNavigationDrawer(); // call method
    }

    private void setNavigationDrawer() {
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = (NavigationView) findViewById(R.id.navigation); // initiate a Navigation View
// implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment frag = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id
// check selected menu item's id and replace a Fragment Accordingly

               //GUIDE
             //   if (itemId == R.id.first) {
            //        frag = new FirstFragment();
             //   } else if (itemId == R.id.second) {
             //       frag = new SecondFragment();
              //  } else if (itemId == R.id.third) {
             //       frag = new ThirdFragment();
              //  }
                //END GUIDE
// display a toast message with menu item's title
                Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                if (frag != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
                    transaction.commit(); // commit the changes
                    dLayout.closeDrawers(); // close the all open Drawer Views
                    return true;
                }
                return false;
            }
        });
    }
}
//   navigationView = (NavigationView) findViewById(R.id.nav_view);
      //  navigationView.setNavigationItemSelectedListener(this);
     //   bundle1=getIntent().getExtras();
      //  id= bundle1.getString("id");
     //   user=findViewById(R.id.user_id);
     //   View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
     //   TextView userName = (TextView) headerView.findViewById(R.id.user_id);
     //   userName.setText(id);

      //  navigationView.addHeaderView(headerView);




