package au.radsoft.unread;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import android.util.Log;

import au.radsoft.unread.badger.*;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Leo Lin
 */
public class ShortcutBadger {
    private static final String LOG_TAG = ShortcutBadger.class.getSimpleName();

    private static final Map<String, Class<? extends ShortcutBadger.Impl>> BADGERS = new HashMap<String, Class<? extends ShortcutBadger.Impl>>();

    static {
        AdwHomeBadger.register(BADGERS);
        ApexHomeBadger.register(BADGERS);
        AsusHomeLauncher.register(BADGERS);
        LGHomeBadger.register(BADGERS);
        NewHtcHomeBadger.register(BADGERS);
        //NovaHomeBadger.register(BADGERS);
        SamsungHomeBadger.register(BADGERS);
        SolidHomeBadger.register(BADGERS);
        SonyHomeBadger.register(BADGERS);
        XiaomiHomeBadger.register(BADGERS);
    }

    private static ShortcutBadger.Impl mShortcutBadger;

    public static ShortcutBadger.Impl with(Context context) {
        return getShortcutBadger(context);
    }

    public static void setBadge(Context context, int badgeCount) {
        setBadge(context, getContextPackageName(context), badgeCount);
    }

    public static void setBadge(Context context, String packageName, int badgeCount) {
        setBadge(context, packageName, getEntryActivityName(context, packageName), badgeCount);
    }
    
    public static void setBadge(Context context, String packageName, String activityName, int badgeCount) {
        Log.d(LOG_TAG, "setBadge " + packageName + " " + activityName + " " + badgeCount);
        getShortcutBadger(context).executeBadge(packageName, activityName, badgeCount);
    }
    
    private static ShortcutBadger.Impl getShortcutBadger(Context context) {
        if (mShortcutBadger != null) {
            return mShortcutBadger;
        }
        
        Log.d(LOG_TAG, "Finding badger");
        //find the home launcher Package
        try {
            String currentHomePackage = getHomePackage(context);

            if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
                mShortcutBadger = new XiaomiHomeBadger(context);
            }
            else
            {
                Class<? extends ShortcutBadger.Impl> badger = BADGERS.get(currentHomePackage);
                if (badger != null)
                {
                    Constructor<? extends ShortcutBadger.Impl> constructor = badger.getConstructor(Context.class);
                    mShortcutBadger = constructor.newInstance(context);
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }

        if (mShortcutBadger == null) {
            mShortcutBadger = new DefaultBadger(context);
        }

        Log.d(LOG_TAG, "Returning badger:" + mShortcutBadger.getClass().getCanonicalName());
        return mShortcutBadger;
    }

    private ShortcutBadger() {
    }

    public interface Impl {
        void executeBadge(String packageName, String entryActivityName, int badgeCount);
    }
    
    private static String getHomePackage(Context context)
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

    /*private*/ static String getEntryActivityName(Context context) {
        ComponentName componentName = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent();
        return componentName.getClassName();
    }

    /*private*/ static String getEntryActivityName(Context context, String packageName) {
        ComponentName componentName = context.getPackageManager().getLaunchIntentForPackage(packageName).getComponent();
        return componentName.getClassName();
    }

    /*private*/ static String getContextPackageName(Context context) {
        return context.getPackageName();
    }
}
