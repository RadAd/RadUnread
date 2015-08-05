package au.radsoft.unread.badger;

import android.content.Context;
import android.content.Intent;

import au.radsoft.unread.ShortcutBadger;

import java.util.Map;

/**
 * @author Leo Lin
 */
public class SonyHomeBadger implements ShortcutBadger.Impl {
    private static final String INTENT_ACTION = "com.sonyericsson.home.action.UPDATE_BADGE";
    private static final String INTENT_EXTRA_PACKAGE_NAME = "com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME";
    private static final String INTENT_EXTRA_ACTIVITY_NAME = "com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME";
    private static final String INTENT_EXTRA_MESSAGE = "com.sonyericsson.home.intent.extra.badge.MESSAGE";
    private static final String INTENT_EXTRA_SHOW_MESSAGE = "com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE";

    private Context mContext;

    public SonyHomeBadger(Context context) {
        mContext = context;
    }

    @Override
    public void executeBadge(String packageName, String entryActivityName, int badgeCount) {
        Intent intent = new Intent(INTENT_ACTION);
        intent.putExtra(INTENT_EXTRA_PACKAGE_NAME, packageName);
        intent.putExtra(INTENT_EXTRA_ACTIVITY_NAME, entryActivityName);
        intent.putExtra(INTENT_EXTRA_MESSAGE, String.valueOf(badgeCount));
        intent.putExtra(INTENT_EXTRA_SHOW_MESSAGE, badgeCount > 0);
        mContext.sendBroadcast(intent);
    }

    public static void register(Map<String, Class<? extends ShortcutBadger.Impl>> r)
    {
        r.put("com.sonyericsson.home", SonyHomeBadger.class);
    }
}
