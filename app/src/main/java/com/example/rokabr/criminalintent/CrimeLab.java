package com.example.rokabr.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.rokabr.criminalintent.database.CrimeBaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by rokabr on 1/10/2016.
 */
public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
        /* Calling getWritableDatabase() does these things:
        opens /data/data/<package name>/databases/crimeBase.db, creating a new
        database file if it doesn't already exist.

        If this is the first time the database has been created, call onCreate(SQLiteDatabase)
        and save out the latest version number.

        if it's not the first time, check version # of database. if version # in CrimeOpenHelper
        is higher, call onUpgrade(SQLiteDatabase, int, int)
         */

        mCrimes = new ArrayList<>();
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) { // get a Crime from mCrimes by UUID
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }

        return null;
    }

    public void addCrime(Crime c) {
        mCrimes.add(c);
    }

    public void deleteCrime(Crime c) {
        mCrimes.remove(c);
    }
}
