import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by mykal on 2/19/15.
 */
public final class FeedReaderContract {

    public FeedReaderContract() {}

    public static abstract class FeedEntry implements BaseColumns {

        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";
        private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + FeedEntry.TABLE_NAME +
                " (" + FeedEntry._ID + " INTEGER PRIMARY KEY," + FeedEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE +
                COMMA_SEP + FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP + " )";

        private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    }

    public class FeedReaderDbHelper extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "FeedReader.db";


        public FeedReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getContext());
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(FeedEntry.COLUMN_NAME_ENTRY_ID, id);
            contentValues.put(FeedEntry.COLUMN_NAME_TITLE, title);
            contentValues.put(FeedEntry.COLUMN_NAME_CONTENT, content);

            long newRowIdl;
            newRowIdl = db.insert(FeedEntry.TABLE_NAME, FeedEntry.COLUMN_NAME_NULLABLE, contentValues);

            SQLiteDatabase db2 = mDbHelper.getReadableDatabase();
            String[] projections = {FeedEntry._ID, FeedEntry.COLUMN_NAME_TITLE, FeedEntry.COLUMN_NAME_UPDATED};
            String sortOrder = FeedEntry.COLUMN_NAME_UPDATED + " DESC";

            Cursor c = db.query(FeedEntry.TABLE_NAME, projections, selection, selectionArgs, null,
                    null, sortOrder);

            c.moveToFirst();
            long itemId = c.getLong(c.getColumnIndexOrThrow(FeedEntry._ID));

            String selection = FeedEntry.COLUMN_NAME_ENTRY_ID + " Like ? ";
            String selectionArgs = {String.valueOf(rowId)};

            db.delete(table_name, selection, selectionArgs);

            int count = db.update(FeedReaderDbHelper.FeedEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}
