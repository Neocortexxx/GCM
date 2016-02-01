package de.android.philipp.uitest;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class MenuActivity extends Fragment {

    MainActivity _mainActivity;
    View rootView;

    public MenuActivity()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_menu, container, false);

        rootView.findViewById(R.id.btnNews).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.btnProfil).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.btnMyGroups).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.btnFriends).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.btnAGB).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.btnGroupSearch).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.btnUserSearch).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.btnSettings).setOnClickListener(onClickListener);

        return rootView;
    }
    public void setMainActivity(MainActivity _mainActivity)
    {
        this._mainActivity = _mainActivity;
    }
    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(final View v)
        {

            switch (v.getId())
            {
                case R.id.btnAGB:
                    ActivityStarten(CreateActivity.class);
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
        Intent i = new Intent(_mainActivity, klasse);
        startActivity(i);
        _mainActivity.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
