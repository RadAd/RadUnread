package au.radsoft.unread.badger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import au.radsoft.unread.ShortcutBadger;

import java.util.Map;

/**
 * @author Leo Lin
 */
public class NewHtcHomeBadger implements ShortcutBadger.Impl {
    public static final String INTENT_UPDATE_SHORTCUT = "com.htc.launcher.action.UPDATE_SHORTCUT";
    public static final String INTENT_SET_NOTIFICATION = "com.htc.launcher.action.SET_NOTIFICATION";
    public static final String PACKAGENAME = "packagename";
    public static final String COUNT = "count";
    public static final String EXTRA_COMPONENT = "com.htc.launcher.extra.COMPONENT";
    public static final String EXTRA_COUNT = "com.htc.launcher.extra.COUNT";

    private Context mContext;

    public NewHtcHomeBadger(Context context) {
        mContext = context;
    }

    @Override
    public void executeBadge(String packageName, String entryActivityName, int badgeCount) {
        Intent intent1 = new Intent(INTENT_SET_NOTIFICATION);
        ComponentName localComponentName = new ComponentName(packageName, entryActivityName);
        intent1.putExtra(EXTRA_COMPONENT, localComponentName.flattenToShortString());
        intent1.putExtra(EXTRA_COUNT, badgeCount);
        mContext.sendBroadcast(intent1);

        Intent intent = new Intent(INTENT_UPDATE_SHORTCUT);
        intent.putExtra(PACKAGENAME, packageName);
        intent.putExtra(COUNT, badgeCount);
        mContext.sendBroadcast(intent);
    }

    public static void register(Map<String, Class<? extends ShortcutBadger.Impl>> r)
    {
        r.put("com.htc.launcher", NewHtcHomeBadger.class);
    }
}
