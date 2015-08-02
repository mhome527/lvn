package teach.vietnam.asia.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;

public class SqlLiteCopyDbHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
//    private static final String DATABASE_NAME = "database.sqlite";
    // Contacts table name
    private static final String DB_PATH_SUFFIX = "/databases/";
    static Context ctx;

    public SqlLiteCopyDbHelper(Context context) {
        super(context, Constant.DB_NAME_V2, null, DATABASE_VERSION);
        ctx = context;
    }


    public boolean CopyDataBaseFromAsset() throws IOException {
        try {

            InputStream myInput = ctx.getAssets().open(Constant.DB_NAME_V2);

            // Path to the just created empty db
            String outFileName = getDatabasePath();

            // if the path doesn't exist first, create it
            File f = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();

            // Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + Constant.DB_NAME_V2;
    }

//    public SQLiteDatabase openDataBase() throws SQLException{
//    	File dbFile = ctx.getDatabasePath(Constant.DB_NAME_V2);
//
//        if (!dbFile.exists()) {
//            try {
//            	CopyDataBaseFromAsset();
//                System.out.println("Copying sucess from Assets folder");
//            } catch (IOException e) {
//                throw new RuntimeException("Error creating source database", e);
//            }
//        }
//
//        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
//    }

    public boolean openDataBase() throws SQLException {
        ULog.i("CopyDB", "openDataBase");
        File dbFile = ctx.getDatabasePath(Constant.DB_NAME_V2);
        try {
            if (!dbFile.exists()) {

                return CopyDataBaseFromAsset();
//                System.out.println("Copying sucess from Assets folder");

            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}

