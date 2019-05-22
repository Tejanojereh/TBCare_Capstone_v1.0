package com.tbsense.android.tbcare_capstone.Class;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

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
import org.json.JSONTokener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WebServiceClass extends AsyncTask {

        private String Address;
        private String[] Value;
        private String[] ValueName;
        private org.json.JSONArray RecordResult;
        private ProgressDialog progressDialog;
        private boolean flag = true;

        public interface Listener{
            void OnTaskCompleted(JSONArray Result, boolean flag);
        }

        Listener listener;
        Context activityContext;

        public WebServiceClass(String address, String[] value, String[] valueName, Listener activityContext, Context activity)
        {
            Address = address;
            Value = value;
            ValueName = valueName;
            RecordResult = new org.json.JSONArray();
            this.listener = activityContext;
            this.activityContext = activity;
        }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(this.activityContext);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
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
                JSONArray RecordResult = jsonObj.getJSONArray("results");

                return RecordResult;

            }catch (final Exception e) {
                flag = false;
                return null;
            }
        }


        @Override
        protected void onPostExecute(Object o) {
            if(flag)
            {
                Object json = null;
                try {
                    json = new JSONTokener(o.toString()).nextValue();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (json instanceof JSONArray) {
                    RecordResult = (JSONArray) json;
                }
                progressDialog.dismiss();
                listener.OnTaskCompleted(RecordResult, flag);
            }else{
                progressDialog.dismiss();
                listener.OnTaskCompleted(RecordResult, flag);
            }

        }
    }
