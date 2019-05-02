package com.example.android.tbcare_capstone;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.voice.VoiceInteractionService;
import android.support.v7.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.Date;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
public class Notify_Patient extends AppCompatActivity{


    String P_ID="1";
    Boolean linkCheck;
    String medication_date;


    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = alertDialogBuilder.create();

       // new WebService_Medication().execute();

    }

  public void outputNotif()
  {


  }


    class WebService_Medication extends AsyncTask
    {
        @Override
        protected Void doInBackground(Object... objects)
        {
            byte[] data;
            HttpPost httpPost;
            StringBuffer buffer;
            HttpResponse response;
            HttpClient httpclient;
            InputStream inputStream;
            final String message;

            List<NameValuePair> nameValuePairs;
            nameValuePairs= new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("P_ID","1"));

            try
            {
                httpclient = new DefaultHttpClient();
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
                final JSONObject c = record.getJSONObject(0);
                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {


                        try {

                            medication_date= c.getString("M_InitialTime");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }catch (final Exception e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Notify_Patient.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }



    }




}
