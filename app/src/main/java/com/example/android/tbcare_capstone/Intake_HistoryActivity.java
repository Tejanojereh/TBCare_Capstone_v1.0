package com.example.android.tbcare_capstone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tbcare_capstone.Class.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Intake_HistoryActivity extends AppCompatActivity implements WebServiceClass.Listener {


    private ListView listView;
    private String[] actions, dateTime, mednames, idarray;
    private Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intake_history);

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("patient_id");
        String address = "http://tbcarephp.azurewebsites.net/retrieve_IntakeLog.php";
        String[] value = {id};
        String[] valueName = {"patient_id"};
        WebServiceClass wbc = new WebServiceClass(address, value, valueName, Intake_HistoryActivity.this, Intake_HistoryActivity.this);
        wbc.execute();
    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {

        if(flag){
            if(Result != null){
                actions = new String[Result.length()];
                mednames = new String[Result.length()];
                dateTime = new String[Result.length()];
                idarray = new String[Result.length()];
                try {
                    JSONObject object1 = Result.getJSONObject(0);
                    String hasIntake = object1.getString("hasIntake");
                    if(hasIntake.equals("true")){
                        for(int i = 0; i < Result.length(); i++){
                            JSONObject object = Result.getJSONObject(i);
                            actions[i] = object.getString("status");
                            if(object.getString("drugID").toString().equals("1")){
                                mednames[i] = "QuadMax";
                            }else if(object.getString("drugID").toString().equals("2")){
                                mednames[i] = "DuoMax";
                            }
                            JSONObject o = object.getJSONObject("datetime");
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                            SimpleDateFormat final_format = new SimpleDateFormat("MMM dd, yyyy HH:mm a");
                            try {
                                Date d = df.parse(o.getString("date"));

                                dateTime[i] = final_format.format(d);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }

                        }
                        listView = findViewById(R.id.ph_history);
                        MyAdapter adapter = new MyAdapter(this, idarray, actions, dateTime, mednames);
                        listView.setAdapter(adapter);
                    }
                    else if(hasIntake.equals("false")){
                        Toast.makeText(this, "No Intake Log history", Toast.LENGTH_LONG).show();
                        finish();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else
                Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "Error occured", Toast.LENGTH_SHORT).show();

    }


    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String[] actions;
        String[] medicinenames;
        String[] dateTime;

        MyAdapter(Context c, String[] id, String[] actions_, String[] date, String[] mednames) {
            super(c, R.layout.intake_history_listview, R.id.linearLayoutID, id);
            this.context = c;
            this.actions = actions_;
            this.dateTime = date;
            this.medicinenames = mednames;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.intake_history_listview, parent, false);
            TextView datetime = row.findViewById(R.id.txtdatetime);
            TextView action = row.findViewById(R.id.txtAction);
            TextView medicineName = row.findViewById(R.id.txtMedicineName);

            datetime.setText(dateTime[position]);
            action.setText(actions[position]);
            medicineName.setText(medicinenames[position]);

            return row;
        }

    }
}
