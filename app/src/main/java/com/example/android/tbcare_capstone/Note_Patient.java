package com.example.android.tbcare_capstone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Note_Patient extends AppCompatActivity {


    EditText note;
    ImageButton back;
    String s,id;
    Bundle bundle1,bundle;
    String notetext;
    JSONObject c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        note = (EditText) findViewById(R.id.txt_note);
        back = (ImageButton) findViewById(R.id.btn_back);

        //updates Patient_note table and returns to My Schedule view
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Note_Patient.this, My_Schedule_Patient.class );
                new WebSer_Back().execute();
                bundle1= getIntent().getExtras();



                bundle.putString("id",bundle1.getString("id"));
                intent.putExtras(bundle);
                startActivity(intent);

            }

        });






        new WebService().execute();

    }

    class WebSer_Back extends AsyncTask {
        @Override
        protected Void doInBackground(Object... objects) {
            byte[] data;
            HttpPost httpPost;
            StringBuffer buffer;
            HttpResponse response;
            HttpClient httpclient;
            InputStream inputStream;
            final String message;
            String id;

            Bundle bundle= getIntent().getExtras();
            id=bundle.getString("id");

            List<NameValuePair> nameValuePairs;
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("ID", id));
            nameValuePairs.add(new BasicNameValuePair("NOTES", note.getText().toString()));


            try {
                httpclient = new DefaultHttpClient();
                httpPost = new HttpPost("http://tbcarephp.azurewebsites.net/updatenotes.php");
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httpPost);
                inputStream = response.getEntity().getContent();

                data = new byte[256];
                buffer = new StringBuffer();
                int len = 0;
                while (-1 != (len = inputStream.read(data))) {
                    buffer.append(new String(data, 0, len));

                }
               s = buffer.toString();
                inputStream.close();
                runOnUiThread(new Runnable(){
                    public void run() {
                        Toast.makeText(Note_Patient.this, s, Toast.LENGTH_LONG).show();

                    }
                });



            } catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Note_Patient.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }


    }






    class WebService extends AsyncTask {
        @Override
        protected Void doInBackground(Object... objects) {
            byte[] data;
            HttpPost httpPost;
            StringBuffer buffer;
            HttpResponse response;
            HttpClient httpclient;
            InputStream inputStream;
            final String message;
            Bundle bundle= getIntent().getExtras();


            List<NameValuePair> nameValuePairs;
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("ID",   bundle.getString("id")));

            try {
                httpclient = new DefaultHttpClient();
                httpPost = new HttpPost("http://tbcarephp.azurewebsites.net/retrieve_notes.php");

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httpPost);
                inputStream = response.getEntity().getContent();

                data = new byte[256];
                buffer = new StringBuffer();
                int len = 0;

                while (-1 != (len = inputStream.read(data))) {
                    buffer.append(new String(data, 0, len));



                }
                if(buffer.length()!=0) {
                    String s = buffer.toString();
                    JSONObject jsonObj = new JSONObject(s);
                    JSONArray record = jsonObj.getJSONArray("results");
                    c = record.getJSONObject(0);


                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {


                            try {

                                    notetext = c.getString("Notes").toString();

                                note.setText(notetext);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    });
                }

            } catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Note_Patient.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }


    }
}