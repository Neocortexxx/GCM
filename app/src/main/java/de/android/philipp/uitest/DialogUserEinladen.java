package de.android.philipp.uitest;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
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

    HashMap<Integer,String> _groupMap;
    List<String> _groupList;
    String _selectedInviteGroup;
    int _selectedInviteGroupID;

    public DialogUserEinladen(Context context)
    {
        super(context);
    }

    public void init()
    {
        findViewById(R.id.btnDialogInviteUser).setOnClickListener(onClickListener);
        new MyGroupsToListServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {
            switch (v.getId())
            {
                case R.id.btnDialogInviteUser:
                    if(_selectedInviteGroupID != -1)
                        new InGruppeEinladenTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    break;
            }
        }
    };

    public class MyGroupsToListServerTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            _selectedInviteGroup = "";
            _selectedInviteGroupID = -1;
        }

        public MyGroupsToListServerTask() {
            _groupMap = new HashMap<Integer,String>();
            _groupMap.clear();

            _groupList = new ArrayList<>();
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
                        _groupList.add(json_data.getString("groupname"));
                        _groupMap.put(json_data.getInt("ID"), json_data.getString("groupname"));
                    }

                    Spinner s = (Spinner) findViewById(R.id.spinnMyGroups);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, _groupList);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    s.setAdapter(adapter);
                    s.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            _selectedInviteGroup = parent.getItemAtPosition(position).toString();
                            for(Map.Entry<Integer,String> e : _groupMap.entrySet())
                            {
                                if(e.getValue().equals(_selectedInviteGroup))
                                {
                                    _selectedInviteGroupID = e.getKey();
                                }
                            }
                        }
                    });
                }
            } catch (final Exception e) {
                if(!result.equals("success"))
                    return;
            }

        }

    }

    public  class InGruppeEinladenTask extends AsyncTask<String, Void, String>
    {
        protected void onPreExecute()
        {

        }

        public InGruppeEinladenTask()
        {
        }

        protected String doInBackground(String... werte)
        {
            try
            {
                //TODO Username von angeklickten User holen
                //String result = InteractWithServer.InGruppeEinladen(_selectedInviteGroupID, userName, Helfer.getUsername(getContext()));
                //return result;
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
        }

    }
}
