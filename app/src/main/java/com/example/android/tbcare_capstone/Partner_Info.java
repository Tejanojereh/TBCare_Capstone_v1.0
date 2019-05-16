package com.example.android.tbcare_capstone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tbcare_capstone.PatientActivities.Menu_Patient;

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

public class Partner_Info extends AppCompatActivity {


    TextView tp_name,contact;
    String id;
    String name;
    ImageButton back;
    Bundle bundle,bundle1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_info);
        tp_name= findViewById(R.id.tp_name);
        contact= findViewById(R.id.tp_contact);
        back=findViewById(R.id.btn_back);

    new WebService_TBPartner().execute();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Partner_Info.this, Menu_Patient.class );
                bundle1= getIntent().getExtras();

                id= bundle1.getString("id");

                bundle.putString("id",id);
                intent.putExtras(bundle);
                startActivity(intent);

            }

        });





    }

    public  class WebService_TBPartner extends AsyncTask {
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

            id=bundle.getString("id");

            List<NameValuePair> nameValuePairs;
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("P_ID", id));

            try {
                httpclient = new DefaultHttpClient();
                httpPost = new HttpPost("http://tbcarephp.azurewebsites.net/retrieve_tp.php");

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httpPost);
                inputStream = response.getEntity().getContent();

                data = new byte[256];
                buffer = new StringBuffer();
                int len = 0;

                while (-1 != (len = inputStream.read(data))) {
                    buffer.append(new String(data, 0, len));


                }
                String s = buffer.toString();
                JSONObject jsonObj = new JSONObject(s);
                final JSONArray record = jsonObj.getJSONArray("results");

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {


                        try {
                            JSONObject c = record.getJSONObject(0);
                            name=c.getString("TP_Fname")+" ";
                             c = record.getJSONObject(1);
                            name+=c.getString("TP_Mname")+" ";
                             c = record.getJSONObject(2);
                            name+=c.getString("TP_Lname")+" ";
                             c = record.getJSONObject(3);
                             tp_name.setText(name);
                             contact.setText(c.getString("TP_ContactNo"));



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });

            } catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Partner_Info.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }
    }


}
