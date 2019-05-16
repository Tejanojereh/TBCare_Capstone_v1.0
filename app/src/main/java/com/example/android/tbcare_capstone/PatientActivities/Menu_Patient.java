package com.example.android.tbcare_capstone.PatientActivities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.Toast;

import com.example.android.tbcare_capstone.AccountActivities.ChangePasswordActivity;
import com.example.android.tbcare_capstone.AccountActivities.View_AuditLog;
import com.example.android.tbcare_capstone.ChoosePartner;
import com.example.android.tbcare_capstone.Class.PatientClass;
import com.example.android.tbcare_capstone.Class.WebServiceClass;
import com.example.android.tbcare_capstone.MainActivity;
import com.example.android.tbcare_capstone.Patient_Setintake;
import com.example.android.tbcare_capstone.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Menu_Patient extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, WebServiceClass.Listener {

    NavigationView navigationView;
    Intent intent;
    DrawerLayout dLayout;
    PatientClass patient;

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
        setContentView(R.layout.home_patient);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar

        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        // implement setNavigationOnClickListener event
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dLayout.openDrawer(Gravity.LEFT);
            }
        });

        SharedPreferences s = getSharedPreferences("session", 0);
        id = Integer.toString(s.getInt("account_id", 0));
        Gson gson = new Gson();
        String json = s.getString("class", "");
        patient = gson.fromJson(json, PatientClass.class);


        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        TextView userName = (TextView) headerView.findViewById(R.id.user_name);
        TextView user_id=headerView.findViewById(R.id.user_id);
        userName.setText(patient.GetUsername());
        user_id.setText(patient.TB_CASE_NO);
        navigationView.addHeaderView(headerView);

        CheckPatientHasPartner();
        //setNavigationDrawer(); // call method
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);

        Bundle bundle = new Bundle();
        switch (menuItem.getItemId())
        {
            case R.id.nav_home:
                break;
            case R.id.nav_account:
                intent = new Intent(Menu_Patient.this, Account_Patient.class);
                startActivity(intent);
                break;
            case R.id.nav_progress:
                intent = new Intent(Menu_Patient.this, My_Progress.class);
                bundle.putString("patient_id", id);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.nav_schedule:
                intent = new Intent(Menu_Patient.this, My_Schedule_Patient.class);
                bundle.putString("patient_id", id);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.nav_tp:
                intent = new Intent(Menu_Patient.this, My_Partner_Info.class);
                startActivity(intent);
                break;
            case R.id.nav_ChangePassword:
                intent = new Intent(Menu_Patient.this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_auditLog:
                intent = new Intent(getApplicationContext(), View_AuditLog.class);
                startActivity(intent);
                break;
            case R.id.nav_log_out:
                SharedPreferences s = getSharedPreferences("session", 0);
                SharedPreferences.Editor editor = s.edit();
                int id = 0;
                String account_type = "";
                editor.putInt("account_id", id);
                editor.putString("account_type", account_type);
                editor.putString("class", "");
                editor.apply();
                intent = new Intent(Menu_Patient.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;



        }

        return true;
    }

    public void CheckPatientHasPartner(){
        SharedPreferences s = getSharedPreferences("session", 0);
        id  = Integer.toString(s.getInt("account_id", 0));
        String address = "http://tbcarephp.azurewebsites.net/retrieve_AssignedPartner.php";
        String[] value = {id};
        String[] valueName = {"id"};
        WebServiceClass wbc = new WebServiceClass(address, value, valueName, Menu_Patient.this, Menu_Patient.this);

        wbc.execute();

    }


    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        if(flag)
        {
            if(Result != null)
            {
                String hasPartner;
                JSONObject object = null;
                try {
                    object = Result.getJSONObject(0);
                    hasPartner = object.getString("hasPartner");

                    if(hasPartner.equals("false")){
                        Toast.makeText(this, "You have no partner assigned", Toast.LENGTH_LONG).show();
                        intent = new Intent(this, ChoosePartner.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(hasPartner.equals("true")){
                        patient.Partner_id = object.getString("partner_id");
                        SharedPreferences s = getSharedPreferences("session", 0);
                        SharedPreferences.Editor editor = s.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(patient);
                        editor.putString("class", json);
                        editor.apply();
                        object = Result.getJSONObject(1);
                        if(Integer.parseInt(object.getString("intakeSchedule").toString()) == 0)
                        {
                            object = Result.getJSONObject(2);
                            int numberofIntake = Integer.parseInt(object.getString("number_of_intake"));
                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Menu_Patient.this);
                            builder.setMessage("Good day! Your partner has already accepted your request. Time to set up your medication intake. Please input your first intake for the day, and we'll calculate it based on the number of intakes your partner gave you.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = new Intent(Menu_Patient.this, Patient_Setintake.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("number_of_intakes", numberofIntake);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                    })
                                    .setCancelable(false);
                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                    }
                } catch (JSONException e) {
                    Toast.makeText(this, e.getMessage().toString(), Toast.LENGTH_LONG);;
                }

            }
        }
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

/*private void setNavigationDrawer() {
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
    }*/




