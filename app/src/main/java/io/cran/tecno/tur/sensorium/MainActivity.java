package io.cran.tecno.tur.sensorium;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.WindowManager;

import java.io.File;

import io.cran.tecno.tur.sensorium.fragments.CameraFragment;
import io.cran.tecno.tur.sensorium.fragments.GoodbyeFragment;
import io.cran.tecno.tur.sensorium.fragments.PreviewFragment;
import io.cran.tecno.tur.sensorium.fragments.WelcomeFragment;

public class MainActivity extends Activity implements WelcomeFragment.Listener, CameraFragment.Listener, PreviewFragment.Listener {

    private Fragment mCamFragment;
    private Fragment mWelcomeFragment;
    private Fragment mGoodbyeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        showWelcomeFragment();
    }

    private void showWelcomeFragment() {
        FragmentManager fm = getFragmentManager();
        mWelcomeFragment = fm.findFragmentByTag(WelcomeFragment.TAG);
        if (mWelcomeFragment == null) {
            mWelcomeFragment = WelcomeFragment.newInstance();
        }
        fm.beginTransaction().replace(R.id.container, mWelcomeFragment, WelcomeFragment.TAG)
                .commit();
    }

    private void showCameraFragment() {
        FragmentManager fm = getFragmentManager();
        mCamFragment = fm.findFragmentByTag(CameraFragment.TAG);
        if (mCamFragment == null) {
            mCamFragment = CameraFragment.newInstance();
        }
        fm.beginTransaction().replace(R.id.container, mCamFragment, CameraFragment.TAG)
                .addToBackStack(CameraFragment.TAG)
                .commit();
    }

    private void showPreviewFragment(File picture) {
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.container, PreviewFragment.newInstance(picture), PreviewFragment.TAG)
                .addToBackStack(PreviewFragment.TAG)
                .commit();
    }

    private void showGoodbyeFragment() {
        FragmentManager fm = getFragmentManager();
        mGoodbyeFragment = fm.findFragmentByTag(GoodbyeFragment.TAG);
        if (mGoodbyeFragment == null) {
            mGoodbyeFragment = GoodbyeFragment.newInstance();
        }
        fm.beginTransaction().replace(R.id.container, mGoodbyeFragment, GoodbyeFragment.TAG)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mGoodbyeFragment != null && mGoodbyeFragment.isVisible()) {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().remove(mGoodbyeFragment).commit();
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void openCamera() {
        showCameraFragment();
    }

    @Override
    public void openPicture(File picture) {
        showPreviewFragment(picture);
    }

    @Override
    public void showGoodbye() {
        showGoodbyeFragment();
    }
}
