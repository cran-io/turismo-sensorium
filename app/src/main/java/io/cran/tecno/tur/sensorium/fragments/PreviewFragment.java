package io.cran.tecno.tur.sensorium.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import io.cran.tecno.tur.sensorium.R;

public class PreviewFragment extends Fragment {

    public static final String TAG = "PreviewFragment";
    public static final String PICTURE_FILE = "picture";
    private Listener mListener;
    private File mTakedPicture;

    public PreviewFragment() {
    }

    public static PreviewFragment newInstance(File pictureFile) {
        PreviewFragment f = new PreviewFragment();
        Bundle args = new Bundle();
        args.putSerializable(PICTURE_FILE, pictureFile);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTakedPicture = (File) getArguments().getSerializable(PICTURE_FILE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_preview, container, false);
        ImageView picturePreview = (ImageView) root.findViewById(R.id.picturePreview);
        if (mTakedPicture.exists()) {
            picturePreview.setImageURI(Uri.fromFile(mTakedPicture));
        }

        Button buttonRetry = (Button) root.findViewById(R.id.buttonRetry);
        Button buttonConfirm = (Button) root.findViewById(R.id.buttonConfirm);
        buttonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTakedPicture.deleteOnExit();
                getFragmentManager().popBackStack();
            }
        });

        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Post and wait for success before showing "Thanks" screen
                //TODO: POST: if success, show "thanks" if fail, stay here.
                Toast.makeText(getActivity(), "Subiendo foto...", Toast.LENGTH_SHORT).show();
                mListener.showGoodbye();
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
        void showGoodbye();
    }
}
