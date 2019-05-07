package com.example.android.tbcare_capstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.android.tbcare_capstone.Class.PartnerClass;
import com.example.android.tbcare_capstone.Class.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONObject;


public class RegisterPartner extends AppCompatActivity implements WebServiceClass.Listener {

    private EditText firstname, lastname, middlename, username, txtemail,txtpass,txtcpass, partner_id, answer_1, answer_2, contact_no;
    private Button btnregister;
    private ProgressBar loading;
    Spinner sec_question_1, sec_question_2;
    private Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_partner);

        loading = findViewById(R.id.loading);
        firstname = findViewById(R.id.input_nameF);
        lastname = findViewById(R.id.input_nameL);
        middlename = findViewById(R.id.input_nameM);
        username = findViewById(R.id.input_username);
        txtemail = findViewById(R.id.email);
        partner_id = findViewById(R.id.partner_id);
        txtpass = findViewById(R.id.input_password);
        txtcpass = findViewById(R.id.input_confirmpassword);
        btnregister = findViewById(R.id.btnNext);
        contact_no = findViewById(R.id.contact_no);
        sec_question_1 = (Spinner)findViewById(R.id.security_question_1);
        sec_question_2 = (Spinner)findViewById(R.id.security_question_2);
        answer_1 = findViewById(R.id.security_answer_1);
        answer_2 = findViewById(R.id.security_answer_2);

        ArrayList<String> list = new ArrayList<>();
        list.add("What was your childhood nickname?");
        list.add("What is your maternal grandmother's maiden name?");
        list.add("In what city or town did your mother and father meet?");
        list.add("Who is your childhood sports hero?");
        //list.add("What was your childhood nickname?");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,list);
        sec_question_1.setAdapter(adapter);
        sec_question_2.setAdapter(adapter);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtpass.getText().toString().equals(txtcpass.getText().toString()))
                {
                    PartnerClass partner = new PartnerClass();
                    // = txtname.getText().toString();
                    partner.SetUsername(username.getText().toString());
                    partner.SetPassword(txtpass.getText().toString());
                    partner.FirstName = firstname.getText().toString();
                    partner.LastName = lastname.getText().toString();
                    partner.MiddleName = middlename.getText().toString();
                    partner.Email = txtemail.getText().toString();
                    partner.TP_ID = partner_id.getText().toString();
                    partner.Contact_No = contact_no.getText().toString();
                    partner.Security_Question1 = sec_question_1.getSelectedItem().toString();
                    partner.Security_Question2 = sec_question_2.getSelectedItem().toString();
                    partner.Security_Answer1 = answer_1.getText().toString();
                    partner.Security_Answer2 = answer_2.getText().toString();

                    String password = partner.GetPassword();
                    String address = "http://tbcarephp.azurewebsites.net/register_account.php";
                    String[] value = {"PARTNER", partner.TP_ID, partner.FirstName, partner.FirstName, partner.Contact_No, partner.Email, partner.TP_ID, partner.GetPassword(), partner.Security_Question1, partner.Security_Answer1};
                    String[] valueName = {"account_type", "partner_id", "firstname", "lastname", "contactNo", "email", "username", "password", "question", "answer"};
                    WebServiceClass wbc = new WebServiceClass(address, value, valueName, RegisterPartner.this, RegisterPartner.this);

                    wbc.execute();
                }
                else
                    Toast.makeText(RegisterPartner.this, "Password does not match", Toast.LENGTH_LONG).show();
            }

            /*private void Register(){
                loading.setVisibility(View.VISIBLE);
                btnregister.setVisibility(View.GONE);
            }*/
        });
    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        if(flag)
        {
            if (Result != null) {
                try {
                    JSONObject object = Result.getJSONObject(0);
                    String success = object.getString("success");
                    //Intent intent = new Intent(this, MainActivity.class);

                    if(success.equals("true"))
                    {
                        Toast.makeText(this, "Registered Successfully", Toast.LENGTH_LONG).show();
                        //startActivity(intent);
                        finish();
                    }else if(success.equals("false")){
                        Toast.makeText(this, "Error occured!", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(RegisterPartner.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(RegisterPartner.this, "Error occured!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(RegisterPartner.this, "Error occured!", Toast.LENGTH_LONG).show();
        }

    }
}
