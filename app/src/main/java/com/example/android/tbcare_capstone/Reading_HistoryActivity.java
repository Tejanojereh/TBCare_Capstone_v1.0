package com.example.android.tbcare_capstone;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class Reading_HistoryActivity extends AppCompatActivity implements WebServiceClass.Listener {

    private ListView listView;
    private String[] phLevel, dateTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deviceoutput_sputumresults);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("patient_id");
        String address = "http://tbcarephp.azurewebsites.net/retrieve_ReadingHistory.php";
        String[] value = {id};
        String[] valueName = {"id"};
        WebServiceClass wbc = new WebServiceClass(address, value, valueName, Reading_HistoryActivity.this, Reading_HistoryActivity.this);
        wbc.execute();
    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {

        if(flag){
            if(Result != null){
                phLevel = new String[Result.length()];
                dateTime = new String[Result.length()];
                try {
                    JSONObject object1 = Result.getJSONObject(0);
                    String hasIntake = object1.getString("hasIntake");
                    if(hasIntake.equals("true")){
                        for(int i = 0; i < Result.length(); i++){
                            JSONObject object = Result.getJSONObject(i);
                            phLevel[i] = object.getString("ph_level");
                            JSONObject o = object.getJSONObject("treatment_date_start");
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                            SimpleDateFormat final_format = new SimpleDateFormat("MMM dd, yyyy");
                            try {
                                Date d = df.parse(o.getString("datetime"));

                                dateTime[i] = final_format.format(d);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }

                        }
                        listView = findViewById(R.id.ph_history);
                        MyAdapter adapter = new MyAdapter(this, phLevel, dateTime);
                        listView.setAdapter(adapter);
                    }
                    else if(hasIntake.equals("false")){
                        Toast.makeText(this, object1.getString("message"), Toast.LENGTH_SHORT).show();
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
        String[] ph_level;
        String[] dateTime;

        MyAdapter(Context c, String[] level, String[] date) {
            super(c, R.layout.row_listview, R.id.linearLayoutID);
            this.context = c;
            this.ph_level = level;
            this.dateTime = date;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.deviceoutput_sputumresults_listview, parent, false);
            TextView datetime = row.findViewById(R.id.txtdatetime);
            TextView phreading = row.findViewById(R.id.txtphreading);

            datetime.setText(dateTime[position]);
            phreading.setText(ph_level[position]);

            return row;
        }

    }
}
