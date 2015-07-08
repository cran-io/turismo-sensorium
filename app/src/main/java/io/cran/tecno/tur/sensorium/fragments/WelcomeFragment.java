package io.cran.tecno.tur.sensorium.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.cran.tecno.tur.sensorium.R;

public class WelcomeFragment extends Fragment {

    public static final String TAG = "WelcomeFragment";
    private Listener mListener;

    public WelcomeFragment() {
    }

    public static WelcomeFragment newInstance(){
        return new WelcomeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_welcome, container, false);
        Button buttonCamera = (Button) root.findViewById(R.id.buttonCamera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openCamera();
            }
        });
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = (Listener) activity;
    }


    public interface Listener {
        void openCamera();
    }
}
