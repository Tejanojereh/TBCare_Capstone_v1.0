package com.example.android.tbcare_capstone;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ListPatientsFragments extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_viewpatient, container, false);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avator;
        public TextView name;
        public TextView casenumber;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.row_listview, parent, false));
            casenumber = (TextView) itemView.findViewById(R.id.txtid);
            name = (TextView) itemView.findViewById(R.id.txtname);



        }
    }
    /**
     * Adapter to display recycler view.
     */


    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private static final int LENGTH = 18;

        private final String[] mCase;
        private final String[] mPname;


        public ContentAdapter(Context context) {
            Resources resources = context.getResources();
            mCase = resources.getStringArray(R.array.casenum);
            mPname = resources.getStringArray(R.array.patientname);


        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            holder.casenumber.setText(mCase[position % mCase.length]);
            holder.name.setText(mPname[position % mPname.length]);
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }



}
