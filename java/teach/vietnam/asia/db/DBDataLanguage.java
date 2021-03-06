package teach.vietnam.asia.db;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import teach.vietnam.asia.R;
import teach.vietnam.asia.activity.BaseActivity;
import teach.vietnam.asia.activity.MainActivity;
import teach.vietnam.asia.activity.MyApplication;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.DaoSession;
import teach.vietnam.asia.entity.EnEntity;
import teach.vietnam.asia.entity.EsEntity;
import teach.vietnam.asia.entity.FrEntity;
import teach.vietnam.asia.entity.ItEntity;
import teach.vietnam.asia.entity.JaEntity;
import teach.vietnam.asia.entity.KoEntity;
import teach.vietnam.asia.entity.RuEntity;
import teach.vietnam.asia.entity.tblVietEN;
import teach.vietnam.asia.entity.tblVietES;
import teach.vietnam.asia.entity.tblVietFR;
import teach.vietnam.asia.entity.tblVietIT;
import teach.vietnam.asia.entity.tblVietJA;
import teach.vietnam.asia.entity.tblVietKO;
import teach.vietnam.asia.entity.tblVietRU;
import teach.vietnam.asia.utils.Common;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.Prefs;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

/**
 * Created by admin on 2/16/15.
 */
@Deprecated
public class DBDataLanguage extends AsyncTask<Void, Void, Boolean> {
    public DaoMaster daoMaster;
    ICreateTable iCreateTable;
//    private ProgressDialog progress;
    private MainActivity activity;
    private String initData = "";
    private String lang;
    private Prefs pref;

    public DBDataLanguage(MainActivity activity, ICreateTable iCreateTable) {
        this.activity = activity;
        this.iCreateTable = iCreateTable;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ULog.i("DBDataLanguage", "onPreExecute....");
        try {
//            lang = activity.getString(R.string.language);
            lang = BaseActivity.pref.getStringValue("en", Constant.EN);

            pref = new Prefs(activity.getApplicationContext());

//            dao = Utility.getDao(activity, lang);
//                qb = dao.queryBuilder();

//            initData = pref.getStringValue("", Constant.JSON_WORDS_NAME);
            initData = pref.getStringValue("", lang);
            if (initData.equals("") || !initData.equals(activity.getString(R.string.db_value))) {
                activity.progressDialog = new ProgressDialog(activity);
                activity.progressDialog.setMessage(activity.getString(R.string.msg_now_loading));
                activity.progressDialog.setProgressStyle(activity.progressDialog.STYLE_HORIZONTAL);
                activity.progressDialog.setCancelable(false);
                activity.progressDialog.setCanceledOnTouchOutside(false);
//                activity.progressDialog.show();
            } else
                ULog.i(DBDataLanguage.class, "Don't create db, lang:" + lang);

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "CreateInitData onPreExecute Error: " + e.getMessage());
        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            // initData = BaseActivity.pref.getStringValue("", Constant.JSON_WORDS_NAME);
            if (initData.equals("") || !initData.equals(activity.getString(R.string.db_value))) {
                ULog.i(DBDataLanguage.class, "doInBackground Loading....");
                daoMaster = ((MyApplication) activity.getApplicationContext()).daoMaster;
                Utility.deleteTableLang(lang, daoMaster.getDatabase());
                Utility.CreateTableLang(lang, daoMaster.getDatabase());
//                insertData(activity);
                insertTable();
            } else {
                ULog.i(DBDataLanguage.class, "Don't insert");
                return false;
//                Thread.sleep(1000);
            }
            // BaseActivity.pref.putStringValue(Constant.PREF_INIT, Constant.PREF_INIT);

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "load data fail");
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result)
            pref.putStringValue(activity.getString(R.string.db_value), lang);

        if (!activity.isFinishing() && activity.progressDialog != null && activity.progressDialog.isShowing())
            activity.progressDialog.dismiss();
        iCreateTable.iFinishCreate();
//            SplashActivity.this.startActivity2(RecognizeMainActicity.class);
//            SplashActivity.this.startActivity2(AndroidDatabaseManager.class);

    }

    private void insertTable()throws Exception {
        if (lang.equals("ja"))
            insertDataJA();
        else if (lang.equals("ko"))
            insertDataKo();
        else if (lang.equals("ru"))
            insertDataRU();
        else if (lang.equals("fr"))
            insertDataFR();
        else if (lang.equals("it"))
            insertDataIT();
        else if (lang.equals("es"))
            insertDataEs();
        else
            insertDataEN();
    }

    private void insertDataEN() throws Exception{
        EnEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
//            if (Constant.isMyDebug) {
                word = (EnEntity) Common.getObjectJson(activity, EnEntity.class, Utility.getFileNameDB(lang));
//            } else {
//                word = (EnEntity) Common.getDataDecrypt(activity, EnEntity.class, Utility.getFileNameDB(lang));
//            }

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

            ULog.i(this, "=====insertDataEN words size data :" + word.listData.size());
            activity.progressDialog.setMax(word.listData.size());

            int count = 0;
            for (tblVietEN entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
                count++;
                activity.progressDialog.setProgress(count);
            }
            ULog.i(this, "insertDataEN ===== words load finish");
//            activity.progressDialog.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            throw new Exception(e);
        }
    }

    private void insertDataJA() throws Exception{
        JaEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
//            if (Constant.isMyDebug) {
                word = (JaEntity) Common.getObjectJson(activity, JaEntity.class, Constant.FILE_JA);
//            } else {
//                word = (JaEntity) Common.getDataDecrypt(activity, JaEntity.class, Constant.FILE_JA);
//            }

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }
            activity.progressDialog.setMax(word.listData.size());
            ULog.i(this, "=====insertDataJA words size data :" + word.listData.size());
            int count = 0;
            for (tblVietJA entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
                count++;
                activity.progressDialog.setProgress(count);
            }
            ULog.i(this, "===== words load finish");
//            activity.progressDialog.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            throw new Exception(e);
        }
    }

    private void insertDataKo() throws Exception{
        KoEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
//            if (Constant.isMyDebug) {
                word = (KoEntity) Common.getObjectJson(activity, KoEntity.class, Constant.FILE_KO);
//            } else {
//                word = (KoEntity) Common.getDataDecrypt(activity, KoEntity.class, Constant.FILE_KO);
//            }

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

            activity.progressDialog.setMax(word.listData.size());
            ULog.i(this, "===== words size data :" + word.listData.size());
            int count = 0;
            for (tblVietKO entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
                count++;
                activity.progressDialog.setProgress(count);
            }
            ULog.i(this, "===== words load finish");
//            activity.progressDialog.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            throw new Exception(e);
        }
    }

    private void insertDataEs() throws Exception{
        EsEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
//            if (Constant.isMyDebug) {
                word = (EsEntity) Common.getObjectJson(activity, EsEntity.class, Constant.FILE_ES);
//            } else {
//                word = (EsEntity) Common.getDataDecrypt(activity, EsEntity.class, Constant.FILE_ES);
//            }

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

            activity.progressDialog.setMax(word.listData.size());
            ULog.i(this, "===== words size data :" + word.listData.size());
            int count = 0;
            for (tblVietES entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
                count++;
                activity.progressDialog.setProgress(count);
            }
            ULog.i(this, "===== words load finish");
//            activity.progressDialog.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            throw new Exception(e);
        }
    }

    private void insertDataFR() throws Exception{
        FrEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
//            if (Constant.isMyDebug) {
                word = (FrEntity) Common.getObjectJson(activity, FrEntity.class, Constant.FILE_FR);
//            } else {
//                word = (FrEntity) Common.getDataDecrypt(activity, FrEntity.class, Constant.FILE_FR);
//            }

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

            activity.progressDialog.setMax(word.listData.size());
            ULog.i(this, "===== words size data :" + word.listData.size());
            int count = 0;
            for (tblVietFR entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
                count++;
                activity.progressDialog.setProgress(count);
            }
            ULog.i(this, "===== words load finish");
//            activity.progressDialog.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            throw new Exception(e);
        }
    }

    private void insertDataIT() throws Exception{
        ItEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
//            if (Constant.isMyDebug) {
                word = (ItEntity) Common.getObjectJson(activity, ItEntity.class, Constant.FILE_IT);
//            } else {
//                word = (ItEntity) Common.getDataDecrypt(activity, ItEntity.class, Constant.FILE_IT);
//            }

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

            activity.progressDialog.setMax(word.listData.size());
            ULog.i(this, "===== words size data :" + word.listData.size());
            int count = 0;
            for (tblVietIT entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
                count++;
                activity.progressDialog.setProgress(count);
            }
            ULog.i(this, "===== words load finish");
//            activity.progressDialog.dismiss();
            mDaoSession.clear();

        } catch (Exception e){
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            throw new Exception(e);
        }
    }

    private void insertDataRU() throws Exception{
        RuEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
//            if (Constant.isMyDebug) {
                word = (RuEntity) Common.getObjectJson(activity, RuEntity.class, Constant.FILE_RU);
//            } else {
//                word = (RuEntity) Common.getDataDecrypt(activity, RuEntity.class, Constant.FILE_RU);
//            }

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

            activity.progressDialog.setMax(word.listData.size());
            ULog.i(this, "===== words size data :" + word.listData.size());
            int count = 0;
            for (tblVietRU entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
                count++;
                activity.progressDialog.setProgress(count);
            }
            ULog.i(this, "insertDataRU ===== words load finish");
//            activity.progressDialog.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            throw new Exception(e);
        }
    }

    public interface ICreateTable {
        void iFinishCreate();
    }

}
