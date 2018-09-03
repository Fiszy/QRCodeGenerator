package com.fiszy.qrcodescanner;

import android.provider.BaseColumns;

public final class RecordContract {

        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public RecordContract() {}

        /* Inner class that defines the table contents */
        public static abstract class FeedEntry implements BaseColumns {
            public static final String TABLE_NAME = "record";
            //public static final String _ID = "id";
            public static final String COLUMN_NAME_TEXT = "text";
            public static final String COLUMN_NAME_DATE = "date";
           // public static final String COLUMN_NAME_TIME = "time";

        }
    }
