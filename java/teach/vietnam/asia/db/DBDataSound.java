package teach.vietnam.asia.db;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import teach.vietnam.asia.BuildConfig;
import teach.vietnam.asia.R;
import teach.vietnam.asia.activity.MyApplication;
import teach.vietnam.asia.activity.SplashActivity;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.DaoSession;
import teach.vietnam.asia.entity.MapNameEntity;
import teach.vietnam.asia.entity.tblMapName;
import teach.vietnam.asia.entity.tblMapNameDao;
import teach.vietnam.asia.utils.Common;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.Prefs;
import teach.vietnam.asia.utils.ULog;

/**
 * Created by admin on 2/16/15.
 */
public class DBDataSound extends AsyncTask<Void, Void, Boolean> {
    public DaoMaster daoMaster;
    public ICreateTable iCreateTable;
    private SplashActivity activity;
    private String initData = "";
    private String lang;
    private Prefs pref;

    public DBDataSound(SplashActivity activity, ICreateTable iCreateTable) {
        this.activity = activity;
        this.iCreateTable = iCreateTable;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ULog.i(DBDataSound.this, "onPreExecute....");
        try {
            lang = activity.getString(R.string.language);
            pref = new Prefs(activity.getApplicationContext());

//            dao = Utility.getDao(activity, lang);
//                qb = dao.queryBuilder();

//            initData = pref.getStringValue("", Constant.JSON_WORDS_NAME);
            initData = pref.getStringValue("", Constant.KEY_SOUND);
            if (initData.equals("") || !initData.equals(Constant.VALUE_SOUND)) {
                activity.progressDialog = new ProgressDialog(activity);
                activity.progressDialog.setMessage(activity.getString(R.string.msg_now_loading));
                activity.progressDialog.setProgressStyle(activity.progressDialog.STYLE_HORIZONTAL);
                activity.progressDialog.setCancelable(false);
                activity.progressDialog.setCanceledOnTouchOutside(false);
                activity.progressDialog.show();
            } else
                ULog.i(DBDataSound.class, "Don't create db, lang:" + lang);

        } catch (Exception e) {
            ULog.e(DBDataSound.class, "CreateInitData onPreExecute Error: " + e.getMessage());
        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            if (initData.equals("") || !initData.equals(Constant.VALUE_SOUND)) {
                ULog.i(DBDataSound.class, "doInBackground Loading....");
                daoMaster = ((MyApplication) activity.getApplicationContext()).daoMaster;
                tblMapNameDao.dropTable(daoMaster.getDatabase(), true);
                tblMapNameDao.createTable(daoMaster.getDatabase(), true);
                insertData();
            } else {
                ULog.i(DBDataSound.class, "Don't insert");
//                Thread.sleep(1000);
            }
            // BaseActivity.pref.putStringValue(Constant.PREF_INIT, Constant.PREF_INIT);

        } catch (Exception e) {
            ULog.e(DBDataSound.class, "load data fail");
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result)
            pref.putStringValue(Constant.VALUE_SOUND, Constant.KEY_SOUND);

        if (!activity.isFinishing() && activity.progressDialog != null && activity.progressDialog.isShowing())
            activity.progressDialog.dismiss();
        iCreateTable.iFinishCreate();
//            SplashActivity.this.startActivity2(AndroidDatabaseManager.class);

    }

    private void insertData() {
        MapNameEntity mapName;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
//            if (Constant.isMyDebug) {
            mapName = (MapNameEntity) Common.getObjectJson(activity, MapNameEntity.class, Constant.JSON_MAPNAME_NAME);
//            } else {
//            mapName = (MapNameEntity) Common.getDataDecrypt(activity, MapNameEntity.class, Constant.JSON_MAPNAME_NAME);
//            }

            if (mapName == null) {
                ULog.e(DBDataSound.class, "Can't load Json");
                return;
            }

            ULog.i(this, "===== map name size data :" + mapName.listData.size());
            activity.progressDialog.setMax(mapName.listData.size());
            int count = 0;
            for (tblMapName entity : mapName.listData) {
                count++;
                // ULog.i(this, "===== Insert data :" + entity.getAlphabet());
                mDaoSession.insertOrReplace(entity);
                activity.progressDialog.setProgress(count);
            }


        } catch (Exception e) {
            ULog.e(DBDataSound.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }


    public interface ICreateTable {
        void iFinishCreate();
    }
}
