package com.example.android.tbcare_capstone;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WebService extends AsyncTask {

    private String Address;
    private String[] Value;
    private String[] ValueName;

    public WebService(String address, String[] value, String[] valueName)
    {
        Address = address;
        Value = value;
        ValueName = valueName;
    }

    public String[] WebServiceManager() //Will Return the Data
    {
        String[] toReturn = new String[100];

        try
        {
            doInBackground(null);
            return toReturn;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        byte data[];
        HttpPost httpPost;
        StringBuffer buffer = null;
        HttpResponse response;
        HttpClient httpClient;
        InputStream inputStream;
        final String message;

        List<NameValuePair> nameValuePairs;
        nameValuePairs = new ArrayList<NameValuePair>(ValueName.length);
        for(int i=0; i<ValueName.length; i++)
        {
            nameValuePairs.add(new BasicNameValuePair(ValueName[i].toString(), Value[i].toString()));
        }
        //nameValuePairs.add(new BasicNameValuePair("username", txtUsername.getText().toString()));
        //nameValuePairs.add(new BasicNameValuePair("password", txtPassword.getText().toString()));

        try {


            httpClient = new DefaultHttpClient();

            httpPost = new HttpPost(Address);

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            response = httpClient.execute(httpPost);
            inputStream = response.getEntity().getContent();
            data = new byte[256];
            buffer = new StringBuffer();
            int len = 0;

            while(-1 != (len=inputStream.read(data))) {
                buffer.append(new String (data, 0, len));
            }

            message = buffer.toString();
            JSONObject jsonObj = new JSONObject(message);
            org.json.JSONArray record = jsonObj.getJSONArray("results");
            //   inputStream.close();

            /*if(record.length() == 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //    progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                JSONObject object = record.getJSONObject(0);
                id = object.getString("id");
                object = record.getJSONObject(1);
                uname = object.getString("username");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent;
                        if(uname.contains("TP") )
                            intent = new Intent(MainActivity.this, Menu_TBPartner.class);

                        else {
                            intent = new Intent(MainActivity.this, Menu_Patient.class);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putString("id", uname);
                        intent.putExtras(bundle);
                        txtPassword.setText(" ");
                        txtUsername.setText(" ");
                        startActivity(intent);

                    }
                });
            }*/

        }catch (final Exception e) {
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Incorrect username or password!", Toast.LENGTH_LONG).show();
                }
            });*/
        }

        return null;
    }
}
