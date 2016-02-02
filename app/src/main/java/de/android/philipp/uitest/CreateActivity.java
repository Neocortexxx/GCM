package de.android.philipp.uitest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class CreateActivity extends Activity
{
    String regId;
    public static final String REG_USERNAME = "username";
    static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        regId = getIntent().getStringExtra("regId");
        Log.d("CreateActivity", "regId: " + regId);

        findViewById(R.id.btnErzeugen).setOnClickListener(onClickListener);
        findViewById(R.id.btnEinladen).setOnClickListener(onClickListener);

    }

    @Override
    protected void onResume()
    {
        super.onResume();


    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        setIntent(intent);

        Bundle bundle = intent.getExtras();
        String gruppenname = bundle.getString("Groupname");
        if (bundle.getString("AcceptGroupInvite").equals("1"))
        {
            String groupname = bundle.getString("groupname");
            String a = "";
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {
            switch (v.getId())
            {
                case R.id.btnErzeugen:
                    //new GruppeErstellenTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    break;

                case R.id.btnEinladen:
                    //new InGruppeEinladenTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    break;
            }
        }
    };




}

