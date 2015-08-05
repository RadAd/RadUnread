package au.radsoft.unread.badger;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import au.radsoft.unread.ShortcutBadger;

import java.util.Map;

/**
 * Shortcut Badger support for Nova Launcher.
 * TeslaUnread must be installed.
 * User: Gernot Pansy
 * Date: 2014/11/03
 * Time: 7:15
 */
public class NovaHomeBadger implements ShortcutBadger.Impl {
    private static final String CONTENT_URI = "content://com.teslacoilsw.notifier/unread_count";
    private static final String COUNT = "count";
    private static final String TAG = "tag";

    private Context mContext;

    public NovaHomeBadger(Context context) {
        mContext = context;
    }

    @Override
    public void executeBadge(String packageName, String entryActivityName, int badgeCount) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TAG, packageName + "/" + entryActivityName);
            contentValues.put(COUNT, badgeCount);
            mContext.getContentResolver().insert(Uri.parse(CONTENT_URI), contentValues);
        } catch (IllegalArgumentException ex) {
            /* Fine, TeslaUnread is not installed. */
        }
    }

    public static void register(Map<String, Class<? extends ShortcutBadger.Impl>> r)
    {
        r.put("com.teslacoilsw.launcher", NovaHomeBadger.class);
    }
}
