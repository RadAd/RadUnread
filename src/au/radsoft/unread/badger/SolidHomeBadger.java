package au.radsoft.unread.badger;

import android.content.Context;
import android.content.Intent;

import au.radsoft.unread.ShortcutBadger;

import java.util.Map;

/**
 * @author MajeurAndroid
 */
public class SolidHomeBadger implements ShortcutBadger.Impl {
    private static final String INTENT_UPDATE_COUNTER = "com.majeur.launcher.intent.action.UPDATE_BADGE";
    private static final String PACKAGENAME = "com.majeur.launcher.intent.extra.BADGE_PACKAGE";
    private static final String COUNT = "com.majeur.launcher.intent.extra.BADGE_COUNT";
    private static final String CLASS = "com.majeur.launcher.intent.extra.BADGE_CLASS";

    private Context mContext;

    public SolidHomeBadger(Context context) {
        mContext = context;
    }

    @Override
    public void executeBadge(String packageName, String entryActivityName, int badgeCount) {
        Intent intent = new Intent(INTENT_UPDATE_COUNTER);
        intent.putExtra(PACKAGENAME, packageName);
        intent.putExtra(COUNT, badgeCount);
        intent.putExtra(CLASS, entryActivityName);
        mContext.sendBroadcast(intent);
    }

    public static void register(Map<String, Class<? extends ShortcutBadger.Impl>> r)
    {
        r.put("com.majeur.launcher", SolidHomeBadger.class);
    }
}
