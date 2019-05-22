package com.tbsense.android.tbcare_capstone;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tbsense.android.tbcare_capstone.AccountActivities.Answer_Security_QuestionsActivity;
import com.tbsense.android.tbcare_capstone.Class.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPassword extends AppCompatActivity implements WebServiceClass.Listener
{

    private EditText username;
    private Button submit;



    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_username);

        username = findViewById(R.id.usernameTxt);
        submit = findViewById(R.id.submitBtn);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address = "http://tbcarephp.azurewebsites.net/retrieve_securityinfo.php";
                String[] value = {username.getText().toString()};
                String[] valueName = {"username"};

                WebServiceClass service = new WebServiceClass(address, value, valueName, ForgotPassword.this, ForgotPassword.this);
                service.execute();

            }
        });


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
                    if(success.equals("false")){
                        Toast.makeText(this, object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                    else if(success.equals("true")){
                        Intent intent = new Intent(this, Answer_Security_QuestionsActivity.class);
                        String[] questions = {object.getString("question1"), object.getString("question2")};
                        String[] answers = {object.getString("answer1"), object.getString("answer2")};
                        Bundle bundle = new Bundle();

                        bundle.putStringArray("questions", questions);
                        bundle.putStringArray("answers", answers);
                        bundle.putString("username", username.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else
            Toast.makeText(this, "Error occured", Toast.LENGTH_LONG).show();
    }



}