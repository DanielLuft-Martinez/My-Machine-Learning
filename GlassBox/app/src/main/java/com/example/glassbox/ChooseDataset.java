package com.example.glassbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Bundle;


public class ChooseDataset extends AppCompatActivity
        implements PhotosFragment.OnFragmentInteractionListener,
        TrainingFragment.OnFragmentInteractionListener,
        ResultsFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_dataset);

        // Load the first fragment in the training process into the container
        loadFragment(new PhotosFragment());
    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayoutContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}