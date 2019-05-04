package com.example.android.tbcare_capstone;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.android.tbcare_capstone.WebServiceClass.Listener;

public class View_and_Update_Medication_Progress extends AppCompatActivity implements View.OnClickListener, Listener{

    TextView txtOverallProgress, txtMedicationProgress, txtSputumResultAnalysis;

    HttpClient httpclient; HttpResponse httpresponse; HttpPost httppost;
    StringBuffer stringbuffer = null; InputStream inputstream; BufferedReader bufferedreader;
//    List<NameValuePair> nameValuePairs;

    String[] tempStorage;
//    String Dtype, Idate, Ddate, ID, EDate, Eresult="";
    byte[] data;
    Bundle bundle;
    ImageButton back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_and_update_medication_progress);
        InstantiateControls();
        bundle = getIntent().getExtras();  back = (ImageButton) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(View_and_Update_Medication_Progress.this, Menu_Patient.class );
                startActivity(intent);
            }

        });

//        nameValuePairs = new ArrayList<NameValuePair>();
//        nameValuePairs.add(new BasicNameValuePair("TB_Case_No", bundle.getString("id")));

        String address = "http://tbcarephp.azurewebsites.net/getPatient_OverallProgress.php";
        String[] value = new String[]{bundle.getString("id")};
        String[] valueName = new String[]{"TB_Case_No"};
        WebServiceClass wbc = new WebServiceClass(address, value, valueName, View_and_Update_Medication_Progress.this, this);
        wbc.execute(); //First

        address = "";
        value = new String[]{bundle.getString("id")};
        valueName = new String[]{"TB_Case_No"};
        wbc = new WebServiceClass(address, value, valueName, View_and_Update_Medication_Progress.this, this);
        wbc.execute(); //Second

        address = "";
        value = new String[]{bundle.getString("id")};
        valueName = new String[]{"TB_Case_No"};
        wbc = new WebServiceClass(address, value, valueName, View_and_Update_Medication_Progress.this, this);
        wbc.execute(); //third


//        new ExecuteTask(this).execute();
//        new ExecuteTask2(this).execute();
//        new ExecuteTask3(this).execute();

    }

    @Override
    public void onClick(View v) { }

    public void InstantiateControls() {
        txtOverallProgress = (TextView)findViewById(R.id.TxtOverallProgress);
        txtMedicationProgress = (TextView)findViewById(R.id.TxtMedicationProgress);
        txtSputumResultAnalysis = (TextView)findViewById(R.id.TxtSputumResultAnalysis);
    }

    @Override
    public void OnTaskCompleted(JSONArray Result) {

        try {
            tempStorage = new String[Result.length()];

            for(int i = 0; i< Result.length(); i++)
            {
                JSONObject c = Result.getJSONObject(i);
                tempStorage[i] = c.getString("date");
            }
        }
        catch(Exception e) {

        }
    }

/*    public class ExecuteTask extends AsyncTask{

        Context context;
        public ExecuteTask(Context con) {context=con;}

        @Override
        protected Object doInBackground(Object[] objects) {
            try{
                int len =0;

                //region Overall Progress
                httppost = new HttpPost("http://tbcarephp.azurewebsites.net/getPatient_OverallProgress.php");

                httpclient = new DefaultHttpClient();
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpresponse = httpclient.execute(httppost);

                inputstream = httpresponse.getEntity().getContent();
                data = new byte[256];
                stringbuffer = new StringBuffer();
                while (-1 != (len = inputstream.read(data))) {
                    stringbuffer.append(new String(data, 0, len));
                }
                inputstream.close();
                String overall = stringbuffer.toString();
                JSONObject jsonObject_Overall = new JSONObject(overall);
                JSONArray record_Overall = jsonObject_Overall.getJSONArray("results");
                tempStorage = new String[record_Overall.length()];
                for(int i = 0; i< record_Overall.length(); i++)
                {
                    JSONObject c = record_Overall.getJSONObject(i);
                    tempStorage[i] = c.getString("date");
                }
                //endregion

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtOverallProgress.setText( (30*6) - Integer.parseInt(tempStorage[0].toString()) + " days out of " + (30*6) + " days of treatment.");
                    }
                });
            }
            catch(final Exception e) {
                    runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
    }*/

/*    public class ExecuteTask2 extends AsyncTask{

        Context context;
        public ExecuteTask2(Context con) {context=con;}

        @Override
        protected Object doInBackground(Object[] objects) {
            try{
                //region Medication Progress
                //httppost = new HttpPost("http://192.168.43.110/retrieve_medicationProgress.php");
                httppost = new HttpPost("http://tbcarephp.azurewebsites.net/retrieve_medicationProgress.php");
                httpclient = new DefaultHttpClient();
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpresponse = httpclient.execute(httppost);

                int len =0;
                inputstream = httpresponse.getEntity().getContent();
                data = new byte[256];
                stringbuffer = new StringBuffer();
                while (-1 != (len = inputstream.read(data))) {
                    stringbuffer.append(new String(data, 0, len));
                }
                inputstream.close();
                String medication = stringbuffer.toString();
                JSONObject jsonObject_Medication = new JSONObject(medication);
                JSONArray record_Medication = jsonObject_Medication.getJSONArray("results");

                JSONObject m = record_Medication.getJSONObject(0);
                Dtype = m.getString("drug_type");
                m = record_Medication.getJSONObject(1);
                Idate = m.getString("initial_time");
                m = record_Medication.getJSONObject(2);
                Ddate = m.getString("due_time");
                //endregion
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtMedicationProgress.setText("Your medicine intake is "+ Dtype +" \nFirst Intake: "+ Idate +" \nSecond Intake: "+Ddate );
                    }
                });
            }
            catch(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
    }*/

    //Displaying Sputum Examination Result
/*    public class ExecuteTask3 extends AsyncTask{

        Context context;
        public ExecuteTask3(Context con) {context=con;}

        @Override
        protected Object doInBackground(Object[] objects) {
            try{
                //region Sputum Exam Result
                httppost = new HttpPost("http://tbcarephp.azurewebsites.net/retrieve_SputumExamResult.php");

                httpclient = new DefaultHttpClient();
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpresponse = httpclient.execute(httppost);

                int len =0;
                inputstream = httpresponse.getEntity().getContent();
                data = new byte[256];
                stringbuffer = new StringBuffer();
                while (-1 != (len = inputstream.read(data))) {
                    stringbuffer.append(new String(data, 0, len));
                }
                inputstream.close();
                String sputumExam = stringbuffer.toString();
                if(!sputumExam.equals("")) {
                    JSONObject jsonObject_SputumExam = new JSONObject(sputumExam);
                    JSONArray record_SputumExam = jsonObject_SputumExam.getJSONArray("results");

                    JSONObject se = record_SputumExam.getJSONObject(0);
                    ID = se.getString("ID");
                    se = record_SputumExam.getJSONObject(1);
                    EDate = se.getString("Exam_date");
                    se = record_SputumExam.getJSONObject(2);
                    Eresult = se.getString("Result");
                }
                //endregion

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(!Eresult.equals("")) {
                            String[] re = Eresult.split(" ");
                            txtSputumResultAnalysis.setText("Date: " + EDate + " \nAppereance: " + re[0].toString() + " \nReading: " + re[1].toString() + " \nDiagnosis: " + re[2].toString());
                        }
                        else{
                            txtSputumResultAnalysis.setText("No Sputum Examination");
                        }
                    }
                });
            }
            catch(final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
    }*/
}
