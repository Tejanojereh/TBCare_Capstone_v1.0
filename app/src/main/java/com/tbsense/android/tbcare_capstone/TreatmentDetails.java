package com.tbsense.android.tbcare_capstone;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

public class TreatmentDetails extends AppCompatActivity {

    TextView txtPatient, txtMedication, txtPartner;

    HttpClient httpclient; HttpResponse httpresponse; HttpPost httppost;
    StringBuffer stringbuffer = null; InputStream inputstream; BufferedReader bufferedreader;
    List<NameValuePair> nameValuePairs;

    String[] ss; byte[] data;
    Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treatment_details);
        InstantiateControl();
        bundle = getIntent().getExtras();
    }

    public void InstantiateControl() {
        txtPatient = (TextView)findViewById(R.id.TxtPatient);
        txtMedication = (TextView)findViewById(R.id.TxtMedication);
        txtPartner = (TextView)findViewById(R.id.TxtTreatmentPartner);
    }

    //retrieving medication
    public class ExecuteTask extends AsyncTask {

        Context context;
        public ExecuteTask(Context con) {context=con;}

        @Override
        protected Object doInBackground(Object[] objects) {
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("TB_Case_No", bundle.getString("id")));
            httppost = new HttpPost("http://tbcarephp.azurewebsites.net/retrieve_medication.php");
            try{
                httpclient = new DefaultHttpClient();
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpresponse = httpclient.execute(httppost);

                String[] toReturn = null; int len =0;
                inputstream = httpresponse.getEntity().getContent();
                data = new byte[256];
                stringbuffer = new StringBuffer();
                while (-1 != (len = inputstream.read(data))) {
                    stringbuffer.append(new String(data, 0, len));
                }
                inputstream.close();
                String s = stringbuffer.toString();
                JSONObject jsonObj = new JSONObject(s);
                JSONArray record = jsonObj.getJSONArray("Initial_Time");
                ss = new String[record.length()];
                for(int i = 0; i< record.length(); i++)
                {
                    JSONObject c = record.getJSONObject(i);
                    ss[i] = c.getString("results");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtMedication.setText(ss[0].toString());
                    }
                });
            }
            catch(Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }

    //retrieving patient details
    public class ExecuteTask2 extends AsyncTask {

        Context context;
        public ExecuteTask2(Context con) {context=con;}

        @Override
        protected Object doInBackground(Object[] objects) {
            nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("TB_Case_No", bundle.getString("id")));
            httppost = new HttpPost("http://tbcarephp.azurewebsites.net/getPartner.php");
            try{
                httpclient = new DefaultHttpClient();
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpresponse = httpclient.execute(httppost);

                String[] toReturn = null; int len =0;
                inputstream = httpresponse.getEntity().getContent();
                data = new byte[256];
                stringbuffer = new StringBuffer();
                while (-1 != (len = inputstream.read(data))) {
                    stringbuffer.append(new String(data, 0, len));
                }
                inputstream.close();
                String s = stringbuffer.toString();
                JSONObject jsonObj = new JSONObject(s);
                JSONArray record = jsonObj.getJSONArray("partner_Name");
                ss = new String[record.length()];
                for(int i = 0; i< record.length(); i++)
                {
                    JSONObject c = record.getJSONObject(i);
                    ss[i] = c.getString("results");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtPartner.setText( ss[0].toString() );
                    }
                });
            }
            catch(Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return null;
        }
    }
}
