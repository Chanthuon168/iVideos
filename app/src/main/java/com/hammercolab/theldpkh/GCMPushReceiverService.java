package com.hammercolab.theldpkh;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by NgocTri on 4/9/2016.
 */
public class GCMPushReceiverService extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d("data", data.toString());
        String status = data.getString("status");
        String message = data.getString("message");
        String image = data.getString("image");
        if (status.equals("global")) {
            int id = data.getInt("id");
            String code = data.getString("code");
            String viewCount = data.getString("viewCount");
            String likeCount = data.getString("likeCount");
            String dislikeCount = data.getString("dislikeCount");
            sendNotification(message, image, id, code, viewCount, likeCount, dislikeCount);
        } else {
            sendNewsNotification(message, image);
        }
    }

    private void sendNotification(String message, String image, int id, String code, String viewCount, String likeCount, String dislikeCount) {
        Intent intent = new Intent(this, ActivityPlay.class);
        intent.putExtra("id", id);
        intent.putExtra("code", code);
        intent.putExtra("title", message);
        intent.putExtra("viewCount", viewCount);
        intent.putExtra("likeCount", likeCount);
        intent.putExtra("dislikeCount", dislikeCount);
        intent.putExtra("favorite", "uncheck");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;//Your request code
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle("LDP Cambodia");
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(getBitmapFromURL(image));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        final int icon = R.mipmap.ic_launcher;
        //Setup notification
        //Sound
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Build notification
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("LDP Cambodia")
                .setContentText(message)
                .setStyle(bigPictureStyle)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), icon))
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
    }

    private void sendNewsNotification(String message, String image) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra("title", message);
        intent.putExtra("position", 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode = 0;//Your request code
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.setBigContentTitle("LDP Cambodia");
        bigPictureStyle.setSummaryText(Html.fromHtml(message).toString());
        bigPictureStyle.bigPicture(getBitmapFromURL(image));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
        final int icon = R.mipmap.ic_launcher;
        //Setup notification
        //Sound
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Build notification
        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("LDP Cambodia")
                .setContentText(message)
                .setStyle(bigPictureStyle)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), icon))
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
