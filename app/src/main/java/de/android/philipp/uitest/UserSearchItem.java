package de.android.philipp.uitest;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Philipp on 14.12.2015.
 */
public class UserSearchItem extends RelativeLayout {


    private int _userID;
    public UserSearchItem(Context context)
    {
        super(context);
        init();
    }

    public UserSearchItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public UserSearchItem(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.activity_usersearch_item, this);

    }

    public void setUsername(String text)
    {
        ((TextView) findViewById(R.id.txtUsername)).setText(text);
    }

    public int getUserID() {
        return _userID;
    }

    public void setUserID(int _userID) {
        this._userID = _userID;
    }

}