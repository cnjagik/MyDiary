package ke.co.coansinternational.mydiary.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ke.co.coansinternational.mydiary.data.ContractClass.NotesEntry;

import static ke.co.coansinternational.mydiary.data.ContractClass.NotesEntry.TABLE_NAME;

public class NotesDB extends SQLiteOpenHelper {

    public static final String LOG_TAG = NotesDB.class.getSimpleName();

    private static final String DATABASE_NAME = "Notes.db";
    private static final int DATABASE_VERSION = 1;

    public NotesDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_DATABASE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + NotesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NotesEntry.COLUMN_DATE + " TEXT, "
                + NotesEntry.COLUMN_TITLE + " TEXT, "
                + NotesEntry.COLUMN_NOTE + " TEXT NOT NULL);";


        sqLiteDatabase.execSQL(SQL_DATABASE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<GetAllData> getallUserData() {
        List<GetAllData> getAllData = new ArrayList<GetAllData>();
        String selectquery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectquery, null);
        if (c.moveToFirst()) {
            do {
                GetAllData g = new GetAllData();
                g.setId(c.getInt(0));
                g.setNDate(c.getString(1));
                g.setNTitle(c.getString(2));
                g.setNDetails(c.getString(3));
                getAllData.add(g);
            } while (c.moveToNext());
        }
        return getAllData;
    }

}
