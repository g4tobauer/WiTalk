package com.desenvolvigames.applications.witalk.fcm.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.desenvolvigames.applications.witalk.R;
import com.desenvolvigames.applications.witalk.activities.LobbyActivity;
import com.desenvolvigames.applications.witalk.entities.Contact;
import com.desenvolvigames.applications.witalk.entities.MessageBody;
import com.desenvolvigames.applications.witalk.utilities.constants.ConstantsClass;
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
            messageBody.data.UserId = remoteMessage.getData().get(getString(R.string.messagebody_data_userid));
            messageBody.data.UserName = remoteMessage.getData().get(getString(R.string.messagebody_data_username));
            messageBody.data.UserMessage = remoteMessage.getData().get(getString(R.string.messagebody_data_usermessage));
            messageBody.data.UserMessageToken = remoteMessage.getData().get(getString(R.string.messagebody_data_usermessagetoken));
        }
        if(ConstantsClass.ContactOpened != null && ConstantsClass.ContactOpened.mUserId != null && ConstantsClass.ContactOpened.mUserId.equals(messageBody.data.UserId))
            sendMessage(messageBody);
        else
            sendNotification(messageBody);
    }

    private void sendMessage(MessageBody messageBody) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.intent_message),messageBody);
        Intent intent = new Intent(messageBody.data.UserId);
        intent.putExtras(bundle);
        broadcaster.sendBroadcast(intent);
    }

    private void sendNotification(MessageBody messageBody){
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.intent_notification),messageBody);

        Intent intent = new Intent(WiTalkFirebaseMessagingService.this, LobbyActivity.class);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        NotificationCompat.Builder mBuilder = null;
        try{

            mBuilder = new NotificationCompat.Builder(WiTalkFirebaseMessagingService.this)
            .setSmallIcon(R.mipmap.ic_notification)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentTitle(URLDecoder.decode(messageBody.notification.title, "UTF-8"))
            .setContentText(URLDecoder.decode(messageBody.notification.body, "UTF-8"))
            .setAutoCancel(true);

        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        if(mBuilder != null) {
            PendingIntent resultPendingIntent = PendingIntent.getActivity(WiTalkFirebaseMessagingService.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            mBuilder.setContentIntent(resultPendingIntent);
            int mNotificationId = 001;
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        } else {
            Log.d(TAG, "Não foi possível criar objeto notificationBuilder");
        }
    }
}
