package teach.vietnam.asia.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.utils.Constant;

/**
 * Created by huynhtran on 7/8/14.
 */
public class DBOpenHelper extends DaoMaster.OpenHelper  {

    public  DBOpenHelper(Context context) {
        super(context, Constant.DB_NAME_V2, null);
//        super(context, Constant.DB_NAME, null);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

}
