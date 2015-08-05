package au.radsoft.unread;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
//import android.util.Log;

class GmailObserver extends ContentObserver
{
    private static final String LOG_TAG = GmailObserver.class.getSimpleName();
    
    // NOTE Dont forget to unregister: getContentResolver().unregisterContentObserver(observer);
    static ContentObserver register(Context context, Handler handler)
    {
        ContentObserver observer = new GmailObserver(context, handler);
        for(String account : Gmail.getAllAccountNames(context))
        {
            Uri uri = Gmail.getLabelsUri(account);
            // NOTE TODO Not sure if registerContentObserver can be called twice for the same observer?
            context.getContentResolver().registerContentObserver(uri, false, observer);
        }
        return observer;
    }
    
    private final Context context_;
    
    GmailObserver(Context context, Handler handler)
    {
        super(handler);
        context_ = context;
    }
    
    @Override
    public void onChange(boolean selfChange)
    {
        onChange(selfChange, null);
    }
    
    //@Override API 16
    public void onChange(boolean selfChange, Uri uri)
    {
        //Log.d(LOG_TAG, "onChange " + uri);
        updateUnread(context_);
    }
    
    static void updateUnread(Context context)
    {
        int unread = Gmail.getUnreadCount(context);
        ShortcutBadger.setBadge(context, Gmail.AUTHORITY, unread);
    }
    
    static void clearUnread(Context context)
    {
        ShortcutBadger.setBadge(context, Gmail.AUTHORITY, 0);
    }
}
