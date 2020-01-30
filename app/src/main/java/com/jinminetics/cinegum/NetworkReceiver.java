package com.jinminetics.cinegum;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.jinminetics.cinegum.utils.StaticMethods;

import java.util.HashMap;
import java.util.Map;

public class NetworkReceiver extends BroadcastReceiver {
    private final String TAG = "NetworkReceiver";
    public static final String NOTIFY_NETWORK_CHANGE = "NOTIFY_NETWORK_CHANGE";
    public static final String EXTRA_IS_CONNECTED = "EXTRA_IS_CONNECTED";
    public static boolean isActive = false;
    public static Map<String, OnInternetConnectionDetectedTask> onInternetConnectionDetectedTasksFactory =
            new HashMap<>();
    public static Map<String, OnInternetConnectionDetectedTask> onInternetConnectionDetectedTasks =
            new HashMap<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        isActive = isOnline(context);
        Intent localIntent = new Intent(NOTIFY_NETWORK_CHANGE);
        localIntent.putExtra(EXTRA_IS_CONNECTED, isOnline(context));
        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
        StaticMethods.log("NetWork", "isActive", isActive);
        Activity activity = CustomApplication.mActivity ; // Getting Current Activity

        //using the temp variable instead of the onInternetConnectionDetectedTasks in order to avoid
        // index error in case any of the interface methods in the below loop are removing the interface from the
        // onInternetConnectionDetectedTasks list
        //if we have any task to do, then let's take the tasks to the factory and do them :)
        if(onInternetConnectionDetectedTasks.size() > 0) {
            onInternetConnectionDetectedTasksFactory.putAll(onInternetConnectionDetectedTasks);
            onInternetConnectionDetectedTasks.clear();
            for(Map.Entry<String, OnInternetConnectionDetectedTask> onInternetConnectionDetectedTasksFactory :
                    onInternetConnectionDetectedTasksFactory.entrySet()){
                OnInternetConnectionDetectedTask task = onInternetConnectionDetectedTasksFactory.getValue();
                if (isActive) {
                    //if internet connection is active
                    try {
                        if(task != null)task.onConnected();
                    } catch (Exception e){}
                }
            }
        }
    }
    //returns internet connection
    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    public interface OnInternetConnectionDetectedTask {
        void onConnected();
    }

    /*
    //Activity code
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("Status");
            Bundle b = intent.getBundleExtra("Location");
            lastKnownLoc = (Location) b.getParcelable("Location");
            if (lastKnownLoc != null) {
                tvLatitude.setText(String.valueOf(lastKnownLoc.getLatitude()));
                tvLongitude
                        .setText(String.valueOf(lastKnownLoc.getLongitude()));
                tvAccuracy.setText(String.valueOf(lastKnownLoc.getAccuracy()));
                tvTimestamp.setText((new Date(lastKnownLoc.getTime())
                        .toString()));
                tvProvider.setText(lastKnownLoc.getProvider());
            }
            tvStatus.setText(message);
            // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    };
    LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
            mMessageReceiver, new IntentFilter("GPSLocationUpdates"));
    //service code
    private static void sendMessageToActivity(Location l, String msg) {
        Intent intent = new Intent("GPSLocationUpdates");
        // You can also include some extra data.
        intent.putExtra("Status", msg);
        Bundle b = new Bundle();
        b.putParcelable("Location", l);
        intent.putExtra("Location", b);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    */
}
