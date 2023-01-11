package w2.g16.odds.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserEmail {

    static final String email = null;

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setEmail(Context ctx, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(email, value);
        editor.commit();
    }

    public static String getEmail(Context ctx) {
        return getSharedPreferences(ctx).getString(email, "");
    }

}
