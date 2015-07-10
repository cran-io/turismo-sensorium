package io.cran.tecno.tur.sensorium;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.cran.tecno.tur.sensorium.fragments.GoodbyeFragment;
import io.cran.tecno.tur.sensorium.fragments.PreviewFragment;
import io.cran.tecno.tur.sensorium.fragments.WelcomeFragment;

public class MainActivity extends Activity implements WelcomeFragment.Listener, PreviewFragment.Listener {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Fragment mWelcomeFragment;
    private Fragment mPreviewFragment;
    private Fragment mGoodbyeFragment;

    private File mCurrentPhotoFile;

    private long lastBackPress = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        showWelcomeFragment();
    }

    /**
     * Helper for showing the welcome fragment
     */
    private void showWelcomeFragment() {
        FragmentManager fm = getFragmentManager();
        mWelcomeFragment = fm.findFragmentByTag(WelcomeFragment.TAG);
        if (mWelcomeFragment == null) {
            mWelcomeFragment = WelcomeFragment.newInstance();
        }
        fm.beginTransaction().replace(R.id.container, mWelcomeFragment, WelcomeFragment.TAG)
                .commit();
    }

    /**
     * Helper for showing the picture preview fragment
     *
     * @param picture the picture file
     */
    private void showPreviewFragment(File picture) {
        FragmentManager fm = getFragmentManager();
        mPreviewFragment = PreviewFragment.newInstance(picture);
        fm.beginTransaction().replace(R.id.container, mPreviewFragment, PreviewFragment.TAG)
                .addToBackStack(PreviewFragment.TAG)
                .commit();
    }

    /**
     * Helper for showing the goodbye fragment
     */
    private void showGoodbyeFragment() {
        FragmentManager fm = getFragmentManager();
        mGoodbyeFragment = fm.findFragmentByTag(GoodbyeFragment.TAG);
        if (mGoodbyeFragment == null) {
            mGoodbyeFragment = GoodbyeFragment.newInstance();
        }
        fm.beginTransaction().replace(R.id.container, mGoodbyeFragment, GoodbyeFragment.TAG)
                .commit();
    }

    /**
     * Creates a temp file for storing the picture
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "SENSORIUM_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        return image;
    }


    /**
     * Launches the intent for taking the photo.
     * It will do nothing if the device does not have apps that cant handle the intent
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                mCurrentPhotoFile = photoFile;
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Helper for a secure delete of the file.
     */
    private void scheduleDeleteFile() {
        if (mCurrentPhotoFile != null && !mCurrentPhotoFile.delete()) {
            mCurrentPhotoFile.deleteOnExit();
            mCurrentPhotoFile = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            showPreviewFragment(mCurrentPhotoFile);
        } else {
            scheduleDeleteFile();
        }
    }

    @Override
    public void onBackPressed() {
        if (mGoodbyeFragment != null && mGoodbyeFragment.isVisible()) {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().remove(mGoodbyeFragment).commit();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else if (mPreviewFragment != null && mPreviewFragment.isVisible()) {
            scheduleDeleteFile();
            super.onBackPressed();
        } else {
            if (System.currentTimeMillis() - lastBackPress <= 200) {
                super.onBackPressed();
            }
            lastBackPress = System.currentTimeMillis();
        }
    }

    @Override
    public void openCamera() {
        dispatchTakePictureIntent();
    }


    @Override
    public void showGoodbye() {
        showGoodbyeFragment();
    }
}
