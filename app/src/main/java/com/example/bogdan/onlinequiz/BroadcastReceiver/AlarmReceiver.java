package com.example.bogdan.onlinequiz.BroadcastReceiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.bogdan.onlinequiz.MainActivity;
import com.example.bogdan.onlinequiz.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("default","ChannelName",NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("channel's description");
            notificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context ,"default")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Online Quiz App")
                    .setContentText("Hey! Don't be lazy today :) !")
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setVibrate(new long[]{1000,1000,1000,1000})
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(0,builder.build());

    }

}
