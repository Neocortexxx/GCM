package de.android.philipp.uitest;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GCMNotificationIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentService";

    @Override
    protected void onHandleIntent(Intent intent)
    {
        Bundle extras = intent.getExtras();

        if (extras.get("groupname") != null && extras.get("inviter") != null)
            sendNotification(extras.getString("inviter"), extras.getString("groupname"));

        Log.i(TAG, "Received: " + extras.toString());


        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String inviter, String groupname) {
         mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intentAnnehmen = new Intent(this, MainActivity.class);
        Intent intentAblehnen = new Intent(this, MainActivity.class);

        intentAnnehmen.putExtra("AcceptGroupInvite", "1");
        intentAnnehmen.putExtra("Groupname", groupname);

        intentAblehnen.putExtra("AcceptGroupInvite", "0");
        intentAblehnen.putExtra("Groupname", groupname);

        PendingIntent contentIntentAnnehmen = PendingIntent.getActivity(this, 0, intentAnnehmen, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent contentIntentAblehnen = PendingIntent.getActivity(this, 1, intentAblehnen, PendingIntent.FLAG_UPDATE_CURRENT);

        String msg = "Neue Gruppenanfrage von " + inviter + ": Einladung in die Gruppe " + groupname;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.drawable.note)
                .setContentTitle("GCM Notification")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .addAction(R.drawable.note,"Annehmen", contentIntentAnnehmen)
                .addAction(R.drawable.note,"Ablehnen", contentIntentAblehnen);


        mBuilder.setContentIntent(contentIntentAnnehmen);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Log.d(TAG, "Notification sent successfully.");
    }
}