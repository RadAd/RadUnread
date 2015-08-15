package au.radsoft.unread.badger;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import au.radsoft.unread.ShortcutBadger;

import java.util.Map;

/**
 * @author Leo Lin
 */
public class SamsungHomeBadger implements ShortcutBadger.Impl {
    private static final String CONTENT_URI = "content://com.sec.badge/apps?notify=true";
    private static final String[] CONTENT_PROJECTION = new String[] { "_id", "class" };
    private static final String ID = "_id";
    private static final String BADGECOUNT = "badgecount";
    private static final String CLASS = "class";
    private static final String PACKAGE = "package";
    private static final String QUERY_PACKAGE = "package=?";
    private static final String QUERY_ID = "_id=?";

    private Context mContext;

    public SamsungHomeBadger(Context context) {
        mContext = context;
    }

    @Override
    public void executeBadge(String packageName, String entryActivityName, int badgeCount) {
        Uri mUri = Uri.parse(CONTENT_URI);
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(mUri, CONTENT_PROJECTION, QUERY_PACKAGE, new String[]{packageName}, null);
            if (cursor != null) {
                int colId = cursor.getColumnIndex(ID);
                int colClass = cursor.getColumnIndex(CLASS);
                boolean entryActivityExist = false;
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(colId);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(BADGECOUNT, badgeCount);
                    contentResolver.update(mUri, contentValues, QUERY_ID, new String[]{Integer.toString(id)});
                    if (entryActivityName.equals(cursor.getString(colClass))) {
                        entryActivityExist = true;
                    }
                }

                if (!entryActivityExist) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(PACKAGE, packageName);
                    contentValues.put(CLASS, entryActivityName);
                    contentValues.put(BADGECOUNT, badgeCount);
                    contentResolver.insert(mUri, contentValues);
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public static void register(Map<String, Class<? extends ShortcutBadger.Impl>> r)
    {
        r.put("com.sec.android.app.launcher", SamsungHomeBadger.class);
        r.put("com.sec.android.app.twlauncher", SamsungHomeBadger.class);
    }
}
