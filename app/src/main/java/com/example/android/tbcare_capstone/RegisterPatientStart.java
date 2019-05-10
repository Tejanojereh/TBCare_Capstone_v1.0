package com.example.android.tbcare_capstone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.tbcare_capstone.Class.PatientClass;
import com.example.android.tbcare_capstone.Class.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RegisterPatientStart extends AppCompatActivity implements WebServiceClass.Listener {

    Button btnRegister;
    Spinner question1, question2;
    EditText answer1, answer2, weight, password, confirmPass, username, contactNo, patient_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_patient_start);

        InstantiateControls();

    }

    public void InstantiateControls(){
        btnRegister = findViewById(R.id.register);
        username = findViewById(R.id.input_username);
        password = findViewById(R.id.input_password);
        confirmPass = findViewById(R.id.input_confirmpassword);
        contactNo = findViewById(R.id.input_contactnum);
        weight = findViewById(R.id.input_weight);
        patient_id = findViewById(R.id.input_idpatient);
        question1 = findViewById(R.id.question1);
        question2 = findViewById(R.id.question2);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);

        ArrayList<String> list = new ArrayList<>();
        list.add("What was your childhood nickname?");
        list.add("What is your maternal grandmother's maiden name?");
        list.add("In what city or town did your mother and father meet?");
        list.add("Who is your childhood sports hero?");
        //list.add("What was your childhood nickname?");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,list);
        question1.setAdapter(adapter);
        question2.setAdapter(adapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validator()){
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    try {
                        Date today = df.parse(df.format(c));
                        PatientClass patient = new PatientClass();
                        patient.SetUsername(username.getText().toString());
                        patient.SetPassword(password.getText().toString());
                        patient.TB_CASE_NO = patient_id.getText().toString();
                        patient.Weight = Float.parseFloat(weight.getText().toString());
                        patient.Contact_No = contactNo.getText().toString();
                        patient.Disease_Classification = "Sample";
                        patient.Registration_Group = "Sample";
                        patient.Treatment_Date_Start = today;
                        patient.Security_Question1 = question1.getSelectedItem().toString();
                        patient.Security_Question2 = question2.getSelectedItem().toString();
                        patient.Security_Answer1 = answer1.getText().toString();
                        patient.Security_Answer2 = answer2.getText().toString();

                        String address = "http://tbcarephp.azurewebsites.net/register_account.php";
                        String[] value = {"PATIENT", patient.TB_CASE_NO, Float.toString(patient.Weight), df.format(patient.Treatment_Date_Start), patient.Registration_Group, patient.Disease_Classification, patient.Contact_No, patient.GetUsername(), patient.GetPassword(), patient.Security_Question1, patient.Security_Answer1, patient.Security_Question2, patient.Security_Answer2};
                        String[] valueName = {"account_type", "patient_id", "weight", "treatment_date_start", "registration_group", "disease_classification", "contactNo", "username", "password", "question1", "answer1", "question2", "answer2"};
                        WebServiceClass wbc = new WebServiceClass(address, value, valueName, RegisterPatientStart.this, RegisterPatientStart.this);

                        wbc.execute();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }



            }
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
                        Toast.makeText(this, object.getString("message"), Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(RegisterPatientStart.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(RegisterPatientStart.this, "Error occured!", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(RegisterPatientStart.this, "Error occured!", Toast.LENGTH_LONG).show();
        }
    }

    private boolean Validator(){
        boolean flag = true;
        if(username.length()==0)
        {
            username.requestFocus();
            username.setError("FIELD CANNOT BE EMPTY");
            flag = false;
        }
        if(answer2.length() == 0){
            answer2.requestFocus();
            answer2.setError("FIELD CANNOT BE EMPTY");
            flag = false;
        }
        if(answer1.length() == 0){
            answer1.requestFocus();
            answer1.setError("FIELD CANNOT BE EMPTY");
            flag = false;
        }
        if(patient_id.length() == 0){
            patient_id.requestFocus();
            patient_id.setError("FIELD CANNOT BE EMPTY");
            flag = false;
        }
        if(weight.length() == 0){
            weight.requestFocus();
            weight.setError("FIELD CANNOT BE EMPTY");
            flag = false;
        }
        if(password.length() < 8){
            password.requestFocus();
            password.setError("Password must be 8 characters or more");
            flag = false;
        }
        else if(!password.getText().toString().equals(confirmPass.getText().toString()))
        {
            confirmPass.requestFocus();
            confirmPass.setError("PASSWORD DOESN'T MATCH");
            flag = false;
        }

        return flag;
    }

}
