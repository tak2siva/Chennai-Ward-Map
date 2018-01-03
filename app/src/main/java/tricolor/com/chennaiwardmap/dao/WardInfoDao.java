package tricolor.com.chennaiwardmap.dao;

import android.database.Cursor;
import android.support.annotation.Nullable;

import tricolor.com.chennaiwardmap.db.DatabaseHandle;
import tricolor.com.chennaiwardmap.model.WardInfo;

public class WardInfoDao {
    private DatabaseHandle handle;

    public WardInfoDao(DatabaseHandle handle) {
        this.handle = handle;
    }

    public WardInfo getWardInfo(String zone_no) {
        Cursor cursor;
        WardInfo wardInfo = new WardInfo();
        try {
            handle.open();
            cursor = handle.getDatabase().query(WardInfo.TABLE_NAME,
                    WardInfo.ALL_COLUMNS,
                    WardInfo.COLUMN_ZONE_NO + " = ?",
                    new String[] { zone_no },
                    null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                wardInfo = new WardInfo(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            }
        } catch (Exception e) {
            System.out.println("Error fetching ward info");
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            handle.close();
        }

        return wardInfo;
    }
}
