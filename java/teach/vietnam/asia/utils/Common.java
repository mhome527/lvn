package teach.vietnam.asia.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.FileChannel;

import de.greenrobot.dao.query.QueryBuilder;
import teach.vietnam.asia.BuildConfig;
import teach.vietnam.asia.activity.MyApplication;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.tblMapName;
import teach.vietnam.asia.entity.tblMapNameDao;

public class Common {
    /**
     * @param context
     * @param cls
     * @param name
     * @return
     */
    public static Object getObjectJson(Context context, Class<?> cls, String name) {
        Object obj = null;
        try {
            Reader reader = new InputStreamReader(context.getAssets().open(name));
            Gson gson = new Gson();
            obj = gson.fromJson(reader, cls);
        } catch (Exception e) {
            ULog.e("Common", "getObjectJson Error:" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * get data form encrypted file
     *
     * @param context
     * @param cls
     * @param fileName
     * @return
     */
    public static Object getDataDecrypt(Context context, Class<?> cls, String fileName) {
        Object obj = null;
        EncryptData eData;
        InputStream is;
        byte[] arrB;
        try {
            eData = new EncryptData(new byte[16]);

            is = context.getAssets().open(fileName);
            byte[] fileBytes = new byte[is.available()];
            is.read(fileBytes);
            is.close();
            arrB = eData.decrypt(fileBytes);

            Gson gson = new Gson();
            obj = gson.fromJson(new String(arrB), cls);
            ULog.i(Common.class, "getDataDecrypt successful");
        } catch (Exception e) {
            ULog.e("Common", "getObjectJson Error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
        return obj;
    }

    public static Boolean isNetAvailable(Context con) {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiInfo.isConnected() || mobileInfo.isConnected()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Object getObjectJsonPath(Class<?> cls, String pathFile) {
        Object obj = null;
        try {
            ULog.i(Common.class, "Read file json from local");
            File yourFile = new File(pathFile);
            FileInputStream stream = new FileInputStream(yourFile);
            Reader reader = new InputStreamReader(stream);
            Gson gson = new Gson();
            obj = gson.fromJson(reader, cls);
        } catch (Exception e) {
            ULog.e("Common", "getObjectJsonPath 3 Error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
        return obj;
    }

    public static String getNameSound(String name) {
        DaoMaster daoMaster;
        tblMapNameDao daoMapName;
        QueryBuilder<tblMapName> qb;
        try {
            daoMaster = MyApplication.getInstance().daoMaster;
            daoMapName = daoMaster.newSession().getTblMapNameDao();
            qb = daoMapName.queryBuilder();

            qb.where(tblMapNameDao.Properties.Filename.eq(name));
            if (qb.list().size() > 0)
                return qb.list().get(0).getSound();
        } catch (Exception e) {
            ULog.e(Common.class, "getNameSound Error: " + e.getMessage());
        }
        return "";
    }

    public static void exportDB(String databaseName, Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + context.getPackageName() + "//databases//" + databaseName + "";
                String backupDBPath = "bk_" + databaseName;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    ULog.i("Common", "export database");
                }
            }
        } catch (Exception e) {
            ULog.e("Common", "export error: " + e.getMessage());
        }
    }


}
