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

        regId = "";

        if(!Helfer.getRegistrationId(this).equals("") && !Helfer.getUsername(this).equals(""))
        {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);

            startActivity(i);
            finish();
        }

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                regId = registerGCM();
                new RegisterOnServer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                regId = registerGCM();
                new LoginOnServer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }

    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);
        //regId = Helfer.getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();

            Log.d("RegisterActivity", "registerGCM - successfully registered with GCM server - regId: " + regId);
        } else {
            Toast.makeText(getApplicationContext(),
                    "RegId already available",
                    Toast.LENGTH_SHORT).show();
        }
        return regId;
    }

    public  class RegisterOnServer extends AsyncTask<String, Void, String>  {

        String username;
        String password;
        protected void onPreExecute()
        {
            username = ((EditText)findViewById(R.id.txtRegisterName)).getText().toString();
            password = ((EditText)findViewById(R.id.txtRegisterPassword)).getText().toString();
        }
        public RegisterOnServer()
        {
        }
        protected String doInBackground(String... werte)
        {
            try
            {
                String result="";

                if(!username.equals("") && !password.equals("")){
                    if(InteractWithServer.CheckUserName(username))
                    {
                        result = InteractWithServer.SaveUserDataOnServer(regId, username, password);
                        Helfer.StoreUsername(RegisterActivity.this, username);
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
                        Helfer.StoreUsername(RegisterActivity.this, username);
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
                    msg = "Device wurde registriert";

                    Helfer.StoreRegistrationID(RegisterActivity.this, regId);
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

    private void registerInBackground() {
        new RegisterInBackgroundTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}