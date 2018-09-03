package com.fiszy.qrcodescanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class RecordDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RecordContract.FeedEntry.TABLE_NAME + " (" +
                    RecordContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +

                    RecordContract.FeedEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_SEP +
                    RecordContract.FeedEntry.COLUMN_NAME_DATE + TEXT_TYPE +
                    " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RecordContract.FeedEntry.TABLE_NAME;

    public RecordDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "FeedReaderDbHelper: db created ");
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "EEE, d MMM yyyy h:mm a", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void addRecord( String text,  SQLiteDatabase database){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        values.put(RecordContract.FeedEntry.COLUMN_NAME_TEXT, text);
        values.put(RecordContract.FeedEntry.COLUMN_NAME_DATE, getDateTime());

        long newRowId;
        newRowId = database.insert(
                RecordContract.FeedEntry.TABLE_NAME,
                null,
                values);
        Log.d(TAG, "addContact: added "+ text + " with "+newRowId);

    }

    public Cursor readContact(SQLiteDatabase database)
    {
        String[] projection = {
                RecordContract.FeedEntry._ID,

                RecordContract.FeedEntry.COLUMN_NAME_TEXT,
                RecordContract.FeedEntry.COLUMN_NAME_DATE,

        };

        String sortOrder =
                RecordContract.FeedEntry._ID + " DESC";

        Cursor c = database.query(
                RecordContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        return c;

    }
    //DELETE/REMOVE
    public boolean delete(int id, SQLiteDatabase db)
    {
        try {

//            // Define 'where' part of query.
//            String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
//// Specify arguments in placeholder order.
//            String[] selectionArgs = { String.valueOf(rowId) };
//// Issue SQL statement.
//            db.delete(table_name, selection, selectionArgs);

            int result=db.delete(RecordContract.FeedEntry.TABLE_NAME,RecordContract.FeedEntry._ID+" =?",new String[]{String.valueOf(id)});
            if(result>0) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}