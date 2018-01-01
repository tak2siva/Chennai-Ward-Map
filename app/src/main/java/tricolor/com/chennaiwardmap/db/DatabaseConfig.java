package tricolor.com.chennaiwardmap.db;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseConfig extends SQLiteAssetHelper {
    public static final String DATABASE_NAME = "chennai_ward_info.db";
    public static final int DATABASE_VERSION = 1;

    public DatabaseConfig(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
