package com.example.android.tbcare_capstone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtname,txtemail,txtpass,txtcpass;
    private Button btnregister;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loading = findViewById(R.id.loading);
        txtname = findViewById(R.id.input_name);
        txtpass = findViewById(R.id.input_password);
        txtcpass = findViewById(R.id.input_confirmpassword);
        btnregister = findViewById(R.id.btnregister);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

            private void Register(){
                loading.setVisibility(View.VISIBLE);
                btnregister.setVisibility(View.GONE);


            }


        });






    }

}
