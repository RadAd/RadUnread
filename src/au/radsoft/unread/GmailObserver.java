package au.radsoft.unread;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

class GmailObserver extends ContentObserver
{
    private static final String LOG_TAG = GmailObserver.class.getSimpleName();
    
    // NOTE Dont forget to unregister: getContentResolver().unregisterContentObserver(observer);
    static GmailObserver register(Context context, Handler handler)
    {
        GmailObserver observer = new GmailObserver(context, handler);
        for(String account : Gmail.getAllAccountNames(context))
        {
            Uri uri = Gmail.getLabelsUri(account);
            // NOTE TODO Not sure if registerContentObserver can be called twice for the same observer?
            context.getContentResolver().registerContentObserver(uri, false, observer);
        }
        return observer;
    }
    
    private final Context context_;
    private ComponentName componentGmail_;
    
    GmailObserver(Context context, Handler handler)
    {
        super(handler);
        context_ = context;
        getGmailComponentName();
    }
    
    private boolean getGmailComponentName()
    {
        if (componentGmail_ == null)
        {
            Intent intentGmail = context_.getPackageManager().getLaunchIntentForPackage(Gmail.AUTHORITY);
            componentGmail_ = intentGmail == null ? null : intentGmail.getComponent();
            Log.d(LOG_TAG, "ComponentName: " + componentGmail_);
        }
        return componentGmail_ != null;
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
        updateUnread();
    }
    
    boolean updateUnread()
    {
        if (!getGmailComponentName())
            return false;
        int unread = Gmail.getUnreadCount(context_);
        ShortcutBadger.setBadge(context_, componentGmail_, unread);
        return true;
    }
    
    boolean clearUnread()
    {
        if (!getGmailComponentName())
            return false;
        ShortcutBadger.setBadge(context_, componentGmail_, 0);
        return true;
    }
}
