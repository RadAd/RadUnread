package au.radsoft.unread;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.Preference;
import android.preference.PreferenceManager;
//import android.util.Log;

//public class RadUnread extends android.app.Activity
public class RadUnread extends android.preference.PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final String LOG_TAG = RadUnread.class.getSimpleName();
    
    private static final String PREF_TESLA_ENABLE = "pref_tesla_enable";
    
    @Override
    public void onCreate(android.os.Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
        //Preference connectionPref = findPreference(key);
        onSharedPreferenceChanged(sharedPref, PREF_TESLA_ENABLE);
        
        UpdateService.start(this);
    }
    
    @Override // OnSharedPreferenceChangeListener
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        //toast("Pref changed " + key);
        if (PREF_TESLA_ENABLE.equals(key))
        {
            int state = sharedPreferences.getBoolean(key, false) ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
            ComponentName component = new ComponentName(this, TeslaContentProvider.class);
            getPackageManager().setComponentEnabledSetting(component, state, PackageManager.DONT_KILL_APP);
        }
    }
    
    //private void toast(String msg)
    //{
        //android.widget.Toast toast = android.widget.Toast.makeText(this, msg, android.widget.Toast.LENGTH_LONG);
        //toast.show();
    //}
}
