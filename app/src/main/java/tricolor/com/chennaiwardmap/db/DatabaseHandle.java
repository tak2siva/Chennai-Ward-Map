package tricolor.com.chennaiwardmap.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import tricolor.com.chennaiwardmap.WardInfo;


public class DatabaseHandle {
    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase database;

    public DatabaseHandle(Context context) {
        this.sqLiteOpenHelper = new DatabaseConfig(context);
    }

    public void open() {
        this.database = sqLiteOpenHelper.getReadableDatabase();
    }

    public void close() {
        if (this.database != null) {
            database.close();
        }
    }

    public void getAllWardInfo() {
        open();
        List<WardInfo> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM ward_info";

        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            list.add(new WardInfo(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
            cursor.moveToNext();
        }

        cursor.close();
        close();
    }
}
