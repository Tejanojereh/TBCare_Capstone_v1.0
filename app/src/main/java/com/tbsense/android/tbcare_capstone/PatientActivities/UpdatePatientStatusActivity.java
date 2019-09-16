package com.tbsense.android.tbcare_capstone.PatientActivities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.tbsense.android.tbcare_capstone.R;

public class UpdatePatientStatusActivity extends AppCompatActivity {

    private Switch smokeSwitch, faternitySwitch;
    private Button updtBtn;
    private boolean isPregnant, isSmoker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_patient_status);
    }

    private void InitiateControls(){
        SharedPreferences s = getSharedPreferences("session", 0);
        int session = s.getInt("account_id", 0);
        isPregnant = s.getBoolean("isPregnant", false);
        isSmoker = s.getBoolean("isSmoker", false);
        smokeSwitch = (Switch)findViewById(R.id.smokeSwitch);
        faternitySwitch = (Switch)findViewById(R.id.faternitySwitch);
        updtBtn = (Button)findViewById(R.id.updtBtn);
        if(isPregnant)
        {
            faternitySwitch.setChecked(true);
        }
        else
            faternitySwitch.setChecked(false);

        if(isSmoker)
        {
            smokeSwitch.setChecked(true);
        }
        else
            smokeSwitch.setChecked(false);

        updtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = s.edit();
                if(smokeSwitch.isChecked())
                {
                    editor.putBoolean("isSmoker", true);

                }
                else
                {
                    editor.putBoolean("isSmoker", false);

                }
                if(faternitySwitch.isChecked())
                {
                    editor.putBoolean("isPregnant", true);
                }
                else
                {
                    editor.putBoolean("isPregnant", false);
                }
            }
        });

    }
}
