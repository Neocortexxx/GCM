package de.android.philipp.uitest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViewById(R.id.btnNews).setOnClickListener(onClickListener);
        findViewById(R.id.btnProfil).setOnClickListener(onClickListener);
        findViewById(R.id.btnMyGroups).setOnClickListener(onClickListener);
        findViewById(R.id.btnFriends).setOnClickListener(onClickListener);
        findViewById(R.id.btnLogout).setOnClickListener(onClickListener);
        findViewById(R.id.btnGroupSearch).setOnClickListener(onClickListener);
        findViewById(R.id.btnUserSearch).setOnClickListener(onClickListener);
        findViewById(R.id.btnSettings).setOnClickListener(onClickListener);

        String regId = Helfer.getRegistrationId(MainActivity.this);
        Toast.makeText(getApplicationContext(), regId, Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {

            switch (v.getId())
            {
                case R.id.btnLogout:
                    Helfer.Logout(MainActivity.this);
                    ActivityStarten(RegisterActivity.class);
                    break;
                case R.id.btnFriends:
                    ActivityStarten(FriendlistActivity.class);
                    break;
                case R.id.btnGroupSearch:
                    ActivityStarten(GroupSearchActivity.class);
                    break;
                case R.id.btnMyGroups:
                    ActivityStarten(MyGroupsActivity.class);
                    break;
                case R.id.btnNews:
                    ActivityStarten(NewsActivity.class);
                    break;
                case R.id.btnProfil:
                    ActivityStarten(ProfileActivity.class);
                    break;
                case R.id.btnSettings:
                    ActivityStarten(SettingsActivity.class);
                    break;
                case R.id.btnUserSearch:
                    ActivityStarten(UserSearchActivity.class);
                    break;
            }
        }
    };

    private void ActivityStarten(Class klasse)
    {
        Intent i = new Intent(this, klasse);
        startActivity(i);
        this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}