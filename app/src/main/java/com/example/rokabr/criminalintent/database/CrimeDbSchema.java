package com.example.rokabr.criminalintent.database;

/**
 * Created by bearg on 4/16/2016.
 */
public class CrimeDbSchema {
    public static final class CrimeTable {

        // Begin database schema definition
        public static final String NAME = "crimes"; // table names

        public static final class Cols { // define the columns in the table
            public static final String UUID = "uuid";
            public static final String TITLE = "title"; // refer to as CrimeTable.Cols.TITLE
            public static final String DATE = "date";
            public static final String SOLVED = "solved";

        } // End db schema definition
    }

}
