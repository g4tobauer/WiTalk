package com.desenvolvigames.applications.witalk.fcm.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.activities.LobbyActivity;
import com.desenvolvigames.applications.witalk.entities.MessageBody;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static android.content.ContentValues.TAG;

public class WiTalkFirebaseMessagingService extends FirebaseMessagingService{
    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        MessageBody messageBody = new MessageBody();
        messageBody.notification.title = remoteMessage.getNotification().getTitle();
        messageBody.notification.icon = remoteMessage.getNotification().getIcon();
        messageBody.notification.body = remoteMessage.getNotification().getBody();

        if (!remoteMessage.getData().isEmpty()) {
            messageBody.data.UserId = remoteMessage.getData().get("UserId");
            messageBody.data.UserName = remoteMessage.getData().get("UserName");
            messageBody.data.UserMessage = remoteMessage.getData().get("UserMessage");
            messageBody.data.UserMessageToken = remoteMessage.getData().get("UserMessageToken");
        }
        this.sendMessage(messageBody);
//        this.sendNotification(messageBody);
    }

    private void sendNotification(MessageBody messageBody) {

        Intent intent = new Intent(this, LobbyActivity.class);
        intent.putExtra(getString(R.string.intent_notification), messageBody.notification.body);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = null;
        try {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_notification)
                    .setContentTitle(URLDecoder.decode(messageBody.notification.title, "UTF-8"))
                    .setContentText(URLDecoder.decode(messageBody.notification.body, "UTF-8"))
                    .setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentIntent(pendingIntent);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (notificationBuilder != null) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        } else {
            Log.d(TAG, "Não foi possível criar objeto notificationBuilder");
        }
    }

    private void sendMessage(MessageBody messageBody) {
        Intent intent = new Intent(messageBody.data.UserId);
        intent.putExtra(getString(R.string.intent_message), messageBody.data.UserMessage);
        broadcaster.sendBroadcast(intent);
    }
}
