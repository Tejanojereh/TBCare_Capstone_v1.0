package com.example.android.tbcare_capstone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.android.tbcare_capstone.R.drawable;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.example.android.tbcare_capstone.WebServiceClass.Listener;

public class MyPatients extends AppCompatActivity implements Listener{

    Button[] btnPatients; RelativeLayout relativeLayout; LinearLayout linearLayout;

    HttpClient httpclient; HttpResponse httpresponse; HttpPost httppost;
    StringBuffer stringbuffer = null; InputStream inputstream;
//    BufferedReader bufferedreader;
    List<NameValuePair> nameValuePairs;

    String[] patients; byte[] data;
    Bundle bundle;
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patients);

        bundle = getIntent().getExtras();
//        nameValuePairs = new ArrayList<NameValuePair>();
//        nameValuePairs.add(new BasicNameValuePair("TP_ID", bundle.getString("id")));

        String address = "http://tbcarephp.azurewebsites.net/retrieveAssignedPatient.php ";
        String[] value = {bundle.getString("id")};
        String[] valueName = {"TP_ID"};
        WebServiceClass wbc = new WebServiceClass(address, value, valueName, MyPatients.this, this);
        wbc.execute();
        //new ExecuteTask(this).execute();
    }

    @Override
    public void OnTaskCompleted(JSONArray Result) {

        try {
                patients = new String[Result.length()];
                for(int i = 0; i< Result.length(); i++)
                {
                    JSONObject c = Result.getJSONObject(i);
                    patients[i] = c.getString("TBCaseNo");
                }

                linearLayout = (LinearLayout)findViewById(R.id.linearLayoutID);
                btnPatients = new Button[patients.length];

                for(int i=0; i<btnPatients.length; i++){
                    btnPatients[i] = new Button(MyPatients.this);
                    btnPatients[i].setWidth(250);
                    btnPatients[i].setHeight(250);
                    btnPatients[i].setText(patients[i].toString());
                    btnPatients[i].setTag(i);
                    btnPatients[i].setOnClickListener(new ButtonClicked(btnPatients[i], MyPatients.this));
                    btnPatients[i].setBackground(ContextCompat.getDrawable(MyPatients.this, R.drawable.containter_1));
                    linearLayout.addView(btnPatients[i]);
                }
        }
        catch (Exception e) {

        }
    }

//    public class ExecuteTask extends AsyncTask {
//
//        private Context context;
//
//        public ExecuteTask(Context con) {
//            context = con;
//        }
//
//        @Override
//        protected Object doInBackground(Object[] objects) {
//            //httppost = new HttpPost("http://192.168.43.110/retrieveAssignedPatient.php");
//            httppost = new HttpPost("http://tbcarephp.azurewebsites.net/retrieveAssignedPatient.php");
//            try {
//                httpclient = new DefaultHttpClient();
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                httpresponse = httpclient.execute(httppost);
//
//                int len =0;
//                inputstream = httpresponse.getEntity().getContent();
//
//                data = new byte[256];
//                stringbuffer = new StringBuffer();
//                while (-1 != (len = inputstream.read(data))) {
//                    stringbuffer.append(new String(data, 0, len));
//                }
//                inputstream.close();
//
//                String s = stringbuffer.toString();
//                JSONObject jsonObj = new JSONObject(s);
//                JSONArray record = jsonObj.getJSONArray("results");
//                patients = new String[record.length()];
//                for(int i = 0; i< record.length(); i++)
//                {
//                    JSONObject c = record.getJSONObject(i);
//                    patients[i] = c.getString("TBCaseNo");
//                }
//
//                    runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        linearLayout = (LinearLayout)findViewById(R.id.linearLayoutID);
//                        btnPatients = new Button[patients.length];
//
//                        for(int i=0; i<btnPatients.length; i++){
//                            btnPatients[i] = new Button(context);
//                            btnPatients[i].setWidth(250);
//                            btnPatients[i].setHeight(250);
//                            btnPatients[i].setText(patients[i].toString());
//                            btnPatients[i].setTag(i);
//                            btnPatients[i].setOnClickListener(new ButtonClicked(btnPatients[i], context));
//                            btnPatients[i].setBackground(ContextCompat.getDrawable(context, R.drawable.containter_1));
//                            linearLayout.addView(btnPatients[i]);
//                        }
////                        <ImageButton
////                        android:id="@+id/btn_back"
////                        android:layout_width="wrap_content"
////                        android:layout_height="wrap_content"
////
////                        android:layout_alignParentBottom="true"
////                        android:layout_alignParentStart="true"
////                        android:background="@android:color/transparent"
////                        app:srcCompat="@drawable/btn_back" />
//
////                        ImageButton img = new ImageButton(context);
////                        img.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
////                        img.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
////                        img.setBackground(ContextCompat.getDrawable(context, drawable.btn_back));
////                        linearLayout.addView(img);
////
////                        img=findViewById(R.id.btn_back);
////                        img.setOnClickListener(new View.OnClickListener() {
////                            @Override
////                            public void onClick(View v) {
////                                Intent intent = new Intent(MyPatients.this,Menu_Patient.class);
////
////                                bundle= getIntent().getExtras();
////
////                                id= bundle.getString("id");
////
////                                bundle.putString("id",id);
////                                intent.putExtras(bundle);
////                                startActivity(intent);
////
////
////                            }
////                        });
//
//
//                    }
//                });
//            }
//            catch (UnsupportedEncodingException e) { e.printStackTrace(); }
//            catch (IOException e) { e.printStackTrace(); }
//            catch (JSONException e) { e.printStackTrace(); }
//            return null;
//        }
//    }

    public class ButtonClicked implements View.OnClickListener {

        Button button; Context context;
        public ButtonClicked(Button btn, Context con) { button = btn; context=con;}

        @Override
        public void onClick(View v) {
            Button btn = (Button)v;
            Intent intent = new Intent(MyPatients.this, View_and_Update_Medication_Progress.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", btn.getText().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}

