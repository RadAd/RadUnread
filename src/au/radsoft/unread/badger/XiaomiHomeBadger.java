package au.radsoft.unread.badger;

import android.content.Context;
import android.content.Intent;

import au.radsoft.unread.ShortcutBadger;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author leolin
 */
public class XiaomiHomeBadger implements ShortcutBadger.Impl {
    public static final String INTENT_ACTION = "android.intent.action.APPLICATION_MESSAGE_UPDATE";
    public static final String EXTRA_UPDATE_APP_COMPONENT_NAME = "android.intent.extra.update_application_component_name";
    public static final String EXTRA_UPDATE_APP_MSG_TEXT = "android.intent.extra.update_application_message_text";

    private Context mContext;

    public XiaomiHomeBadger(Context context) {
        mContext = context;
    }

    @Override
    public void executeBadge(String packageName, String entryActivityName, int badgeCount) {
        try {
            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
            Object miuiNotification = miuiNotificationClass.newInstance();
            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
            field.setAccessible(true);
            field.set(miuiNotification, String.valueOf(badgeCount == 0 ? "" : badgeCount));
        } catch (Exception e) {
            Intent localIntent = new Intent(INTENT_ACTION);
            localIntent.putExtra(EXTRA_UPDATE_APP_COMPONENT_NAME, packageName + "/" + entryActivityName);
            localIntent.putExtra(EXTRA_UPDATE_APP_MSG_TEXT, String.valueOf(badgeCount == 0 ? "" : badgeCount));
            mContext.sendBroadcast(localIntent);
        }
    }

    public static void register(Map<String, Class<? extends ShortcutBadger.Impl>> r)
    {
        r.put("com.miui.miuilite", XiaomiHomeBadger.class);
        r.put("com.miui.home", XiaomiHomeBadger.class);
        r.put("com.miui.miuihome", XiaomiHomeBadger.class);
        r.put("com.miui.miuihome2", XiaomiHomeBadger.class);
        r.put("com.miui.mihome", XiaomiHomeBadger.class);
        r.put("com.miui.mihome2", XiaomiHomeBadger.class);
    }
}
