package de.android.philipp.uitest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserSearchActivity extends Activity
{
    ActionBar actionbar;
    DialogUserEinladen _dialogUserEinladen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usersearch);

        new GetAllUsersFromServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        ((EditText)findViewById(R.id.userSearchFilter)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new GetAllUsersFromServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {
            if (_dialogUserEinladen == null)
            {
                _dialogUserEinladen = new DialogUserEinladen(UserSearchActivity.this);
                    Helfer.LadeDialog(_dialogUserEinladen, R.layout.dialog_inviteuser, true, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, UserSearchActivity.this);
                _dialogUserEinladen.init(((UserSearchItem)v).getUserID(), Helfer.getRegistrationId(UserSearchActivity.this));
            }
            _dialogUserEinladen.show();
        }
    };

    public  class GetAllUsersFromServerTask extends AsyncTask<String, Void, String> {
        LinearLayout alleEintraege;
        protected void onPreExecute() {

        }

        public GetAllUsersFromServerTask() {
            alleEintraege = (LinearLayout)findViewById(R.id.usersearch_list);
            alleEintraege.removeAllViews();
        }

        protected String doInBackground(String... werte) {
            try {
                String result = "";
                String filter = ((EditText) findViewById(R.id.userSearchFilter)).getText().toString();

                result = InteractWithServer.GetAllUsersFromServer(filter);

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

                        UserSearchItem item = new UserSearchItem(UserSearchActivity.this);
                        item.setOnClickListener(onClickListener);
                        json_data = jArray.getJSONObject(i);
                        item.setUserID(json_data.getInt("ID"));
                        item.setUsername(json_data.getString("username"));//
                        alleEintraege.addView(item);
                    }
                }
            } catch (final Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UserSearchActivity.this, "Serverantwort fehlerhaft!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }

    }
}

