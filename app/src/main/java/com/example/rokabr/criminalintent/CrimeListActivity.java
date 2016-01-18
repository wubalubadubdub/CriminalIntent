package com.example.rokabr.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by rokabr on 1/10/2016.
 */
public class CrimeListActivity extends SingleFragmentActivity {



    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
