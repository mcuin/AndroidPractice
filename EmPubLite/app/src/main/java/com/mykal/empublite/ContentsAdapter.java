package com.mykal.empublite;

import android.app.Activity;
import android.app.Fragment;
import android.support.v13.app.FragmentStatePagerAdapter;

/**
 * Created by mykal on 9/23/14.
 */
public class ContentsAdapter extends FragmentStatePagerAdapter {

    public ContentsAdapter(Activity ctxt) {
        super(ctxt.getFragmentManager());
    }

    @Override
    public Fragment getItem(int arg0) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

}
