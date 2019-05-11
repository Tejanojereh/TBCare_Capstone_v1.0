package com.example.android.tbcare_capstone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class Notes extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static Set<String> set;

    //DYNMAMIC NOTES

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        ListView listView = (ListView) findViewById(R.id.listnotes);



        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.android.tbcare_capstone",Context.MODE_PRIVATE);

        set=sharedPreferences.getStringSet("notes",null);

        notes.clear();


        if(set != null){

            notes.addAll(set);

        }else{


            notes.add("Example Note");
            set = new HashSet<String>();
            set.addAll(notes);
            sharedPreferences.edit().remove("notes").apply();
            sharedPreferences.edit().putStringSet("notes",set).apply();

        }


         arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes );
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), EditNote.class);
                intent.putExtra("noteId", position);
                startActivity(intent);

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(Notes.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                notes.remove(position);

                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.android.tbcare_capstone", Context.MODE_PRIVATE);


                                if (set == null){

                                    set=new HashSet<String>();


                                }else{

                                    set.clear();

                                }


                                set.addAll(notes);

                                sharedPreferences.edit().remove("notes").apply();

                                sharedPreferences.edit().putStringSet("notes",set).apply();
                                arrayAdapter.notifyDataSetChanged();





                            }
                        })

                        .setNegativeButton("No",null)
                        .show();



            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                notes.add("");

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.android.tbcare_capstone", Context.MODE_PRIVATE);

                if (set == null){

                    set=new HashSet<String>();


                }else{

                    set.clear();

                }


                set.addAll(notes);

                sharedPreferences.edit().remove("notes").apply();

                sharedPreferences.edit().putStringSet("notes",set).apply();
                arrayAdapter.notifyDataSetChanged();



                Intent intent = new Intent(getApplicationContext(),EditNote.class);

                intent.putExtra("noteId",notes.size()-1);

                startActivity(intent);
            }
        });



    }

}
