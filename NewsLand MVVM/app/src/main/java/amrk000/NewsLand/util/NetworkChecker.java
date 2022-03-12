package amrk000.NewsLand.util;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import amrk000.NewsLand.R;

public class NetworkChecker extends BroadcastReceiver {
    private Snackbar snackbar;
    private ConnectivityManager connectivityManager;

    public  NetworkChecker(){}

    public NetworkChecker(Activity activity){
        connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        snackbar = Snackbar.make(activity.findViewById(R.id.snakBarCoordinatorLayout),"No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                .setBackgroundTint(Color.RED)
                .setTextColor(Color.WHITE)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnected()) snackbar.dismiss();
        else snackbar.show();

    }
}