package ke.co.coansinternational.mydiary.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static ke.co.coansinternational.mydiary.data.ContractClass.CONTENT_AUTHORITY;
import static ke.co.coansinternational.mydiary.data.ContractClass.NotesEntry.CONTENT_ITEM_TYPE;
import static ke.co.coansinternational.mydiary.data.ContractClass.NotesEntry.CONTENT_LIST_TYPE;
import static ke.co.coansinternational.mydiary.data.ContractClass.NotesEntry.TABLE_NAME;
import static ke.co.coansinternational.mydiary.data.ContractClass.NotesEntry._ID;
import static ke.co.coansinternational.mydiary.data.ContractClass.PATH_NOTES;

public class NoteProvider extends ContentProvider {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = NoteProvider.class.getSimpleName();

    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_NOTES, NOTES);

        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_NOTES + "/#", NOTES_ID);

    }

    int rowsDeleted;
    long id;
    private NotesDB mnotesDB;

    @Override
    public boolean onCreate() {
        mnotesDB = new NotesDB(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[]
            strings1, @Nullable String s1) {

        // Get readable database
        SQLiteDatabase database = mnotesDB.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES:
                // For the NEARMISS code, query the NEARMISS table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the NEARMISS table.
                // Perform database query on Nearmiss table
                cursor = database.query(TABLE_NAME, strings,
                        s, strings1, null, null, s1);
                break;
            case NOTES_ID:
                // For the NEARMISS_ID code, extract out the ID from the URI.
                // For an example URI such as "content://com.example.android.NEARMISS/NEARMISS/3",
                // the selection will be "_id=?" and the selection argument will be a
                // String array containing the actual ID of 3 in this case.
                //
                // For every "?" in the selection, we need to have an element in the selection
                // arguments that will fill in the "?". Since we have 1 question mark in the
                // selection, we have 1 String in the selection arguments' String array.
                s = _ID + "=?";
                strings1 = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the NEARMISS table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(TABLE_NAME, strings, s, strings1,
                        null, null, s1);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }
        //noinspection ConstantConditions
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES:
                return CONTENT_LIST_TYPE;
            case NOTES_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
//get writable database
        SQLiteDatabase database = mnotesDB.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES:
                id = database.insert(TABLE_NAME, null, values);
                // If the ID is -1, then the insertion failed. Log an error and return null.
                if (id == -1) {
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                    return null;
                }
                break;
        }

        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        // Get writeable database
        SQLiteDatabase database = mnotesDB.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES:
                // Delete all rows that match the selection and selection args
                //                /Delete all rows that match the selection and selection args
                // For  case NEARMISS:
                rowsDeleted = database.delete(TABLE_NAME, s, strings);
            case NOTES_ID:
                // Delete a single row given by the ID in the URI
                s = _ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                // For case PET_ID:
                // Delete a single row given by the ID in the URI
                rowsDeleted = database.delete(TABLE_NAME, s, strings);
                if (rowsDeleted != 0) {
                    //noinspection ConstantConditions
                    getContext().getContentResolver().notifyChange(uri, null);

                    return rowsDeleted;
                }
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String s,
                      @Nullable String[] strings) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case NOTES:
                return updateData(uri, values, s, strings);
            case NOTES_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                s = _ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateData(uri, values, s, strings);

            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateData(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {
            return 0;
        }
        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mnotesDB.getWritableDatabase();
        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Returns the number of database rows affected by the update statement
        // Return the number of rows updated
        return rowsUpdated;

    }

}

