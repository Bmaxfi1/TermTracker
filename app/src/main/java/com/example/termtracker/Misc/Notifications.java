package com.example.termtracker.Misc;


import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.example.termtracker.MainActivity;
import com.example.termtracker.R;

public class Notifications {

    public static final String CHANNEL_ID = "MyNotificationChannel";




    public static void makeNotification(Context context, String title, String message) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_school_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);  //makes the notification go away when clicked.

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(0, builder.build());
    }

    //    //This is how the notification channel gets created.  It's safe to call this multiple times
//    //because if a notification channel already exists, this does nothing.
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "myChannel";
//            String description = "my channel description";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }


}
