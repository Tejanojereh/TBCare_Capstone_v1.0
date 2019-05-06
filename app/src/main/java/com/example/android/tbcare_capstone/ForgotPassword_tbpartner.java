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

public class ForgotPassword_tbpartner extends AppCompatActivity
{

    Bundle bundle;
    TextView txt_question;
    EditText txt_answer;
    Button next;
    String uname;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        txt_question= findViewById(R.id.question);
        txt_answer= findViewById(R.id.answer);
        next=findViewById(R.id.cmd_next);





    }

    public class WebService_ForgotPassword extends AsyncTask
    {
        @Override
        protected Void doInBackground(Object... objects)
        {
            bundle= getIntent().getExtras();
            uname = bundle.getString("uname");

            //for webservice
            byte[] data;
            HttpPost httpPost;
            StringBuffer buffer;
            HttpResponse response;
            HttpClient httpclient;
            InputStream inputStream;
            final String message;


            List<NameValuePair> nameValuePairs;
            nameValuePairs= new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("P_username",uname));

            try
            {
                httpclient = new DefaultHttpClient();
                httpPost = new HttpPost("http://tbcarephp.azurewebsites.net/retrieve_securityinfo.php");
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



                        //for verification if the user input the right answer to security question
                        try {
                            txt_question.setText("Question: "+ c.getString("question").toString());

                            next.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                            if (txt_answer.getText().equals(c1.getString("answer")))
                                            {


                                            }
                                            else
                                            {
                                                Toast.makeText(ForgotPassword_tbpartner.this, "Incorrect answer", Toast.LENGTH_SHORT).show();
                                            }

                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            });



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                });
            }catch (final Exception e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ForgotPassword_tbpartner.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;

        }
    }



}