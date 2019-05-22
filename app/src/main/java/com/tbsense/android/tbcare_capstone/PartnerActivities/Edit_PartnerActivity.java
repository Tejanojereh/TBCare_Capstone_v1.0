package com.tbsense.android.tbcare_capstone.PartnerActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tbsense.android.tbcare_capstone.Class.PartnerClass;
import com.tbsense.android.tbcare_capstone.Class.WebServiceClass;
import com.tbsense.android.tbcare_capstone.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Edit_PartnerActivity extends AppCompatActivity implements WebServiceClass.Listener {

    private EditText firstname, lastname, middlename, username, txtemail, contact_no;
    private FloatingActionButton save;
    private String account_id, partner_id;
    private PartnerClass partnerClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editpartner);
        InstantiateControls();


    }

    public void InstantiateControls()
    {
        firstname = findViewById(R.id.firstNameTxt);
        lastname = findViewById(R.id.lastNameTxt);
        middlename = findViewById(R.id.middleNameTxt);
        username = findViewById(R.id.usernameTxt);
        txtemail = findViewById(R.id.emailTxt);
        contact_no = findViewById(R.id.contactNoTxt);
        save =findViewById(R.id.saveBtnpartner);

        SharedPreferences s = getSharedPreferences("session", 0);
        account_id = s.getString("id", "");
        partner_id = Integer.toString(s.getInt("account_id", 0));
        Gson gson = new Gson();
        String json = s.getString("class", "");
        String name = "";
        partnerClass = gson.fromJson(json, PartnerClass.class);

        if(partnerClass.MiddleName.equals(null))
        {
            middlename.setText("");
        }
        else
            middlename.setText(partnerClass.MiddleName);

        firstname.setText(partnerClass.FirstName);
        lastname.setText(partnerClass.LastName);
        username.setText(partnerClass.GetUsername());
        txtemail.setText(partnerClass.Email);
        contact_no.setText(partnerClass.Contact_No);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validator())
                {
                    PartnerClass partner = new PartnerClass();
                    partner.SetUsername(username.getText().toString());
                    partner.FirstName = firstname.getText().toString();
                    partner.LastName = lastname.getText().toString();
                    if(middlename.getText().toString().equals("")){
                        partner.MiddleName = "";
                    }else{
                        partner.MiddleName = middlename.getText().toString();
                    }
                    partner.Email = txtemail.getText().toString();
                    partner.Contact_No = contact_no.getText().toString();
                    String address = "http://tbcarephp.azurewebsites.net/edit_partnerinfo.php";
                    String[] value = {partner_id, account_id, partner.FirstName, partner.MiddleName, partner.LastName, partner.Contact_No, partner.Email, partner.GetUsername()};
                    String[] valueName = {"partner_id", "account_id","firstname", "middlename", "lastname", "contact_no", "email", "username"};
                    WebServiceClass wbc = new WebServiceClass(address, value, valueName, Edit_PartnerActivity.this, Edit_PartnerActivity.this);

                    wbc.execute();
                }
            }
        });
    }

    public boolean Validator(){

        boolean flag = true;
        if(firstname.getText().toString().length() == 0)
        {
            firstname.requestFocus(); firstname.setError("Filed cannot be empty");flag = false;
        }
        if(lastname.getText().toString().length() == 0){lastname.requestFocus(); lastname.setError("Filed cannot be empty");flag = false;}
        if(username.getText().toString().length() == 0){username.requestFocus(); username.setError("Filed cannot be empty");flag = false;}
        if(txtemail.getText().toString().length() == 0){txtemail.requestFocus(); txtemail.setError("Filed cannot be empty");flag = false;}
        return flag;
    }


    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        if(flag)
        {
            if(Result != null)
            {
                try {
                    JSONObject object = Result.getJSONObject(0);
                    String success = object.getString("success");
                    if(success.equals("true"))
                    {
                        partnerClass.MiddleName = middlename.getText().toString();
                        partnerClass.FirstName = firstname.getText().toString();
                        partnerClass.LastName = lastname.getText().toString();
                        partnerClass.SetUsername(username.getText().toString());
                        partnerClass.Email = txtemail.getText().toString();
                        partnerClass.Contact_No = contact_no.getText().toString();

                        SharedPreferences s = getSharedPreferences("session", 0);
                        SharedPreferences.Editor editor = s.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(partnerClass);
                        editor.putString("class", json);
                        editor.apply();

                        Intent intent = new Intent(Edit_PartnerActivity.this, Account_TBPartner.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else if(success.equals("false"))
                    {
                        Toast.makeText(this, object.getString("message"), Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();

            }
        }
        else{
            Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
        }
    }
}
