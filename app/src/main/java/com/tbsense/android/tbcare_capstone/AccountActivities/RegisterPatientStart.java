package com.tbsense.android.tbcare_capstone.AccountActivities;

import android.app.DatePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tbsense.android.tbcare_capstone.Class.PatientClass;
import com.tbsense.android.tbcare_capstone.Class.Utility.DatepickerFragment;
import com.tbsense.android.tbcare_capstone.Class.WebServiceClass;
import com.tbsense.android.tbcare_capstone.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RegisterPatientStart extends AppCompatActivity implements WebServiceClass.Listener, DatePickerDialog.OnDateSetListener {

    private Button btnRegister, btnDate;
    Spinner question1, question2, reg_group;
    private EditText answer1, answer2, weight, password, confirmPass, username, contactNo, patient_id;
    private TextView dateTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_patient_start);

        InstantiateControls();

    }

    public void InstantiateControls(){
        btnRegister = findViewById(R.id.btnregister);
        btnDate = findViewById(R.id.btndateregister);
        username = findViewById(R.id.input_username);
        password = findViewById(R.id.usernameTxt);
        confirmPass = findViewById(R.id.input_confirmpassword);
        contactNo = findViewById(R.id.input_contactnum);
        weight = findViewById(R.id.input_weight);
        reg_group = findViewById(R.id.spn_group);
        patient_id = findViewById(R.id.input_idpatient);
        question1 = findViewById(R.id.question1);
        question2 =  findViewById(R.id.question2);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);

        ArrayList<String> list = new ArrayList<>();
        list.add("What was your childhood nickname?");
        list.add("What is your maternal grandmother's maiden name?");
        list.add("In what city or town did your mother and father meet?");
        list.add("Who is your childhood sports hero?");

        ArrayList<String> reg_groupList = new ArrayList<>();
        reg_groupList.add("New");
        reg_groupList.add("Relapse");
        //list.add("What was your childhood nickname?");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,list);
        question1.setAdapter(adapter);
        question2.setAdapter(adapter);
        question1.setSelection(0, true);
        question2.setSelection(0, true);

        ArrayAdapter<String> adapter_regGroup = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,reg_groupList);
        reg_group.setAdapter(adapter_regGroup);
        reg_group.setSelection(0, true);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatepickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validator()){
                    SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                    try {
                        Date today = df.parse(dateTxt.getText().toString());
                        PatientClass patient = new PatientClass();
                        patient.Registration_Group = reg_group.getSelectedItem().toString();
                        patient.SetUsername(username.getText().toString());
                        patient.SetPassword(password.getText().toString());
                        patient.Security_Question1 = question1.getSelectedItem().toString();
                        patient.Security_Question2 = question2.getSelectedItem().toString();
                        patient.TB_CASE_NO = patient_id.getText().toString();
                        patient.Weight = Float.parseFloat(weight.getText().toString());
                        patient.Contact_No = contactNo.getText().toString();
                        patient.Disease_Classification = "Pulmonary";
                        patient.Treatment_Date_Start = today;
                        patient.Security_Answer1 = answer1.getText().toString().toLowerCase();
                        patient.Security_Answer2 = answer2.getText().toString().toLowerCase();
                        String dated = df.format(today);
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
        if(username.getText().toString().contains(" ")){
            username.requestFocus();
            username.setError("Username cannot contain white spaces");
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        c.add(Calendar.MONTH, 1);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        String m="", d="";

        if(month < 10){

            m = "0" + month;
        }
        else
            m=Integer.toString(month);
        if(dayOfMonth < 10){

            d  = "0" + dayOfMonth ;
        }
        else
            d=Integer.toString(dayOfMonth);
        dateTxt = (TextView) findViewById(R.id.txtRegisterdate);
        dateTxt.setText(m + "-" + d + "-" + year);

    }

}
