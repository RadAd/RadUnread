package au.radsoft.unread;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.os.SystemClock;
import android.preference.PreferenceManager;
//import android.util.Log;

public class UpdateService extends android.app.Service implements SharedPreferences.OnSharedPreferenceChangeListener
{
    //private static final String LOG_TAG = UpdateService.class.getSimpleName();
    
    private static final String PREF_GMAIL_UNREAD_ENABLE = "pref_gmail_unread_enable";
    
    static void start(Context context)
    {
        Intent intent = new Intent(context, UpdateService.class);
        context.startService(intent);
    }
    
    static void stop(Context context)
    {
        Intent intent = new Intent(context, UpdateService.class);
        context.stopService(intent);
    }
    
    private GmailObserver observerGmail_ = null;
    
    public UpdateService()
    {
    }
    
    @Override
    public void onCreate()
    {
        //Log.d(LOG_TAG, "onCreate");
        super.onCreate();
        
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        
        onSharedPreferenceChanged(sharedPref, PREF_GMAIL_UNREAD_ENABLE);
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //Log.d(LOG_TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
    
    @Override
    public void onDestroy()
    {
        //Log.d(LOG_TAG, "onDestroy");
        super.onDestroy();
        
        if (observerGmail_ != null)
            getContentResolver().unregisterContentObserver(observerGmail_);
        observerGmail_ = null;
    }
    
    @Override
    public android.os.IBinder onBind(Intent intent)
    {
        return null;
    }
    
    @Override // OnSharedPreferenceChangeListener
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        //toast("Pref changed " + key);
        if (PREF_GMAIL_UNREAD_ENABLE.equals(key))
        {
            if (sharedPreferences.getBoolean(key, false))
            {
                if (observerGmail_ == null)
                {
                    //toast("Gmail start");
                    observerGmail_ = GmailObserver.register(this, null);
                    if (!observerGmail_.updateUnread())
                        toast("Gmail not instaalled.");
                }
            }
            else
            {
                if (observerGmail_ != null)
                {
                    //toast("Gmail stop");
                    observerGmail_.clearUnread();
                    getContentResolver().unregisterContentObserver(observerGmail_);
                    observerGmail_ = null;
                    
                }
            }
        }
    }
    
    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        //Log.d(LOG_TAG, "onTaskRemoved");
        
        Context appContext = getApplicationContext();
        Intent restartServiceIntent = new Intent(appContext, UpdateService.class);
        restartServiceIntent.setPackage(getPackageName());

        // TODO Use JobScheduler API rather than AlarmManager
        // Restart service http://stackoverflow.com/a/20781110/2566649
        // http://www.vogella.com/tutorials/AndroidTaskScheduling/article.html
        
        PendingIntent restartServicePendingIntent = PendingIntent.getService(appContext, 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 5 * 60 * 1000, restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }

    private void toast(String msg)
    {
        android.widget.Toast toast = android.widget.Toast.makeText(this, msg, android.widget.Toast.LENGTH_LONG);
        toast.show();
    }
}
