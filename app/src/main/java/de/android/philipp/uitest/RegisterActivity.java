package de.android.philipp.uitest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class RegisterActivity extends Activity {

    Button btnRegister;
    Button btnLogin;
    GoogleCloudMessaging gcm;
    Context context;
    String regId;

    public static final String REG_ID = "regId";
    public static final String REG_USERNAME = "username";
    private static final String APP_VERSION = "appVersion";

    static final String TAG = "Register Activity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(!getRegistrationId(this).equals("") && !getUsername().equals(""))
        {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);

            startActivity(i);
            finish();
        }

        context = getApplicationContext();

        if (TextUtils.isEmpty(regId)) {
            regId = registerGCM();
            Log.d("RegisterActivity", "GCM RegId: " + regId);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Already Registered with GCM Server!",
                    Toast.LENGTH_LONG).show();
        }

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new RegisterOnServer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new LoginOnServer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();

            Log.d("RegisterActivity",
                    "registerGCM - successfully registered with GCM server - regId: "
                            + regId);
        } else {
            Toast.makeText(getApplicationContext(),
                    "RegId already available. RegId: " + regId,
                    Toast.LENGTH_LONG).show();
        }
        return regId;
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private String getUsername() {
        final SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String regUsername = prefs.getString(REG_USERNAME, "");
        if (regUsername.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        return regUsername;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            Log.d("RegisterActivity",
                    "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    public  class RegisterOnServer extends AsyncTask<String, Void, String>  {
        protected void onPreExecute()
        {

        }

        public RegisterOnServer()
        {
        }

        protected String doInBackground(String... werte)
        {
            try
            {
                String result="";
                String username = ((EditText)findViewById(R.id.txtRegisterName)).getText().toString();
                String password = ((EditText)findViewById(R.id.txtRegisterPassword)).getText().toString();
                if(!username.equals("") && !password.equals("")){
                    if(InteractWithServer.CheckUserName(username))
                    {
                        result = InteractWithServer.SaveUserDataOnServer(regId, username, password);
                        StoreUsername(RegisterActivity.this);
                    }
                    else
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "Der Username ist schon vergeben!",Toast.LENGTH_LONG).show();
                            }
                        });
                }
                else
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Username oder Passwort ist leer",Toast.LENGTH_LONG).show();
                        }
                    });

                return result;
            }
            catch (final Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result)
        {
            if(!result.equals("success"))
                return;

            Intent i = new Intent(getApplicationContext(),
                    MainActivity.class);
            i.putExtra("regId", regId);
            Log.d("RegisterActivity",
                    "onClick of Share: Before starting main activity.");
            startActivity(i);
            finish();
            Log.d("RegisterActivity", "onClick of Share: After finish.");
        }
    }

    public  class LoginOnServer extends AsyncTask<String, Void, String> {
        protected void onPreExecute()
        {

        }

        public LoginOnServer()
        {
        }

        protected String doInBackground(String... werte)
        {
            try
            {
                String result="";
                String username = ((EditText)findViewById(R.id.txtLoginName)).getText().toString();
                String password = ((EditText)findViewById(R.id.txtLoginPassword)).getText().toString();
                if(!username.equals("") && !password.equals("")){
                    if(InteractWithServer.CheckLogin(username, password))
                    {
                        result = "success";
                        StoreUsername(RegisterActivity.this);
                        InteractWithServer.CheckRegistration(username, regId);
                    }
                    else
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RegisterActivity.this, "Der eingebene Username oder das Passwort sind nicht korrekt!",Toast.LENGTH_LONG).show();
                            }
                        });
                }
                else
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Username oder Passwort ist leer",Toast.LENGTH_LONG).show();
                        }
                    });
                return result;
            }
            catch (final Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result)
        {
            if(!result.equals("success"))
                return;

            Intent i = new Intent(getApplicationContext(),
                    MainActivity.class);
            i.putExtra("regId", regId);
            Log.d("RegisterActivity",
                    "onClick of Share: Before starting main activity.");
            startActivity(i);
            finish();
            Log.d("RegisterActivity", "onClick of Share: After finish.");
        }

    }

    private void registerInBackground() {
        new RegisterInBackgroundTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public  class RegisterInBackgroundTask extends AsyncTask<String, Void, String> {
        protected void onPreExecute()
        {

        }

        public RegisterInBackgroundTask()
        {
        }

        protected String doInBackground(String... werte)
        {
            try
            {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(Config.GPID);
                    Log.d("RegisterActivity", "registerInBackground - regId: "
                            + regId);
                    msg = "Device registered, registration ID=" + regId;

                    StoreRegistrationId(context, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterActivity", "Error: " + msg);
                }
                Log.d("RegisterActivity", "AsyncTask completed: " + msg);
                return msg;
            }
            catch (final Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result)
        {
            Toast.makeText(getApplicationContext(),
                    "Registered with GCM Server." + result, Toast.LENGTH_LONG)
                    .show();
        }

    }

    private void StoreRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }
    private void StoreUsername(Context context) {
        final SharedPreferences prefs = getSharedPreferences(MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();

        String username = ((EditText)findViewById(R.id.txtRegisterName)).getText().toString();
        editor.putString(REG_USERNAME, username);
        editor.commit();
    }
}