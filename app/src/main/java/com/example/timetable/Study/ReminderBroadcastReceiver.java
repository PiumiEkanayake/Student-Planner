package com.example.timetable.Study;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.timetable.MainActivity;
import com.example.timetable.R;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        // Set activity to open when notification is clicked
        Intent mainIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(mainIntent);
        PendingIntent mainPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, "notifyStudyPlanner")
                .setSmallIcon(R.drawable.alarm)
                .setContentTitle("Time for " + intent.getStringExtra(ReminderScheduler.EXTRA_STUDY_TITLE))
                .setContentText(intent.getStringExtra(ReminderScheduler.EXTRA_SUBJECT_NAME))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(mainPendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(1, notificationBuilder.build());
    }
}
