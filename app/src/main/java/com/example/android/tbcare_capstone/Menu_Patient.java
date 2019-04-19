package com.example.android.tbcare_capstone;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Menu_Patient extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    NavigationView navigationView;
    Intent intent;
    DrawerLayout drawerLayout;
    String id;
    Bundle bundle1;
    TextView user;
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu_patient);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        bundle1=getIntent().getExtras();
        id= bundle1.getString("id");
        user=findViewById(R.id.user_id);
        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        TextView userName = (TextView) headerView.findViewById(R.id.user_id);
        userName.setText(id);

        navigationView.addHeaderView(headerView);



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);

        switch (menuItem.getItemId())
        {

            case R.id.nav_progress:
                intent = new Intent(Menu_Patient.this, View_and_Update_Medication_Progress.class);
                break;
            case R.id.nav_schedule:
                intent = new Intent(Menu_Patient.this, My_Schedule_Patient.class);
                break;
            case R.id.nav_tp:
                intent = new Intent(Menu_Patient.this, Partner_Info.class);
                break;
            case R.id.nav_log_out:
                intent = new Intent(Menu_Patient.this, MainActivity.class);
                break;



        }
        Bundle bundle= new Bundle();

        bundle1=getIntent().getExtras();
        id= bundle1.getString("id");


        bundle.putString("id",id);
        intent.putExtras(bundle);

        startActivity(intent);

        return false;
    }
}