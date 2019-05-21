package com.example.android.tbcare_capstone;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.tbcare_capstone.Class.Utility.AlertReceiver;
import com.example.android.tbcare_capstone.Class.Utility.TimePickerfragments;
import com.example.android.tbcare_capstone.Class.WebServiceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public  class Patient_Setintake extends AppCompatActivity implements TimePickerfragments.TimePickerListener, WebServiceClass.Listener {

    private TextView tvDisplayTime;
    private int numberofIntake, numberofIntake2;
    private Button submitBtn;
    private String first_intake, second_intake, third_intake, fourth_intake, fifth_intake, first_intake2, second_intake2, third_intake2, fourth_intake2, fifth_intake2;
    private String[] intake, intake2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_setintake);

        first_intake = "";
        second_intake = "";
        third_intake = "";
        fourth_intake = "";
        fifth_intake = "";
        first_intake2 = "";
        second_intake2 ="";
        third_intake2 = "";
        fourth_intake2= "";
        fifth_intake2 = "";
        tvDisplayTime = findViewById(R.id.tvDisplayTime);
        submitBtn = findViewById(R.id.btnSubmit);
        Bundle bundle = getIntent().getExtras();
        numberofIntake = bundle.getInt("number_of_intakes");
        numberofIntake2 = bundle.getInt("number_of_intakes2");
        Button btnShowTimePicker = findViewById(R.id.btnShowTimePicker);
        btnShowTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePickerFragment = new TimePickerfragments();
                timePickerFragment.setCancelable(false);
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvDisplayTime.getText().toString().equals(""))
                {
                    Toast.makeText(Patient_Setintake.this, "Please select your medication schedule.", Toast.LENGTH_LONG).show();
                }else{
                    SharedPreferences s = getSharedPreferences("session", 0);
                    String patient_id  = Integer.toString(s.getInt("account_id", 0));
                    String address = "http://tbcarephp.azurewebsites.net/set_medication_schedule.php";
                    String[] value = {first_intake, second_intake, third_intake, fourth_intake, fifth_intake, first_intake2, second_intake2, third_intake2, fourth_intake2, fifth_intake2, patient_id, Integer.toString(numberofIntake), Integer.toString(numberofIntake2)};
                    String[] valueName = {"first", "second", "third", "fourth", "fifth", "first2", "second2", "third2", "fourth2", "fifth2", "patient_id", "numberofIntakes", "numberofIntakes2"};

                    WebServiceClass service = new WebServiceClass(address, value, valueName, Patient_Setintake.this, Patient_Setintake.this);
                    service.execute();
                }

            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        int hour2 = hour, minute2 = minute;
        Time time = new Time(hour, minute, 0);

        timePicker.getCurrentMinute();
        timePicker.getCurrentHour();
        /*tvDisplayTime.setText(  timePicker.getCurrentHour() + " :" + timePicker.getCurrentMinute() );*/
        tvDisplayTime.setText(time.toString());

        intake = new String[numberofIntake];

        switch(numberofIntake)
        {
            case 2:
                first_intake = time.toString();
                intake[0] = time.toString();
                hour = hour + 12;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                intake[1] = time.toString();
                second_intake = time.toString();
                break;
            case 3:
                first_intake = time.toString();
                intake[0] = time.toString();
                hour = hour + 8;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                intake[1] = time.toString();
                second_intake = time.toString();
                hour = hour + 8;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                intake[2] = time.toString();
                third_intake = time.toString();
                break;
            case 4:
                first_intake = time.toString();
                intake[0] = time.toString();
                hour = hour + 6;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                intake[1] = time.toString();
                second_intake = time.toString();
                hour = hour + 6;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                intake[2] = time.toString();
                third_intake = time.toString();
                hour = hour + 6;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                intake[3] = time.toString();
                fourth_intake = time.toString();
                break;
            case 5:
                first_intake = time.toString();
                intake[0] = time.toString();
                hour = hour + 5;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                intake[1] = time.toString();
                second_intake = time.toString();
                hour = hour + 5;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                intake[2] = time.toString();
                third_intake = time.toString();
                hour = hour + 5;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                intake[3] = time.toString();
                fourth_intake = time.toString();
                hour = hour + 5;
                if(hour >= 24)
                {
                    hour = hour - 24;
                }
                time = new Time(hour, minute, 0);
                intake[4] = time.toString();
                fifth_intake = time.toString();
                break;
        }

        time = new Time(hour2, minute2, 0);


        timePicker.getCurrentMinute();
        timePicker.getCurrentHour();
        /*tvDisplayTime.setText(  timePicker.getCurrentHour() + " :" + timePicker.getCurrentMinute() );*/
        tvDisplayTime.setText(time.toString());

        switch(numberofIntake2)
        {
            case 2:
                first_intake2 = time.toString();
                hour2 = hour2 + 12;
                if(hour2 >= 24)
                {
                    hour2 = hour2 - 24;
                }
                time = new Time(hour2, minute2, 0);
                second_intake2 = time.toString();
                break;
            case 3:
                first_intake2 = time.toString();
                hour2 = hour2 + 8;
                if(hour2 >= 24)
                {
                    hour2 = hour2 - 24;
                }
                time = new Time(hour2, minute2, 0);
                second_intake2 = time.toString();
                hour2 = hour2 + 8;
                if(hour2 >= 24)
                {
                    hour2 = hour2 - 24;
                }
                time = new Time(hour2, minute2, 0);
                third_intake2 = time.toString();
                break;
            case 4:
                first_intake2 = time.toString();
                hour2 = hour2 + 6;
                if(hour2 >= 24)
                {
                    hour2 = hour2 - 24;
                }
                time = new Time(hour2, minute2, 0);
                second_intake2 = time.toString();
                hour2 = hour2 + 6;
                if(hour2 >= 24)
                {
                    hour2 = hour2 - 24;
                }
                time = new Time(hour2, minute2, 0);
                third_intake2 = time.toString();
                hour2 = hour2 + 6;
                if(hour2 >= 24)
                {
                    hour2 = hour2 - 24;
                }
                time = new Time(hour2, minute2, 0);
                fourth_intake2 = time.toString();
                break;
            case 5:
                first_intake2 = time.toString();
                hour2 = hour2 + 5;
                if(hour >= 24)
                {
                    hour2 = hour2 - 24;
                }
                time = new Time(hour2, minute2, 0);
                second_intake2 = time.toString();
                hour2 = hour2 + 5;
                if(hour2 >= 24)
                {
                    hour2 = hour2 - 24;
                }
                time = new Time(hour2, minute2, 0);
                third_intake2 = time.toString();
                hour2 = hour2 + 5;
                if(hour2 >= 24)
                {
                    hour2 = hour2 - 24;
                }
                time = new Time(hour2, minute2, 0);
                fourth_intake2 = time.toString();
                hour2 = hour2 + 5;
                if(hour2 >= 24)
                {
                    hour2 = hour2 - 24;
                }
                time = new Time(hour2, minute2, 0);
                fifth_intake2 = time.toString();
                break;
        }


        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 1);

        startAlarm(c);
    }

    @Override
    public void OnTaskCompleted(JSONArray Result, boolean flag) {

        if(flag)
        {
            if(Result != null)
            {
                try {
                    JSONObject object = Result.getJSONObject(0);
                    String message = object.getString("message");
                    if(message.equals("Success"))
                    {
                        try {

                            AlarmManager mgrAlarm = (AlarmManager) getSystemService(ALARM_SERVICE);
                            ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();

                            for(int i = 0; i < numberofIntake; ++i)
                            {
                                Calendar cal = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                                cal.setTime(sdf.parse(intake[i]));
                                Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
                                // Loop counter `i` is used as a `requestCode`
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), i, intent, 0);
                                // Single alarms in 1, 2, ..., 10 minutes (in `i` minutes)
                                mgrAlarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                        cal.getTimeInMillis(),
                                        AlarmManager.INTERVAL_DAY,
                                        pendingIntent);

                                intentArray.add(pendingIntent);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(this, "You're all set!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else{
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /*public class MyReceiver extends BroadcastReceiver {



        @Override
        public void onReceive(Context context, Intent intent) {

            showNotification(context,Main3Activity.class,"Test Time","do it now");


        }

        public static void showNotification(Context context,Class<?> cls,String title,String content)

        {

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);




            Intent notificationIntent = new Intent(context, cls);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);




            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

            stackBuilder.addParentStack(cls);

            stackBuilder.addNextIntent(notificationIntent);




            PendingIntent pendingIntent = stackBuilder.getPendingIntent(

                    DAILY_REMINDER_REQUEST_CODE,PendingIntent.FLAG_UPDATE_CURRENT);




            NotificationCompat.Builder builder = new     NotificationCompat.Builder(context);

            Notification notification = builder.setContentTitle(title)

                    .setContentText(content).setAutoCancel(true)

                    .setSound(alarmSound).setSmallIcon(R.mipmap.ic_launcher_round)

                    .setContentIntent(pendingIntent).build();



            NotificationManager notificationManager = (NotificationManager)

                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification);

        }*/

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        /*if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }*/

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}


