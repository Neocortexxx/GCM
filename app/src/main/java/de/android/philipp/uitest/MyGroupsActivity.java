package de.android.philipp.uitest;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyGroupsActivity extends Activity
{

    public static final String REG_ID = "regId";

    DialogGruppeErstellen _dialogGruppeErstellen;
    static final String TAG = "MyGroupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mygroups);

        new GetMyGroupsFromServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        findViewById(R.id.btnCreateGroup).setOnClickListener(onClickListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {
            switch (v.getId())
            {
                case R.id.btnCreateGroup:
                    LadeDialogGruppeErstellen();
                    break;

                case R.id.btnEinladen:
                    //new InGruppeEinladenTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    break;
            }
        }
    };

    private Dialog.OnDismissListener onDismissListener = new Dialog.OnDismissListener()
    {
        @Override
        public void onDismiss(DialogInterface dialog) {
            new GetMyGroupsFromServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private String getRegistrationId() {
        final SharedPreferences prefs = getSharedPreferences(
                MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        return registrationId;
    }

    public void LadeDialogGruppeErstellen()
    {
        if (_dialogGruppeErstellen == null)
        {
            _dialogGruppeErstellen = new DialogGruppeErstellen(this);
            _dialogGruppeErstellen.setOnDismissListener(onDismissListener);
            Helfer.LadeDialog(_dialogGruppeErstellen, R.layout.dialog_creategroup, true, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, this);
            _dialogGruppeErstellen.init();
        }
        _dialogGruppeErstellen.show();
    }

    public class GetMyGroupsFromServerTask extends AsyncTask<String, Void, String> {
        LinearLayout alleEintraege;
        protected void onPreExecute() {

        }

        public GetMyGroupsFromServerTask() {
            alleEintraege = (LinearLayout)findViewById(R.id.groupsearch_list);
            alleEintraege.removeAllViews();
        }

        protected String doInBackground(String... werte) {
            try {
                String result = "";

                result = InteractWithServer.GetMyGroupsFromServer(Helfer.getRegistrationId(MyGroupsActivity.this));

                return result;
            } catch (final Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {

            try {
                if(!result.equals("null\n")) {
                    JSONArray jArray = new JSONArray(result);
                    JSONObject json_data = null;
                    for (int i = 0; i < jArray.length(); i++) {

                        GroupSearchItem item = new GroupSearchItem(MyGroupsActivity.this);

                        json_data = jArray.getJSONObject(i);
                        item.setGroupID(json_data.getInt("ID"));
                        item.setGroupName(json_data.getString("groupname"));
                        alleEintraege.addView(item);
                    }
                }
            } catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyGroupsActivity.this, "Serverantwort fehlerhaft!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }

    }

}

