package com.example.android.tbcare_capstone.AccountActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tbcare_capstone.R;

public class Answer_Security_QuestionsActivity extends AppCompatActivity {


    private EditText answer;
    private TextView question;
    private Button submitBtn;
    private Button changeBtn;
    private String[] questions;
    private String[] answers;
    private String username;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpass_sec);

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();

        questions = new String[bundle.getStringArray("questions").length];
        answers = new String[bundle.getStringArray("answers").length];
        username = bundle.getString("username");

        questions = bundle.getStringArray("questions");
        answers = bundle.getStringArray("answers");

        InstantiateControls();

    }

    private void InstantiateControls(){
        answer = findViewById(R.id.input_answer1);
        question = findViewById(R.id.txtsec1);
        submitBtn = findViewById(R.id.submitBtn);
        changeBtn = findViewById(R.id.changeBtn);
        question.setText(questions[index]);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validator();
            }
        });
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(index == 0)
                {
                    index = 1;
                }
                else if(index == 1)
                    index = 0;

                question.setText(questions[index]);
            }
        });

    }

    public void Validator(){
        if(answer.getText().toString().equals(answers[index])){
            Toast.makeText(this, "Answer is correct", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ForgotPassword_ChangePassActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            intent.putExtras(bundle);

            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Incorrect", Toast.LENGTH_LONG).show();
        }
    }
}
