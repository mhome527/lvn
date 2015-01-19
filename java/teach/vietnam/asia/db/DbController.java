package teach.vietnam.asia.db;

import teach.vietnam.asia.entity.DaoMaster;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by huynhtran on 7/8/14.
 */
public class DbController {
    public static SQLiteDatabase init(Context context, boolean writable) {
        SQLiteDatabase database;
        DaoMaster.OpenHelper mOpenHelper;

        mOpenHelper = new DBOpenHelper(context);
        if (writable) {
            database = mOpenHelper.getWritableDatabase();
        } else {
            database = mOpenHelper.getReadableDatabase();
        }
        return database;
    }
}
