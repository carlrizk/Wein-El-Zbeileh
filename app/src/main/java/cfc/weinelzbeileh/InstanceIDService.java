package cfc.weinelzbeileh;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class InstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "InstanceIDService";
    private static final String WEIN_EL_ZBEILEH_TOPIC = "WEIN_EL_ZBEILEH_TOPIC";

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "FCM Token: " + token);
        FirebaseMessaging.getInstance().subscribeToTopic(WEIN_EL_ZBEILEH_TOPIC);
    }

}
