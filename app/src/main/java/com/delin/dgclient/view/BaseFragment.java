package com.delin.dgclient.view;



import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * Created by cdm on 15/5/1.
 */
public abstract class BaseFragment extends Fragment {
    protected LayoutInflater layoutInflater;
    protected ViewGroup container;
    public FragmentActivity baseContext;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.layoutInflater = inflater;
        this.container = container;
        baseContext = getActivity();
        View view = inflater.inflate(getViewId(), container, false);
        return view;
    }

    public abstract int getViewId();

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }
    public void showShortToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

}


