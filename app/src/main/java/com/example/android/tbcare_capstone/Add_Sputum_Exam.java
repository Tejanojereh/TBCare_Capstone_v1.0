package com.example.android.tbcare_capstone;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Add_Sputum_Exam extends AppCompatActivity implements View.OnClickListener{

    Button btnSubmit;
    ImageButton back;
    Spinner spinnerVisualAppearance, spinnerReading, spinnerDiagnosis, spinnerPatient;
    ArrayAdapter <CharSequence> arrayAdapterVisualAppearance, arrayAdapterReading, arrayAdapterDiagnose, arrayAdapterPatient;

    HttpClient httpclient; HttpResponse httpresponse; HttpPost httppost;
    StringBuffer stringbuffer = null; InputStream inputstream; BufferedReader bufferedreader;
    List<NameValuePair> nameValuePairs;

    String[] ss; byte[] data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sputum_exam);
        InstantiateControls();
        PopulateSpinner();

        btnSubmit.setOnClickListener(this);
        back = (ImageButton) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Sputum_Exam.this, Menu_TBPartner.class );
                startActivity(intent);
            }

        });


    }

    @Override
    public void onClick(View v) {
        //insert to DB
        new ExecuteTask2(this).execute();
    }

    private void InstantiateControls(){
        btnSubmit = (Button)findViewById(R.id.BtnSubmit);
        spinnerVisualAppearance = (Spinner)findViewById(R.id.SpinnerVisualAppearance);
        spinnerReading = (Spinner)findViewById(R.id.SpinnerReading);
        spinnerDiagnosis = (Spinner)findViewById(R.id.SpinnerDiagnosis);
        spinnerPatient = (Spinner)findViewById(R.id.SpinnerPatient);

        return;
    }
    private void PopulateSpinner() {
        String[] VisualAppearance = {"Muco-purulent", "Blood-Stained", "Salivary"};
        String[] Reading = {"0", "+n", "1+", "2+", "3+"};
        String[] Diagnosis = {"NEGATIVE", "POSITIVE"};

        arrayAdapterVisualAppearance = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, VisualAppearance);
        arrayAdapterVisualAppearance.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerVisualAppearance.setAdapter(arrayAdapterVisualAppearance);

        arrayAdapterReading = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, Reading);
        arrayAdapterReading.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerReading.setAdapter(arrayAdapterReading);

        arrayAdapterDiagnose = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, Diagnosis);
        arrayAdapterDiagnose.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerDiagnosis.setAdapter(arrayAdapterDiagnose);



        //TBPatient Spinner Populate
        new ExecuteTask(this).execute();
        return;
    }
    public String[] ReadResponse(String action) {
        try {
            String[] toReturn = null; int len =0;
            inputstream = httpresponse.getEntity().getContent();

            data = new byte[256];
            stringbuffer = new StringBuffer();
            while (-1 != (len = inputstream.read(data))) {
                stringbuffer.append(new String(data, 0, len));
            }
            inputstream.close();

            switch(action)
            {
                case "retrieve": {
                    String s = stringbuffer.toString();
                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray record = jsonObj.getJSONArray("results");
                    toReturn = new String[record.length()];
                    for(int i = 0; i< record.length(); i++)
                    {
                        JSONObject c = record.getJSONObject(i);
                        toReturn[i] = c.getString("TBCaseNo");
                    }
                } break;

                case "insert": {
                    toReturn = new String[]{stringbuffer.toString()};
                }break;
            }
            return toReturn;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public class ExecuteTask extends AsyncTask {

        private Context context;

        public ExecuteTask(Context con) {
            context = con;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Bundle bundle=getIntent().getExtras();
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("TP_ID", bundle.getString("id")));
            //httppost = new HttpPost("http://192.168.43.110/retrieveAssignedPatient.php");
            httppost = new HttpPost("http://tbcarephp.azurewebsites.net/retrieveAssignedPatient.php");
            try {
                httpclient = new DefaultHttpClient();
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpresponse = httpclient.execute(httppost);

                if((ss = ReadResponse("retrieve")) != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            arrayAdapterPatient = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, ss);
                            arrayAdapterPatient.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            spinnerPatient.setAdapter(arrayAdapterPatient);
                        }
                    });
                }
            }
            catch (UnsupportedEncodingException e) { e.printStackTrace(); }
            catch (IOException e) { e.printStackTrace(); }
            return null;
        }
    }

    public class ExecuteTask2 extends AsyncTask {

        private Context context;

        public ExecuteTask2(Context con) {
            context = con;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            String SE_Result = spinnerVisualAppearance.getSelectedItem().toString() + " " + spinnerReading.getSelectedItem().toString() +" "+ spinnerDiagnosis.getSelectedItem().toString();
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("TBCaseNo", spinnerPatient.getSelectedItem().toString()));
            nameValuePairs.add(new BasicNameValuePair("SE_Result", SE_Result));
            nameValuePairs.add(new BasicNameValuePair("SE_Date", DateFormat.getDateTimeInstance().format(new Date()).toString()));



            httppost = new HttpPost("http://tbcarephp.azurewebsites.net/insertSputumExam.php");
            try{
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpresponse = httpclient.execute(httppost);
                if((ss = ReadResponse("insert")) != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, ss[0].toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
