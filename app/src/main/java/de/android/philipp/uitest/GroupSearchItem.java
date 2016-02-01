package de.android.philipp.uitest;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Philipp on 14.12.2015.
 */
public class GroupSearchItem extends RelativeLayout {


    private int _groupID;
    public GroupSearchItem(Context context)
    {
        super(context);
        init();
    }

    public GroupSearchItem(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public GroupSearchItem(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init();
    }

    private void init()
    {
        inflate(getContext(), R.layout.activity_groupsearch_item,
                this);


    }

    public void setGroupName(String text)
    {
        ((TextView) findViewById(R.id.txtGroupCreator)).setText(text);
    }

    public void setMemberCount(String text)
    {
        ((TextView) findViewById(R.id.txtGroupCreator)).setText(text);
    }

    public void setGroupAdmin(String text)
    {
        ((TextView) findViewById(R.id.txtGroupCreator)).setText(text);
    }



    public int getGroupID() {
        return _groupID;
    }

    public void setGroupID(int _groupID) {
        this._groupID = _groupID;
    }

}