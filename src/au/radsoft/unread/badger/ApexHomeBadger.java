package au.radsoft.unread.badger;

import android.content.Context;
import android.content.Intent;

import au.radsoft.unread.ShortcutBadger;

import java.util.Map;

/**
 * @author Gernot Pansy
 */
public class ApexHomeBadger implements ShortcutBadger.Impl {
    private static final String INTENT_UPDATE_COUNTER = "com.anddoes.launcher.COUNTER_CHANGED";
    private static final String PACKAGENAME = "package";
    private static final String COUNT = "count";
    private static final String CLASS = "class";
    
    private Context mContext;

    public ApexHomeBadger(Context context) {
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
        r.put("com.anddoes.launcher", ApexHomeBadger.class);
    }
}
