package io.cran.tecno.tur.sensorium.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import io.cran.tecno.tur.sensorium.R;
import io.cran.tecno.tur.sensorium.net.UploadHandler;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class PreviewFragment extends Fragment {

    public static final String TAG = "PreviewFragment";
    public static final String PICTURE_FILE = "picture";
    private Listener mListener;
    private File mTakedPicture;
    private CheckBox mTOSCheckbox;
    private ProgressBar mProgressBar;
    private Button mButtonConfirm;
    private Button mButtonRetry;

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
        mTOSCheckbox = (CheckBox) root.findViewById(R.id.checkboxTOS);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progress);
        if (mTakedPicture.exists()) {
            Glide.with(this).load(mTakedPicture).into(picturePreview);
        }

        mButtonRetry = (Button) root.findViewById(R.id.buttonRetry);
        mButtonConfirm = (Button) root.findViewById(R.id.buttonConfirm);
        mButtonRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTakedPicture != null && !mTakedPicture.delete()) {
                    mTakedPicture.deleteOnExit();
                    mTakedPicture = null;
                }
                getFragmentManager().popBackStack();
            }
        });

        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTOSCheckbox.isChecked()) {
                    uploadPicture(mTakedPicture);
                } else {
                    Toast.makeText(getActivity(), "Primero acepta los TOS", Toast.LENGTH_SHORT).show();
                }
            }
        });
        updateUI(false);

        return root;
    }

    private void updateUI(boolean isUploading) {
        if (isUploading) {
            mTOSCheckbox.setEnabled(false);
            mProgressBar.setVisibility(View.VISIBLE);
            mButtonRetry.setEnabled(false);
            mButtonConfirm.setEnabled(false);
        } else {
            mTOSCheckbox.setEnabled(true);
            mProgressBar.setVisibility(View.INVISIBLE);
            mButtonRetry.setEnabled(true);
            mButtonConfirm.setEnabled(true);
        }
    }

    private void uploadPicture(File file) {
        Toast.makeText(getActivity(), "Subiendo foto...", Toast.LENGTH_SHORT).show();
        updateUI(true);
        UploadHandler.getInstance().upload(new TypedFile("image/jpeg", file), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                mListener.showGoodbye();
            }

            @Override
            public void failure(RetrofitError error) {
                String msg = "ERROR";
                if (error != null) {
                    if (error.getKind() == RetrofitError.Kind.NETWORK) {
                        msg = "ERROR: Chequea la conexión a internet";
                    } else if (error.getKind() == RetrofitError.Kind.HTTP) {
                        msg = "ERROR: " + error.getMessage();
                    }
                }
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                updateUI(false);
            }
        });
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
