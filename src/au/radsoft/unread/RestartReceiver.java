package au.radsoft.unread;

import android.content.Intent;
import android.util.Log;

public class RestartReceiver extends android.content.BroadcastReceiver
{
    private static final String LOG_TAG = RestartReceiver.class.getSimpleName();
    
    @Override
    public void onReceive(android.content.Context context, Intent intent)
    {
        Log.d(LOG_TAG, "onReceive " + intent.getAction());
        UpdateService.start(context);
    }
}
