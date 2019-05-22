package com.tbsense.android.tbcare_capstone.AccountActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.tbsense.android.tbcare_capstone.R;

public class Choose_Account_Type extends AppCompatActivity implements View.OnClickListener {

    ImageButton partnerBtn, patientBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_account_type);

        partnerBtn = (ImageButton)findViewById(R.id.partnerImgBtn);
        patientBtn = (ImageButton)findViewById(R.id.patientImgBtn);

        partnerBtn.setOnClickListener(this);
        patientBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch(v.getId())
        {
            case R.id.partnerImgBtn:
                intent = new Intent(this, RegisterPartner.class);
                startActivity(intent);
                finish();
                break;
            case R.id.patientImgBtn:
                intent = new Intent(this, RegisterPatientStart.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
