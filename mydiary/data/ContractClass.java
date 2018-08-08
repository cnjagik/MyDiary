package ke.co.coansinternational.mydiary.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ContractClass {
    static final String CONTENT_AUTHORITY;
    static final String PATH_NOTES = "NoteProvider/MyNotes";
    static {
        CONTENT_AUTHORITY = "ke.co.coansinternational.mydiary";
    }

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public ContractClass() {
    }

    public static final class NotesEntry implements BaseColumns {
        /**
         * The content URI to access the nearmiss data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTES);
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_DATE = "Date";
        public static final String COLUMN_NOTE = "Notes";
        public static final String COLUMN_TITLE = "Titles";
        static final String TABLE_NAME = "MyNotes";
        static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;
        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;


    }
}
