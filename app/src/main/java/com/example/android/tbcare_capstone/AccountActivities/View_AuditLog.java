package com.example.android.tbcare_capstone.AccountActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tbcare_capstone.Class.WebServiceClass;
import com.example.android.tbcare_capstone.DetailedView_Patient;
import com.example.android.tbcare_capstone.PartnerActivities.My_Patients;
import com.example.android.tbcare_capstone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class View_AuditLog extends AppCompatActivity implements WebServiceClass.Listener {

    private ListView listView;
    private Button btn_back;
    private String id;
    private String[] date_time, logAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audit_log);

        InstantiateControls();
    }

    private void InstantiateControls(){
        listView = findViewById(R.id.auditListview);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SharedPreferences s = getSharedPreferences("session", 0);
        id = s.getString("id", "");
        String address = "http://tbcarephp.azurewebsites.net/retrieve_auditlogs.php";
        String[] value = {id};
        String[] valueName = {"id"};
        WebServiceClass wbc = new WebServiceClass(address, value, valueName, View_AuditLog.this, View_AuditLog.this);

        wbc.execute();
    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {

        logAction = new String[Result.length()];
        date_time = new String[Result.length()];
        String[] idArray = new String[Result.length()];
        if(flag)
        {
            if(Result != null)
            {
                try {
                    JSONObject object = Result.getJSONObject(0);
                    String hasData = object.getString("hasData");

                    if(hasData.equals("true"))
                    {
                        for(int i = 0; i < Result.length(); i++)
                        {
                            object = Result.getJSONObject(i);
                            logAction[i] = object.getString("action");
                            JSONObject o = object.getJSONObject("datetime");
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                            SimpleDateFormat final_format = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
                            try {
                                Date d = df.parse(o.getString("date"));
                                date_time[i] = final_format.format(d);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                        AuditAdapter adapter = new AuditAdapter(this, idArray, logAction, date_time);
                        listView.setAdapter(adapter);
                    }
                    else if(hasData.equals("false")){
                        Toast.makeText(this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else{
                Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
        }

    }

    class AuditAdapter extends ArrayAdapter<String> {

        Context context;
        String[] actionArray;
        String[] datetimeArray;

        AuditAdapter(Context c, String[] id, String[] action, String[] datetime) {
            super(c, R.layout.audit_rowlistview, R.id.linearLayoutID, id);
            this.context = c;
            this.actionArray = action;
            this.datetimeArray = datetime;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = layoutInflater.inflate(R.layout.audit_rowlistview, parent, false);

            TextView datetime = row.findViewById(R.id.txtdatetime);
            TextView action = row.findViewById(R.id.txtaction);

            datetime.setText(datetimeArray[position]);
            action.setText(actionArray[position]);


            return row;
        }

    }
}
