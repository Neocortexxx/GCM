package de.android.philipp.uitest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class GroupSearchActivity extends Activity
{
    ActionBar actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupsearch);

        new GetAllGroupFromServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        ((EditText)findViewById(R.id.groupSearchFilter)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new GetAllGroupFromServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public  class GetAllGroupFromServerTask extends AsyncTask<String, Void, String> {
        LinearLayout alleEintraege;
        protected void onPreExecute() {

        }

        public GetAllGroupFromServerTask() {
            alleEintraege = (LinearLayout)findViewById(R.id.groupsearch_list);
            alleEintraege.removeAllViews();
        }

        protected String doInBackground(String... werte) {
            try {
                String result = "";
                String filter = ((EditText) findViewById(R.id.groupSearchFilter)).getText().toString();

                result = InteractWithServer.GetAllGroupsFromServer(filter);

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

                        GroupSearchItem item = new GroupSearchItem(GroupSearchActivity.this);

                        json_data = jArray.getJSONObject(i);
                        item.setGroupID(json_data.getInt("ID"));
                        item.setGroupName(json_data.getString("groupname"));
//                        item.setGroupAdmin(json_data.getString("Baujahr"));
//                        item.setMemberCount(json_data.getString("Baujahr"));
                        alleEintraege.addView(item);
                    }
                }
            } catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GroupSearchActivity.this, "Serverantwort fehlerhaft!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }

    }
}

