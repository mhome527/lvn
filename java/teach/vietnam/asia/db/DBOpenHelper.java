package teach.vietnam.asia.db;

import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.utils.Constant;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by huynhtran on 7/8/14.
 */
public class DBOpenHelper extends DaoMaster.OpenHelper  {

    public  DBOpenHelper(Context context) {
        super(context, Constant.DB_NAME, null);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

}
