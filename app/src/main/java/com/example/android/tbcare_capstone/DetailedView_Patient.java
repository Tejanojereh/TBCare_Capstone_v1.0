package com.example.android.tbcare_capstone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


import android.view.animation.Animation;
import android.view.animation.AnimationUtils;



public class DetailedView_Patient extends AppCompatActivity {


    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;
    private Boolean isFABOpen = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailedview_patient);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnfab);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.fab1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.fab2);
        floatingActionButton3 = (FloatingActionButton) findViewById(R.id.fab3);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });



    }
    private void showFABMenu(){
        isFABOpen=true;
        floatingActionButton1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        floatingActionButton2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        floatingActionButton3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu(){
        isFABOpen=false;
        floatingActionButton1.animate().translationY(0);
        floatingActionButton2.animate().translationY(0);
        floatingActionButton3.animate().translationY(0);
    }
}
