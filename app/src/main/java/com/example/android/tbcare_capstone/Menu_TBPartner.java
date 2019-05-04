package com.example.android.tbcare_capstone;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Menu_TBPartner extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    NavigationView navigationView;
    Intent intent;
    TextView user;

    Bundle bundle1;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tbpartner);
        navigationView = (NavigationView) findViewById(R.id.nav_viewtb);
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

            case R.id.nav_account:
                intent = new Intent(Menu_TBPartner.this, Account_TBPartner.class);
                break;
            case R.id.nav_add:
                intent = new Intent(Menu_TBPartner.this, Add_sputum_exam.class);
                break;
            case R.id.nav_patients:
                intent = new Intent(Menu_TBPartner.this, MyPatients.class);
                break;
            case R.id.nav_log_out:
                intent = new Intent(Menu_TBPartner.this, MainActivity.class);
                break;



        }

        Bundle bundle=new Bundle();
         bundle1= getIntent().getExtras();

        id= bundle1.getString("id");

        bundle.putString("id",id);
        intent.putExtras(bundle);

        startActivity(intent);

        return false;
    }
}