package com.example.android.tbcare_capstone;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.example.android.tbcare_capstone.Class.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class My_Patients extends AppCompatActivity implements WebServiceClass.Listener {

    ListView listView;
    String patientsid[];
    String patientsname[];
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patients);

        SharedPreferences s = getSharedPreferences("session", 0);
        id = Integer.toString(s.getInt("account_id", 0));
        String address = "http://tbcarephp.azurewebsites.net/retrieve_patientList.php";
        String[] value = {id};
        String[] valueName = {"id"};
        WebServiceClass wbc = new WebServiceClass(address, value, valueName, My_Patients.this, My_Patients.this);

        wbc.execute();
    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        if(flag)
        {
            String patients_no;
            patientsid = new String[Result.length()];
            patientsname = new String[Result.length()];
            try {
                JSONObject object = Result.getJSONObject(0);
                patients_no = object.getString("patients_no");
                Toast.makeText(this, "You have no patients at the moment", Toast.LENGTH_LONG).show();
                //finish();
            } catch (JSONException e) {
                for (int i = 0; i < Result.length(); i++) {
                    try {
                        JSONObject object = Result.getJSONObject(i);
                        patientsid[i] = object.getString("TB_CASE_NO").toString();
                        patientsname[i] = object.getString("contact_no").toString();
                    } catch (JSONException er) {
                        er.printStackTrace();
                    }
                }
                listView = findViewById(R.id.listview1);
                MyAdapter adapter = new MyAdapter(this, patientsid, patientsname);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            Toast.makeText(My_Patients.this, "Description1", Toast.LENGTH_SHORT).show();
                        }
                        if (position == 0) {
                            Toast.makeText(My_Patients.this, "Description2", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String pid[];
        String pname[];

        MyAdapter(Context c, String id[], String name[]) {
            super(c, R.layout.row_listview, R.id.linearLayoutID, id);
            this.context = c;
            this.pid = id;
            this.pname = name;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_listview, parent, false);
            TextView ids = row.findViewById(R.id.txtid);
            TextView names = row.findViewById(R.id.tp_name);

            ids.setText(pid[position]);
            names.setText(pname[position]);


            return row;
        }

    }

}
