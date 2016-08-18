package com.shituocheng.calcalculateapplication.com.test;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by shituocheng on 2016/8/18.
 */

public class PageAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> title;
    private Context context;

    public PageAdapter(FragmentManager fm, ArrayList<String> title, Context context) {
        super(fm);
        this.title = title;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return BlankFragment.newInstance(position+1);
    }

    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}
