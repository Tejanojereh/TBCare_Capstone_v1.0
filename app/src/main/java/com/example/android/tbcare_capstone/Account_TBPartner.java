package com.example.android.tbcare_capstone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.android.tbcare_capstone.WebServiceClass.Listener;

public class Account_TBPartner extends AppCompatActivity implements Listener {
    private TextView fname,lname,mname,contact,uname;
    private ImageButton btn,back;
    private Bundle bundle;
    private WebServiceClass webService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_tbpartner);
        fname=findViewById(R.id.firstname);
        lname=findViewById(R.id.lastname);
        mname=findViewById(R.id.middlename);
        uname=findViewById(R.id.username);
        contact=findViewById(R.id.contactnumber);
        //btn= findViewById(R.id.btnsave);
        back = (ImageButton) findViewById(R.id.btn_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Account_TBPartner.this, Menu_TBPartner.class );

                startActivity(intent);

            }

        });

        String address = "http://tbcarephp.azurewebsites.net/retrieve_tbinfo.php";
        String[] value = {bundle.getString("id")};
        String[] valueName = {"P_ID"};
        webService = new WebServiceClass(address, value, valueName, Account_TBPartner.this);
        webService.execute();

        //new WebService_TBPartner().execute();

    }

    @Override
    public void OnTaskCompleted(JSONArray Result) {

        try {
            JSONObject c = Result.getJSONObject(0);

            fname.setText(c.getString("TP_Fname").toString());
            c = Result.getJSONObject(1);
            mname.setText("TP_Mname");
            c = Result.getJSONObject(2);
            lname.setText(c.getString("TP_Lname").toString());
            c = Result.getJSONObject(3);
            contact.setText(c.getString("TP_ContactNo").toString());
        }
        catch(Exception e) { Toast.makeText(Account_TBPartner.this, e.getMessage().toString(), Toast.LENGTH_LONG).show(); }
    }
    /*public  class WebService_TBPartner extends AsyncTask
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
            bundle = getIntent().getExtras();
            nameValuePairs.add(new BasicNameValuePair("P_ID",bundle.getString("id")));

            uname.setText(bundle.getString("id"));
            try
            {
                httpclient = new DefaultHttpClient();
                httpPost = new HttpPost("http://tbcarephp.azurewebsites.net/retrieve_tbinfo.php");

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
              final   JSONArray record = jsonObj.getJSONArray("results");

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {


                            try {

                                     JSONObject c = record.getJSONObject(0);
                                     fname.setText(c.getString("TP_Fname").toString());
                                     c = record.getJSONObject(1);
                                     mname.setText("TP_Mname");
                                     c = record.getJSONObject(2);
                                    lname.setText(c.getString("TP_Lname").toString());
                                     c = record.getJSONObject(3);
                                contact.setText(c.getString("TP_ContactNo").toString());


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    });

            }catch (final Exception e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Account_TBPartner.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }*/
}
