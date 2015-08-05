package au.radsoft.unread.badger;

import android.content.Context;
import android.content.Intent;

import au.radsoft.unread.ShortcutBadger;

import java.util.Map;

/**
 * @author Gernot Pansy
 */
public class AdwHomeBadger implements ShortcutBadger.Impl {
    public static final String INTENT_UPDATE_COUNTER = "org.adw.launcher.counter.SEND";
    public static final String PACKAGENAME = "PNAME";
    public static final String COUNT = "COUNT";

    private Context mContext;

    public AdwHomeBadger(Context context) {
        mContext = context;
    }

    @Override
    public void executeBadge(String packageName, String entryActivityName, int badgeCount) {
        Intent intent = new Intent(INTENT_UPDATE_COUNTER);
        intent.putExtra(PACKAGENAME, packageName);
        intent.putExtra(COUNT, badgeCount);
        mContext.sendBroadcast(intent);
    }

    public static void register(Map<String, Class<? extends ShortcutBadger.Impl>> r)
    {
        r.put("org.adw.launcher", AdwHomeBadger.class);
        r.put("org.adwfreak.launcher", AdwHomeBadger.class);
    }
}
