package com.tbsense.android.tbcare_capstone.PartnerActivities;

import android.content.Context;
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

import com.tbsense.android.tbcare_capstone.Class.WebServiceClass;
import com.tbsense.android.tbcare_capstone.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class View_PatientNotes extends AppCompatActivity implements WebServiceClass.Listener {

    ListView viewNotes;
    String[] patientNotes, notesDateTime, idArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_notes);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("patient_id");
        String address = "http://tbcarephp.azurewebsites.net/retrieve_patient_notes.php";
        String[] value = {id};
        String[] valueName = {"patient_id"};
        WebServiceClass wbc = new WebServiceClass(address, value, valueName, View_PatientNotes.this, View_PatientNotes.this);
        wbc.execute();
    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {
        if(flag)
        {
            if(Result != null)
            {
                patientNotes = new String[Result.length()];
                notesDateTime = new String[Result.length()];
                idArray = new String[Result.length()];
                try {
                    JSONObject object = Result.getJSONObject(0);
                    String hasNotes = object.getString("hasNotes");
                    if(hasNotes.equals("true")){
                        for(int i = 0; i < Result.length(); i++)
                        {
                            object = Result.getJSONObject(i);
                            patientNotes[i] = object.getString("notes");
                            JSONObject o = object.getJSONObject("date");
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                            SimpleDateFormat final_format = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
                            try {
                                Date d = df.parse(o.getString("date"));

                                notesDateTime[i] = final_format.format(d);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                        viewNotes = findViewById(R.id.listView_notes);
                        MyAdapter adapter = new MyAdapter(this, idArray, patientNotes, notesDateTime);
                        viewNotes.setAdapter(adapter);
                    }
                    else if(hasNotes.equals("false"))
                    {
                        Toast.makeText(this, object.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
                Toast.makeText(this, "Error occured", Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this, "Error occured", Toast.LENGTH_LONG).show();
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        String[] notesArray;
        String[] dateTime;

        MyAdapter(Context c, String[] id, String[] notes, String[] date) {
            super(c, R.layout.notes_listview, R.id.listView_notes, id);
            this.context = c;
            this.notesArray = notes;
            this.dateTime = date;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertview, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.notes_listview, parent, false);

            TextView datetime = row.findViewById(R.id.tvDateTime);
            TextView notes = row.findViewById(R.id.tvNotes);

            datetime.setText(dateTime[position]);
            notes.setText(notesArray[position]);

            return row;
        }

    }
}
