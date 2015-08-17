package au.radsoft.unread;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class TeslaContentProvider extends android.content.ContentProvider
{
    private static final String LOG_TAG = TeslaContentProvider.class.getSimpleName();
    
    private static final String CONTENT_URI = "content://com.teslacoilsw.notifier/unread_count";
    private static final String COUNT = "count";
    private static final String TAG = "tag";
    
    private static final Uri mUri = Uri.parse(CONTENT_URI);
    
    public boolean onCreate()
    {
        //Log.d(LOG_TAG, "onCreate");
        return true;
    }
    
    public String getType(Uri uri)
    {
        Log.d(LOG_TAG, "getType " + uri.toString());
        return new String();
    }
    
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        Log.d(LOG_TAG, "delete " + uri.toString());
        return 0;
    }
    
    public Uri insert(Uri uri, ContentValues values)
    {
        Log.d(LOG_TAG, "insert " + uri.toString());
        if (mUri.equals(uri))
        {
            String tag = values.getAsString(TAG);
            int badgeCount = values.getAsInteger(COUNT);
            Log.d(LOG_TAG, "values " + tag + " " + badgeCount);
            ComponentName localComponentName = ComponentName.unflattenFromString(tag);
            if (localComponentName != null)
                ShortcutBadger.setBadge(getContext(), localComponentName, badgeCount);
            else
                ShortcutBadger.setBadge(getContext(), tag, badgeCount);
        }
        return null;
    }
    
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        Log.d(LOG_TAG, "update " + uri.toString());
        return 0;
    }
    
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        Log.d(LOG_TAG, "query " + uri.toString());
        return null;
    }
}
