package io.cran.tecno.tur.sensorium.fragments;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.cran.tecno.tur.sensorium.R;

public class GoodbyeFragment extends Fragment {

    public static final String TAG = "GoodbyeFragment";

    public GoodbyeFragment() {
    }

    public static GoodbyeFragment newInstance() {
        return new GoodbyeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_goodbye, container, false);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getActivity().getFragmentManager().beginTransaction().remove(GoodbyeFragment.this).commit();
            }
        });
        return root;
    }

}
