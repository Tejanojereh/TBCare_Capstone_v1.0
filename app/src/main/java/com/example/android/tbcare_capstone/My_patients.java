package com.example.android.tbcare_capstone;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class My_patients extends AppCompatActivity {

    //OPTION A- VIEW MY PATIENTS

ListView listView;
String patientsid[] = {"TB234321", "TB343234"};
String patientsname[] = {"Chi", "Chow"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patients);

        listView = findViewById(R.id.listview1);

        MyAdapter adapter = new MyAdapter(this,patientsid, patientsname);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position ==0){
                    Toast.makeText(My_patients.this, "Description1", Toast.LENGTH_SHORT).show();
                }
                if (position ==0){
                    Toast.makeText(My_patients.this, "Description2", Toast.LENGTH_SHORT).show();
                }
            }
        });


}

        class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String pid[];
        String pname[];

        MyAdapter (Context c,String id[], String name[]) {
            super(c, R.layout.row_listview, R.id.linearLayoutID, id);
            this.context = c;
            this.pid=id;
            this.pname=id;

        }

        @NonNull
            @Override
            public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent){
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_listview,parent,false);
            TextView ids= row.findViewById(R.id.txtid);
            TextView names= row.findViewById(R.id.txtname);

            ids.setText(pid[position]);
            names.setText(pname[position]);


            return row;
        }

        }


    }
