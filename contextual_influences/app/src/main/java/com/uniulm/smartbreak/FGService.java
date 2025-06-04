package com.uniulm.smartbreak;

import static com.uniulm.smartbreak.App.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.uniulm.social_media_interventions.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class FGService extends Service {

    int n = 0;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Date currentTime = Calendar.getInstance().getTime();
        //String date = new SimpleDateFormat("dd-MM-yyyy_HHmmss", Locale.getDefault()).format(new Date());
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true){
                            String date = new SimpleDateFormat("dd-MM-yyyy_HHmmss", Locale.getDefault()).format(new Date());
                            Log.e("Service", "Service is running "+date + " " + n);
                            n++;

                            sendNotification( n , pendingIntent);

                            try{
                                Thread.sleep(2000);
                            }
                            catch ( InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();






        //return START_STICKY;

        return super.onStartCommand(intent, flags, startId);
    }

    public void sendNotification(int timer, PendingIntent pendingIntent){
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Timer pressed "+ timer+ " times")
                .setContentText("lul")
                .setSmallIcon(R.drawable.ic_baseline_back_hand_24)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        startForeground(1, notification);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
