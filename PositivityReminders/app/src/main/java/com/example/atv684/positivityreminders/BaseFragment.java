package com.example.atv684.positivityreminders;

import android.support.v4.app.Fragment;


public class BaseFragment extends Fragment {

    public BaseActivity getBaseActivity() {
        if (getActivity() instanceof BaseActivity) {
            return (BaseActivity) getActivity();
        } else {
            return null;
        }
    }

}
