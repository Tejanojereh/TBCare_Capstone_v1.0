//package com.example.android.tbcare_capstone;
//
//import android.app.DatePickerDialog;
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.TextView;
//
//import java.text.DateFormat;
//import java.util.Calendar;
//
//public class AcceptPendingConfirmation extends AppCompatActivity implements DatePickerDialog.OnDateSetListener  {
//
//    //Class for accept pending confirmation
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.partner_acceptpendingconfirmation);
//
//
//        Button btn = (Button) findViewById(R.id.dp_med1);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogFragment datePicker = new DatepickerFragment();
//                datePicker.show(getSupportFragmentManager(), "date picker");
//            }
//        });
//
//
//    }
//
//
//
//    @Override
//    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//        Calendar c = Calendar.getInstance();
//        c.set(Calendar.YEAR, year);
//        c.set(Calendar.MONTH, month);
//        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
//
//        TextView textView = (TextView) findViewById(R.id.lblmed1);
//        textView.setText(currentDateString);
//    }
//
//
//    public void showDatePicker(View v) {
//        DialogFragment newFragment = new MyDatePickerSampleFragment();
//        newFragment.show(getSupportFragmentManager(), "date picker");
//    }
//
//}
