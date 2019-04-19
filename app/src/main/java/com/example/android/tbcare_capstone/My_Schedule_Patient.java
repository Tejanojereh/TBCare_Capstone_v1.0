package com.example.android.tbcare_capstone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.voice.VoiceInteractionService;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.toolbox.JsonObjectRequest;

public class My_Schedule_Patient extends AppCompatActivity {


    TextView med_date;
    TextView datenow;
    ImageButton addnote,back;
    Bundle bundle,bundle1;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule_patient);



        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd ");
        DateFormat datefor= new SimpleDateFormat("HH:mm");
        Date date = new Date();
        med_date = findViewById(R.id.medicine_date);
        datenow = findViewById(R.id.date);
        addnote = findViewById(R.id.addnote);
        datenow.setText(dateFormat.format(date)+ "\n"+ datefor.format(date));
        bundle=getIntent().getExtras();
        id= bundle.getString("id");

        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(My_Schedule_Patient.this, Note_Patient.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);

            }

        });

      back=findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_Schedule_Patient.this,Menu_Patient.class);

                bundle1= getIntent().getExtras();

                id= bundle1.getString("id");

                bundle.putString("id",id);
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });


     new WebService_Medication().execute();

    }



  public  class WebService_Medication extends AsyncTask
    {
        @Override
        protected Void doInBackground(Object... objects)
        {
            //bundle
            bundle=getIntent().getExtras();
             id= bundle.getString("id");


            byte[] data;
            HttpPost httpPost;
            StringBuffer buffer;
            HttpResponse response;
            HttpClient httpclient;
            InputStream inputStream;
            final String message;

            List<NameValuePair> nameValuePairs;
            nameValuePairs= new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("P_ID",id));

            try
            {
                httpclient = new DefaultHttpClient();
                //httpPost = new HttpPost("http://tbcarephp.azurewebsites.net/retrieve_medication.php");
                httpPost = new HttpPost("http://tbcarephp.azurewebsites.net/retrieve_medication.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response=httpclient.execute(httpPost);
                inputStream=response.getEntity().getContent();

                data= new byte[256];
                buffer=new StringBuffer();
                int len=0;

                while(-1!=(len=inputStream.read(data))) {
                    buffer.append(new String(data, 0, len));


                }
                String s= buffer.toString();
                JSONObject jsonObj= new JSONObject(s);
                JSONArray record = jsonObj.getJSONArray("results");
             final   JSONObject c = record.getJSONObject(0);
               final JSONObject c1 =record.getJSONObject(1);
                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {


                try {
                            med_date.setText("Medicine Intake Schedule:\n"+ c.getString("Initial_time").toString()+"\n"+c1.getString("due_time"));
                        } catch (JSONException e) {
                    e.printStackTrace();
                }


                    }

                });
            }catch (final Exception e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(My_Schedule_Patient.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
            }



        }



}
