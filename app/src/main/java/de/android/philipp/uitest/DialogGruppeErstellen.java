package de.android.philipp.uitest;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Philipp on 10.01.2016.
 */
public class DialogGruppeErstellen extends Dialog {

    public DialogGruppeErstellen(Context context)
    {
        super(context);
    }

    public void init()
    {//
        findViewById(R.id.btnDialogCreateGroup).setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {
            switch (v.getId())
            {
                case R.id.btnDialogCreateGroup:
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
}
