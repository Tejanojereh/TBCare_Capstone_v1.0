package com.example.android.tbcare_capstone.Class.Utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.tbcare_capstone.R;

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

public class ListPatientsFragments extends Fragment{

    //LIST OF PATIENTS FRAGMENTS(TAB CONTAINER LAYOUT OPTION B)
    private String[] mCase;
    private String[] disease_classification;
    private String[] status;
    private String id;
    private int array_length;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_viewpatient, container, false);

        id = Integer.toString(getArguments().getInt("account_id"));
        GetPatientList();
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext(), mCase, disease_classification, array_length);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public void GetPatientList(){

        String address = "http://tbcarephp.azurewebsites.net/retrieve_patientList.php";
        String[] value = {id};
        String[] valueName = {"id"};

        GetPatientService service = new GetPatientService(address, value, valueName);

        service.execute();
    }


    class GetPatientService extends AsyncTask {

        private String Address;
        private String[] Value;
        private String[] ValueName;
        private org.json.JSONArray RecordResult;
        private ProgressDialog progressDialog;
        private boolean flag = true;


        public GetPatientService(String address, String[] value, String[] valueName)
        {
            Address = address;
            Value = value;
            ValueName = valueName;
            RecordResult = new org.json.JSONArray();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
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
                    if (json instanceof JSONArray) {
                        RecordResult = (JSONArray) json;
                    }
                    JSONObject object = RecordResult.getJSONObject(0);
                    String patients_no = object.getString("patients_no");
                    Toast.makeText(getActivity(), "You have no patients at the moment", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    mCase = new String[RecordResult.length()];
                    disease_classification = new String[RecordResult.length()];
                    status = new String[RecordResult.length()];
                    for(int i = 0; i<RecordResult.length(); i++){
                        try
                        {
                            JSONObject object = RecordResult.getJSONObject(i);

                            mCase[i] = object.getString("TB_CASE_NO");
                            disease_classification[i] = object.getString("disease_classification");
                            status[i] = object.getString("status");
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                    array_length = RecordResult.length();
                }

                progressDialog.dismiss();
            }else{
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error occured", Toast.LENGTH_LONG).show();
            }

        }
    }





    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avator;
        public TextView name;
        public TextView casenumber;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.row_listview, parent, false));
            casenumber = (TextView) itemView.findViewById(R.id.patient_case_numTxtView);
            name = (TextView) itemView.findViewById(R.id.tp_name);

            casenumber.setText("HI");
            name.setText("hello");


        }
    }
    /**
     * Adapter to display recycler view.
     */


    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private int LENGTH = 2;

        private final String[] mCase;
        private final String[] disease_classification;


        public ContentAdapter(Context context, String[] m_case, String[] disease_c, int length) {
            Resources resources = context.getResources();
            LENGTH = length;
            mCase = m_case;
            disease_classification = disease_c;


        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.casenumber.setText(mCase[position % mCase.length]);
            holder.name.setText(disease_classification[position % disease_classification.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }



}
