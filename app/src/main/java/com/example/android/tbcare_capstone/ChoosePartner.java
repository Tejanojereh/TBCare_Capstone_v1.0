package com.example.android.tbcare_capstone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ChoosePartner extends AppCompatActivity implements WebServiceClass.Listener {


    String[] pid;
    String[] pname;
    String[] patientshandled;
    String[] account_Pid;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choosepartner);

        String address = "http://tbcarephp.azurewebsites.net/retrieve_partnerList.php";
        String[] value = {};
        String[] valueName = {};
        WebServiceClass wbc = new WebServiceClass(address, value, valueName, ChoosePartner.this, ChoosePartner.this);

        wbc.execute();

    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {

        pid = new String[Result.length()];
        pname = new String[Result.length()];
        patientshandled = new String[Result.length()];
        account_Pid = new String[Result.length()];
        if(flag)
        {
            if(Result != null)
            {
                for(int i = 0; i < Result.length(); i++)
                {
                    try {

                        JSONObject object = Result.getJSONObject(i);
                        pid[i] = object.getString("TP_ID");
                        pname[i] = object.getString("Firstname") +" "+ object.getString("Lastname");
                        patientshandled[i] = object.getString("no_patients");
                        account_Pid[i] = object.getString("ID");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                lv = findViewById(R.id.listview1);
                ChoosePartner.MyAdapter adapter = new ChoosePartner.MyAdapter(this, pid, pname, patientshandled);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(ChoosePartner.this, account_Pid[position], Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String[] pid;
        String[] pname;
        String[] patientshandled;

        MyAdapter(Context c, String id[], String name[], String[] patientshandled) {
            super(c, R.layout.choosepartner_listview, R.id.linearLayoutID, id);
            this.context = c;
            this.pid = id;
            this.pname = name;
            this.patientshandled = patientshandled;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.choosepartner_listview, parent, false);
            TextView ids = row.findViewById(R.id.txtid);
            TextView names = row.findViewById(R.id.txtname);
            TextView patientsHandled = row.findViewById(R.id.txtpatientshandled);

            ids.setText(pid[position]);
            names.setText(pname[position]);
            patientsHandled.setText(patientshandled[position]);



            return row;
        }

    }
}
