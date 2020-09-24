package com.example.timetable.Study;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ReminderScheduler {

    public static final String EXTRA_STUDY_TITLE = "com.example.timetable.STUDY_TITLE";
    public static final String EXTRA_SUBJECT_NAME = "com.example.timetable.SUBJECT_NAME";
    private static final long INTERVAL_WEEK = 604800000L;
    private Context context;
    private Intent intent;
    private Calendar calendar;
    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
    private Long dateTimeInMillis;
    private AlarmManager alarmManager;

    public ReminderScheduler(Context context, String studyTitle, String subjectName,
                             String studyDate, String startTime, String reminderTime) {
        this.context = context;
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        intent = new Intent(this.context, ReminderBroadcastReceiver.class);
        intent.putExtra(EXTRA_STUDY_TITLE, studyTitle);
        intent.putExtra(EXTRA_SUBJECT_NAME, subjectName);

        try {
            Date dateTime = dateTimeFormatter.parse(studyDate + " " + startTime);
            assert dateTime != null;
            calendar.setTime(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        dateTimeInMillis = calendar.getTimeInMillis();

        long reminderTimeInMillis;
        String reminderTimeUnit = reminderTime.split(" ")[1].startsWith("m") ? "m" : "h";

        if (reminderTimeUnit.equals("m"))
            reminderTimeInMillis = Long.parseLong(reminderTime.split(" ")[0]) * 60 * 1000;
        else
            reminderTimeInMillis = Long.parseLong(reminderTime.split(" ")[0]) * 60 * 60 * 1000;

        dateTimeInMillis -= reminderTimeInMillis;
    }

    public void scheduleReminder(String repeat) {
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        if (repeat.equalsIgnoreCase("daily"))
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, dateTimeInMillis,
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        else if (repeat.equalsIgnoreCase("weekly"))
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, dateTimeInMillis,
                    INTERVAL_WEEK, pendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, dateTimeInMillis, pendingIntent);
    }


}
