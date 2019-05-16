package com.example.android.tbcare_capstone.PartnerActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.android.tbcare_capstone.Class.PartnerClass;
import com.example.android.tbcare_capstone.Class.WebServiceClass;
import com.example.android.tbcare_capstone.R;
import com.google.gson.Gson;

public class Account_TBPartner extends AppCompatActivity {
    private TextView fname,lname,mname,contact,uname, email, partner_id, header_name;
    private FloatingActionButton editBtn;
    private Bundle bundle;
    private WebServiceClass webService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acct_tbpartner);

        InitiateControls();

    }

    private void InitiateControls(){
        fname=findViewById(R.id.myAcc_firstname);
        lname=findViewById(R.id.myAcc_lastname);
        mname=findViewById(R.id.myAcc_Middlename);
        header_name=findViewById(R.id.tp_name);
        uname=findViewById(R.id.myAcc_username);
        email = findViewById(R.id.myAcc_email);
        contact=findViewById(R.id.myAcc_ContactNo);
        partner_id=findViewById(R.id.myAcc_partner_ID);
        editBtn = findViewById(R.id.editBtn);


        SharedPreferences s = getSharedPreferences("session", 0);
        Gson gson = new Gson();
        String json = s.getString("class", "");
        String name = "";
        PartnerClass partner = gson.fromJson(json, PartnerClass.class);

        if(!partner.MiddleName.equals(null))
        {
            name = partner.FirstName +" "+partner.MiddleName+" "+partner.LastName;
            mname.setText(partner.MiddleName);
        }
        else{
            mname.setText("");
            name = partner.FirstName +" "+partner.LastName;
        }
        fname.setText(partner.FirstName);
        lname.setText(partner.LastName);
        header_name.setText(name);
        uname.setText(partner.GetUsername());
        email.setText(partner.Email);
        contact.setText(partner.Contact_No);
        partner_id.setText(partner.TP_ID);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account_TBPartner.this, Edit_PartnerActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
