package com.example.android.tbcare_capstone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.android.tbcare_capstone.Class.PartnerClass;
import com.example.android.tbcare_capstone.Class.WebServiceClass;
import com.example.android.tbcare_capstone.Class.WebServiceClass.Listener;
import com.google.gson.Gson;

public class Account_TBPartner extends AppCompatActivity implements Listener {
    private TextView fname,lname,mname,contact,uname, email, partner_id;
    private ImageButton btn,back;
    private Bundle bundle;
    private WebServiceClass webService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acct_tbpartner);

        InitiateControls();

        /*String address = "http://tbcarephp.azurewebsites.net/retrieve_tbinfo.php";
        String[] value = {bundle.getString("id")};
        String[] valueName = {"P_ID"};
        webService = new WebServiceClass(address, value, valueName, Account_TBPartner.this, this);
        webService.execute();*/

        //new WebService_TBPartner().execute();

    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {

        try {
            JSONObject c = Result.getJSONObject(0);

            fname.setText(c.getString("TP_Fname").toString());
            c = Result.getJSONObject(1);
            mname.setText("TP_Mname");
            c = Result.getJSONObject(2);
            lname.setText(c.getString("TP_Lname").toString());
            c = Result.getJSONObject(3);
            contact.setText(c.getString("TP_ContactNo").toString());
        }
        catch(Exception e) { Toast.makeText(Account_TBPartner.this, e.getMessage().toString(), Toast.LENGTH_LONG).show(); }
    }

    private void InitiateControls(){
        fname=findViewById(R.id.myAcc_firstname);
        lname=findViewById(R.id.myAcc_lastname);
        mname=findViewById(R.id.myAcc_Middlename);
        uname=findViewById(R.id.myAcc_username);
        email = findViewById(R.id.myAcc_email);
        contact=findViewById(R.id.myAcc_ContactNo);
        partner_id=findViewById(R.id.myAcc_partner_ID);
        //btn= findViewById(R.id.btnsave);
        back = (ImageButton) findViewById(R.id.btn_Back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }

        });

        SharedPreferences s = getSharedPreferences("session", 0);
        Gson gson = new Gson();
        String json = s.getString("partner", "");

        PartnerClass partner = gson.fromJson(json, PartnerClass.class);

        fname.setText(partner.FirstName);
        lname.setText(partner.LastName);
        mname.setText(partner.MiddleName);
        uname.setText(partner.GetUsername());
        email.setText(partner.Email);
        contact.setText(partner.Contact_No);
        partner_id.setText(partner.TP_ID);
    }
}
