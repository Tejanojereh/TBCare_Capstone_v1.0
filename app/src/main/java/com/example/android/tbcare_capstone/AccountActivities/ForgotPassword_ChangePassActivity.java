package com.example.android.tbcare_capstone.AccountActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.tbcare_capstone.Class.Account;
import com.example.android.tbcare_capstone.Class.WebServiceClass;
import com.example.android.tbcare_capstone.MainActivity;
import com.example.android.tbcare_capstone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPassword_ChangePassActivity extends AppCompatActivity implements WebServiceClass.Listener {

    private EditText newPass;
    private EditText confirmPass;
    private Button submitBtn;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_changepass);

        newPass = findViewById(R.id.input_newpassword);
        confirmPass = findViewById(R.id.input_confirmpassword);
        submitBtn = findViewById(R.id.submitBtn);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();

        username = bundle.getString("username");


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
                    String address = "http://tbcarephp.azurewebsites.net/forgetPass_change.php";
                    String[] value = {username, account.hash(newPass.getText().toString())};
                    String[] valueName = {"username", "new_password"};

                    WebServiceClass service = new WebServiceClass(address, value, valueName, ForgotPassword_ChangePassActivity.this, ForgotPassword_ChangePassActivity.this);
                    service.execute();
                }

            }
        });

    }

    public boolean Validator(){
        if(newPass.getText().length() < 8)
        {
            newPass.setError("Password must be atleast 8 characters");
            confirmPass.requestFocus();
            return false;
        }
        else if(!newPass.getText().toString().equals(confirmPass.getText().toString())){
            confirmPass.setError("Password doesn't match");
            confirmPass.requestFocus();
            return false;
        }
        else
            return true;
    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        if(flag){
            if(Result != null)
            {
                try {
                    JSONObject object = Result.getJSONObject(0);
                    String success = object.getString("success");
                    String message = object.getString("message");

                    if(success.equals("true"))
                    {
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }else if(success.equals("false")){
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
