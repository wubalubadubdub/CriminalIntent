package com.example.rokabr.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rokabr.criminalintent.database.CrimeBaseHelper;
import com.example.rokabr.criminalintent.database.CrimeCursorWrapper;
import com.example.rokabr.criminalintent.database.CrimeDbSchema;
import com.example.rokabr.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

/**
 * Created by rokabr on 1/10/2016.
 */
public class CrimeLab {

    private static CrimeLab sCrimeLab;


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


    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursor = queryCrimes(
                CrimeTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

    // Writes and updates to databases are done with assistance of ContentValues class.
    // It's a key-value store class, like Bundles, but specifically designed to store
    // the kinds of data SQLite can hold.

    private static ContentValues getContentValues(Crime crime) {
        // column names are keys
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTitle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        // _id is automatically created for you as a unique row ID

    return values;

}

    // Reading in data from SQLite is done using the query method. It has a few
    // different overloads. They are arguments of SQL's select statement. We will
    // only use the first four: String table, String[] columns, String where,
    // String[] whereArgs.

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new CrimeCursorWrapper(cursor);
    }

    public void addCrime(Crime c) {
        // return the UUID, title, date, and whether solved values for the Crime
        ContentValues values = getContentValues(c);

        // insert these values into the database. arg1 is the table to insert into.
        // arg3 is the data you want to put in. arg2 is rarely used. it's called
        // nullColumnHack. this allows you to call insert with an empty ContentValues,
        // passing in a UUID for that parameter instead. Then, it would pass in a
        // ContentValues with UUID set to null and allow insertion of a new row.
        mDatabase.insert(CrimeTable.NAME, null, values);

    }

    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);

        // 3rd argument is a "where clause" that specifies which row gets updated
        // 4th argument is whereArgs. if you put ? in where clause, which protects
        // against SQL injection, then it will be replaced by values from whereArgs,
        // bound as Strings.
        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public void deleteCrime(Crime c) {
        String uuidString = c.getId().toString();
        mDatabase.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ?",
                new String[] { uuidString });

    }
}
