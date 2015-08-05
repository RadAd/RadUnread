package au.radsoft.unread;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

//import android.util.Log;

public class Gmail
{
    private static final String LOG_TAG = Gmail.class.getSimpleName();
    
    public static final String AUTHORITY = "com.google.android.gm";
    static final String BASE_URI_STRING = "content://" + AUTHORITY + "/";
    static final String LABELS_PARAM = "/labels";
    static final String ACCOUNT_TYPE_GMAIL_GOOGLE = "com.google";
    static final String ACCOUNT_TYPE_GMAIL_LEGACY = "com.google.android.gm.legacyimap";
    static final String ACCOUNT_TYPE_GMAIL_EXCHANGE = "com.google.android.gm.exchange";

    public static final String NUM_UNREAD_CONVERSATIONS = "numUnreadConversations";
    public static final String CANONICAL_NAME = "canonicalName";

    static final String CANONICAL_NAME_INBOX = "^i";
    static final String CANONICAL_NAME_INBOX_CATEGORY_PRIMARY = "^sq_ig_i_personal";
    static String[] GMAIL_PROJECTION = { CANONICAL_NAME, NUM_UNREAD_CONVERSATIONS };
    
    private Gmail()
    {
    }

    public static Uri getLabelsUri(String account)
    {
        return Uri.parse(BASE_URI_STRING + account + LABELS_PARAM);
    }
    
    private static Account[] combine(Account[] ... accounts)
    {
        int size = 0;
        for (Account[] as : accounts)
        {
            size += as.length;
        }
        Account[] out = new Account[size];
        int i = 0;
        for (Account[] as : accounts)
        {
            for (Account a : as)
            {
                out[i++] = a;
            }
        }
        return out;
    }

    public static String[] getAllAccountNames(Context context)
    {
        AccountManager am = AccountManager.get(context);
        final Account[] accounts = am.getAccountsByType(ACCOUNT_TYPE_GMAIL_GOOGLE);
        //final Account[] accounts = combine(
            //am.getAccountsByType(ACCOUNT_TYPE_GMAIL_GOOGLE),
            //am.getAccountsByType(ACCOUNT_TYPE_GMAIL_LEGACY),
            //am.getAccountsByType(ACCOUNT_TYPE_GMAIL_EXCHANGE));
        final String[] accountNames = new String[accounts.length];
        int i = 0;
        for (Account account : accounts)
        {
            //Log.d(LOG_TAG, "getAllAccountNames " + account.name + "<" + account.type + ">");
            accountNames[i++] = account.name;
        }
        //for (Account account : am.getAccounts())
        //{
            //Log.d(LOG_TAG, "getAllAccountNames all " + account.name + "<" + account.type + ">");
        //}
        return accountNames;
    }

    public static int getUnreadCount(Context context)
    {
        int count = 0;
        
        for (String account : getAllAccountNames(context))
        {
            count += getUnreadCount(context, account);
        }
    
        return count;
    }
    
    public static int getUnreadCount(Context context, String account)
    {
        //Log.d(LOG_TAG, "getUnreadCount " + account);
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(getLabelsUri(account),
                GMAIL_PROJECTION,
                null, null,
                null);

        if (cursor == null || cursor.isAfterLast()) {
            //Log.d(LOG_TAG, "No Gmail inbox information found for account.");
            if (cursor != null) {
                cursor.close();
            }
            return 0;
        }

        int count = 0;

        while (cursor.moveToNext())
        {
            String label = cursor.getString(cursor.getColumnIndex(CANONICAL_NAME));
            //Log.d(LOG_TAG, "cursor " + label);
            if (CANONICAL_NAME_INBOX.equals(label) || CANONICAL_NAME_INBOX_CATEGORY_PRIMARY.equals(label))
            {
                count = cursor.getInt(cursor.getColumnIndex(NUM_UNREAD_CONVERSATIONS));
            }
        }

        cursor.close();

        return count;
    }
}
