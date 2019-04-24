package com.example.android.tbcare_capstone;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

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

//public class WebService extends AppCompatActivity { //for the runOnUiThread
    class WebServiceClass extends AsyncTask {

        private String Address;
        private String[] Value;
        private String[] ValueName;
        private org.json.JSONArray RecordResult;

        public interface Listener{
            void OnTaskCompleted(JSONArray Result);
        }

        Listener listener;

        public WebServiceClass(String address, String[] value, String[] valueName, Listener activityContext)
        {
            Address = address;
            Value = value;
            ValueName = valueName;
            RecordResult = new org.json.JSONArray();
            this.listener = activityContext;
        }

        /*public org.json.JSONArray WebServiceManager() //Will Return the Data
        {
            if(RecordResult.length() != 0)
                return RecordResult;
            else
                return null;
        }*/


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

                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WebServiceManager();
                    }
                });*/

                return RecordResult;

            }catch (final Exception e) {
                return null;
            }
        }


        @Override
        protected void onPostExecute(Object o) {
            Object json = null;
            try {
                json = new JSONTokener(o.toString()).nextValue();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (json instanceof JSONArray) {
                RecordResult = (JSONArray) json;
            }
            listener.OnTaskCompleted(RecordResult);
        }
    }
//}
