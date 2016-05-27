package cfc.weinelzbeileh;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = "MessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.i(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.i(TAG, "FCM Data Message: " + remoteMessage.getData());
    }
}
