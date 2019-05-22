package com.tbsense.android.tbcare_capstone.AccountActivities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tbsense.android.tbcare_capstone.Class.Account;
import com.tbsense.android.tbcare_capstone.Class.WebServiceClass;
import com.tbsense.android.tbcare_capstone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity implements WebServiceClass.Listener {

    private Button submitBtn;
    private EditText oldPass, newPass, confirmPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepass);

        InstantiateControls();
    }

    public void InstantiateControls(){
        oldPass = findViewById(R.id.usernameTxt);
        newPass = findViewById(R.id.input_newpassword);
        confirmPass = findViewById(R.id.input_confirmpassword);
        submitBtn = findViewById(R.id.submitBtn);

        SharedPreferences s = getSharedPreferences("session", 0);
        final String account_id = s.getString("id", "");

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validator())
                {
                    Account account = new Account() {
                        @Override
                        public String hash(String s) {
                            return super.hash(s);
                        }
                    };
                    String address = "http://tbcarephp.azurewebsites.net/changePass.php";
                    String[] value = {account_id, account.hash(oldPass.getText().toString()), account.hash(newPass.getText().toString())};
                    String[] valueName = {"account_id", "old_password", "new_password"};

                    WebServiceClass service = new WebServiceClass(address, value, valueName, ChangePasswordActivity.this, ChangePasswordActivity.this);
                    service.execute();
                }
            }
        });
    }

    public boolean Validator(){
        boolean flag = true;
        if(oldPass.getText().toString().equals("")){
            oldPass.setError("FIELD CANNOT BE EMPTY");
            oldPass.requestFocus();
            flag = false;
        }
        if(newPass.getText().toString().equals("")) {
            newPass.setError("FIELD CANNOT BE EMPTY");
            newPass.requestFocus();
            flag = false;
        }
        if(newPass.getText().toString().contains(oldPass.getText().toString())){
            newPass.setError("New password must not contain old password");
            newPass.requestFocus();
            flag = false;
        }
        if(newPass.length() < 8){
            newPass.requestFocus();
            newPass.setError("Password must be 8 characters or more");
            flag = false;
        }
        else if(!newPass.getText().toString().equals(confirmPass.getText().toString()))
        {
            confirmPass.requestFocus();
            confirmPass.setError("PASSWORD DOESN'T MATCH");
            flag = false;
        }
        return flag;

    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        if(flag)
        {
            if(Result != null){

                try {
                    JSONObject object = Result.getJSONObject(0);
                    String success = object.getString("success");
                    String message = object.getString("message");

                    if(success.equals("false")){
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                    }
                    else if(success.equals("true")){
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            else
                Toast.makeText(this, "Error occured", Toast.LENGTH_LONG).show();

        }
        else
            Toast.makeText(this, "Error occured", Toast.LENGTH_LONG).show();

    }
}
