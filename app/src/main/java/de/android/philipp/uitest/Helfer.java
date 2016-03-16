package de.android.philipp.uitest;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class Helfer {

    public static final String REG_USERNAME = "username";
    public static final String REG_ID = "regId";

    public static void LadeDialog(Dialog dialog, int layout, boolean cancelable, int width, int height, Context context)
    {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = width;
        lp.height = height;
        dialog.getWindow().setAttributes(lp);

        dialog.setCancelable(cancelable);
    }

    public static void StoreUsername(Context context, String username) {
        final SharedPreferences prefs = context.getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        Log.i("Helfer", "Store username");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_USERNAME, username);
        editor.commit();
    }

    public static void StoreRegistrationID(Context context, String regId) {
        final SharedPreferences prefs = context.getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = Helfer.getAppVersion(context);
        Log.i("Helfer", "Store RegID");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.commit();
    }

    public static String getUsername(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String username = prefs.getString(REG_USERNAME, "");
        if (username.isEmpty()) {
            Log.i("Helfer", "User not found.");
            return "";
        }
        return username;
    }

    public static void Logout(Context context)
    {
        final SharedPreferences prefs = context.getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_USERNAME, "");
        editor.putString(REG_ID, "");
        editor.commit();
    }

    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("Helfer", "Registration not found.");
            return "";
        }
        return registrationId;
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("RegisterActivity",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

}
