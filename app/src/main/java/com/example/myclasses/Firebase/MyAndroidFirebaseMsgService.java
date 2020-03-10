package com.example.myclasses.Firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.example.myclasses.PrefsUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

public class MyAndroidFirebaseMsgService extends FirebaseMessagingService {

    private static final String TAG = "MyAndroidFCMService";
    private String channerId = "";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onNewToken(String deviceId) {
        super.onNewToken(deviceId);


        Log.e(TAG, "onNewToken :: " + deviceId);
        PrefsUtil.with(this).write("device_token", deviceId);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Log data to Log Cat

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "Notification DATA: " + remoteMessage.getData().toString());
        Log.e(TAG, "Notification BODY: " + remoteMessage.getData().get("body"));
        Log.e(TAG, "Notification BODY: " + remoteMessage.getData().get("type"));
        Log.e(TAG, "Notification Title: " + remoteMessage.getData().get("title"));

        try {
            JSONObject obj = new JSONObject(remoteMessage.getData());
            Log.e(TAG, "Json Obj:: " + obj);
//            createNotification(remoteMessage.getData().get("body"), remoteMessage.getData().get("title"), remoteMessage.getData());

        } catch (Exception e) {
            Log.e(TAG, "Exception :: " + e);

        }
       /* Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.e(TAG, "Notification TITLE: " + remoteMessage.getNotification().getTitle());
        Log.e(TAG, "Notification SOUND: " + remoteMessage.getNotification().getSound());*/

        //create notification_push
        // createNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), remoteMessage.getData());
    }

   /* private void createNotification(String messageBody, String messageTitle, Map<String, String> data) {

        PendingIntent resultIntent = null;

        if (data.get("type").equals("request_accepted")) {
            Log.e(TAG, "Accepted SERVICE REQUEST");

            Intent intent = new Intent(this, RequestAcceptedActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            intent.putExtra("reqAccepted", true);
            intent.putExtra("washerName", data.get("washer_name"));
            intent.putExtra("washerLat", data.get("washer_lat"));
            intent.putExtra("washerLong", data.get("washer_long"));
            intent.putExtra("serviceId", data.get("service_id"));
            intent.putExtra("FromNotification", true);

            startActivity(intent);

            resultIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);

        } else if (data.get("type").equals("replace_service_washer")) {
            Log.e(TAG, "Replaced SERVICE REQUEST");

            Intent intent = new Intent(this, DrawerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("reqReplaced", true);
            resultIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);

        } else if (data.get("type").equals("service_pickup_customer")) {
            Log.e(TAG, "PickUp SERVICE REQUEST");

            Intent intent = new Intent(this, DrawerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("reqAccepted", true);
            resultIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);

        } else if (data.get("type").equals("service_start_customer")) {
            Log.e(TAG, "Start SERVICE REQUEST");

            Intent intent = new Intent(this, DrawerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("reqStartService", true);
            resultIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);

        } else if (data.get("type").equals("service_complete_customer")) {
            Log.e(TAG, "Start SERVICE REQUEST");

            Intent intent = new Intent(this, RatingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("reqCompleteService", true);
            intent.putExtra("serviceId", data.get("service_id"));
            resultIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);

        } else if (data.get("type").equals("wallet_transaction")) {
            Log.e(TAG, "Start SERVICE REQUEST");

            Intent intent = new Intent(this, DrawerHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("reqFinancialInfo", true);
            resultIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);

        } else if (data.get("type").equalsIgnoreCase("new_service_request")) {
            Intent i = new Intent(this, CwNewServiceRequestActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.putExtra("title", data.get("title"));
            i.putExtra("service_id", data.get("service_id"));
            i.putExtra("customer_id", data.get("customer_id"));
            i.putExtra("service_map_lat", data.get("service_map_lat"));
            i.putExtra("service_map_long", data.get("service_map_long"));
            i.putExtra("request_timeout_minutes", data.get("request_timeout_minutes"));
            i.putExtra("pickup_location", data.get("pickup_location"));
            i.putExtra("FromNotification", true);
            startActivity(i);
        } else if (data.get("type").equalsIgnoreCase("repeat_service_request")) {
            Intent i = new Intent(this, CwNewServiceRequestActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            i.putExtra("title", data.get("title"));
            i.putExtra("service_id", data.get("service_id"));
            i.putExtra("customer_id", data.get("customer_id"));
            i.putExtra("service_map_lat", data.get("service_map_lat"));
            i.putExtra("service_map_long", data.get("service_map_long"));
            i.putExtra("request_timeout_minutes", data.get("request_timeout_minutes"));
            i.putExtra("pickup_location", data.get("pickup_location"));
            i.putExtra("FromNotification", true);
            startActivity(i);

        } else if (data.get("type").equalsIgnoreCase("no_washer_found")) {
            Intent i = new Intent(this, CustomerNewRequestServiceActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra("title", data.get("title"));
            i.putExtra("body", data.get("body"));
            i.putExtra("noWasherFound", true);
            startActivity(i);
            resultIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), i, PendingIntent.FLAG_ONE_SHOT);
        } else if (data.get("type").equalsIgnoreCase("logout")) {
            Intent i = new Intent(this, CustomerLoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PrefsUtil.with(this).clearPrefs();
            startActivity(i);
            resultIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), i, PendingIntent.FLAG_ONE_SHOT);
        }

        // resultIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_ONE_SHOT);

        //   if (resultIntent != null) {

       *//* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channerId = new ChannelIdUtils().createNotificationChannel(getApplicationContext());
            Log.e("LocationUpdateService", "channelId----->" + channerId);
        } else {
            // If earlier version channel ID is not used
            // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
        }*//*

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // The id of the channel.
            String Ch_id = "carwasher";

            // The user-visible name of the channel.
            CharSequence name = getString(R.string.channel_name);

            // The user-visible description of the channel.
            //String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(Ch_id, name, importance);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            mChannel.setSound(notificationSoundURI, audioAttributes);
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableLights(true);

            mChannel.setVibrationPattern(new long[]{0});
            mChannel.enableVibration(true);


            // Create a notification and set the notification channel.
            notification = new Notification.Builder(this, Ch_id)
                    .setSmallIcon(R.mipmap.ic_launcher_circle)
                    .setContentTitle(messageTitle)
//                    .setColor(ContextCompat.getColor(this, R.color.bright_sky_blue))
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setStyle(new Notification.BigTextStyle().bigText(messageBody))
                    .setContentIntent(resultIntent)
                    .setChannelId(Ch_id)
                    .build();
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // Create a notification
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher_circle)
                    .setContentTitle(messageTitle)
//                    .setColor(ContextCompat.getColor(this, R.color.bright_sky_blue))
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setStyle(new Notification.BigTextStyle().bigText(messageBody))
                    .setSound(notificationSoundURI)
                    .setLights(Color.BLUE, 1000, 1000)
                    .setContentIntent(resultIntent)
                    .build();
        } else {
            // Create a notification
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher_circle)
                    .setContentTitle(messageTitle)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setStyle(new Notification.BigTextStyle().bigText(messageBody))
                    .setSound(notificationSoundURI)
                    .setLights(Color.BLUE, 1000, 1000)
                    .setContentIntent(resultIntent)
                    .build();
        }

        assert notificationManager != null;
        notificationManager.notify(0, notification);

        //   }
    }*/
}
