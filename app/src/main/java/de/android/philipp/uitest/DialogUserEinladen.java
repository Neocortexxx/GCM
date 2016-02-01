package de.android.philipp.uitest;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogUserEinladen extends Dialog {

    HashMap<Integer,String> groupMap;
    List<String> groupList;

    public DialogUserEinladen(Context context)
    {
        super(context);
    }

    public void init()
    {//
        findViewById(R.id.btnDialogInviteUser).setOnClickListener(onClickListener);
        new MyGroupsToListServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //Spinner s = (Spinner) findViewById(R.id.spinnMyGroups);
        //ArrayAdapter<HashMap<Integer, String>> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item);
        //adapter.add(groupMap);
        //s.setAdapter(adapter);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {
            switch (v.getId())
            {
                case R.id.btnDialogInviteUser:
                    new GruppeErstellenTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    break;
            }
        }
    };

    public  class GruppeErstellenTask extends AsyncTask<String, Void, String>
    {
        protected void onPreExecute()
        {
        }

        public GruppeErstellenTask()
        {
        }

        protected String doInBackground(String... werte)
        {
            try
            {
                String gruppenName = ((EditText)findViewById(R.id.txtDialogGroupName)).getText().toString();
                String result = InteractWithServer.GruppeErstellen(gruppenName, Helfer.getUsername(getContext()));
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

            dismiss();
        }

    }

    public class MyGroupsToListServerTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {

        }

        public MyGroupsToListServerTask() {
            groupMap = new HashMap<Integer,String>();
            groupMap.clear();

            groupList = new ArrayList<>();
        }

        protected String doInBackground(String... werte) {
            try {
                String result = "";

                result = InteractWithServer.GetMyGroupsFromServer(Helfer.getRegistrationId(getContext()));

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

                        json_data = jArray.getJSONObject(i);
                        groupList.add(json_data.getString("groupname"));
                        groupMap.put(json_data.getInt("ID"), json_data.getString("groupname"));
                    }

                    Spinner s = (Spinner) findViewById(R.id.spinnMyGroups);


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, groupList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s.setAdapter(adapter);

                    s.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    });
                }
            } catch (final Exception e) {
                if(!result.equals("success"))
                    return;
            }

        }

    }
}
