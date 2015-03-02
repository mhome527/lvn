package teach.vietnam.asia.db;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import teach.vietnam.asia.BuildConfig;
import teach.vietnam.asia.R;
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
public class DBDataLanguage extends AsyncTask<Void, Void, Boolean> {
    public DaoMaster daoMaster;
    ICreateTable iCreateTable;
    private ProgressDialog progress;
    private Activity activity;
    private String initData = "";
    private String lang;
    private Prefs pref;

    public DBDataLanguage(Activity activity, ICreateTable iCreateTable) {
        this.activity = activity;
        this.iCreateTable = iCreateTable;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ULog.i(DBDataLanguage.this, "onPreExecute....");
        try {
            lang = activity.getString(R.string.language);
            pref = new Prefs(activity.getApplicationContext());

//            dao = Utility.getDao(activity, lang);
//                qb = dao.queryBuilder();

//            initData = pref.getStringValue("", Constant.JSON_WORDS_NAME);
            initData = pref.getStringValue("", lang);
            if (initData.equals("") || !initData.equals(activity.getString(R.string.db_value))) {
                progress = new ProgressDialog(activity);
                progress.setMessage(activity.getString(R.string.msg_now_loading));
                progress.setProgressStyle(progress.STYLE_HORIZONTAL);
                progress.setCancelable(false);
                progress.setCanceledOnTouchOutside(false);
                progress.show();
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

        if (!activity.isFinishing() && progress != null && progress.isShowing())
            progress.dismiss();
        iCreateTable.iFinishCreate();
//            SplashActivity.this.startActivity2(RecognizeMainActicity.class);
//            SplashActivity.this.startActivity2(AndroidDatabaseManager.class);

    }

    private void insertTable() {
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

    private void insertDataEN() {
        EnEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
            if (Constant.isMyDebug) {
                word = (EnEntity) Common.getObjectJson(activity, EnEntity.class, Utility.getFileNameDB(lang));

            } else {
                word = (EnEntity) Common.getDataDecrypt(activity, EnEntity.class, Utility.getFileNameDB(lang));
            }

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

            ULog.i(this, "=====insertDataEN words size data :" + word.listData.size());
            progress.setMax(word.listData.size());

            int count = 0;
            for (tblVietEN entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
                count++;
                progress.setProgress(count);
            }
            ULog.i(this, "===== words load finish");
            progress.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    private void insertDataJA() {
        JaEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
            if (Constant.isMyDebug) {
                word = (JaEntity) Common.getObjectJson(activity, JaEntity.class, Constant.FILE_JA);

            } else {
                word = (JaEntity) Common.getDataDecrypt(activity, JaEntity.class, Constant.FILE_JA);
            }

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }
            progress.setMax(word.listData.size());
            ULog.i(this, "=====insertDataJA words size data :" + word.listData.size());
            int count = 0;
            for (tblVietJA entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
                count++;
                progress.setProgress(count);
            }
            ULog.i(this, "===== words load finish");
            progress.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    private void insertDataKo() {
        KoEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
            if (Constant.isMyDebug) {
                word = (KoEntity) Common.getObjectJson(activity, KoEntity.class, Constant.FILE_KO);

            } else {
                word = (KoEntity) Common.getDataDecrypt(activity, KoEntity.class, Constant.FILE_KO);
            }

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

            progress.setMax(word.listData.size());
            ULog.i(this, "===== words size data :" + word.listData.size());
            int count = 0;
            for (tblVietKO entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
                count++;
                progress.setProgress(count);
            }
            ULog.i(this, "===== words load finish");
            progress.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    private void insertDataEs() {
        EsEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
            if (Constant.isMyDebug) {
                word = (EsEntity) Common.getObjectJson(activity, EsEntity.class, Constant.FILE_ES);

            } else {
                word = (EsEntity) Common.getDataDecrypt(activity, EsEntity.class, Constant.FILE_ES);
            }

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

            progress.setMax(word.listData.size());
            ULog.i(this, "===== words size data :" + word.listData.size());
            int count = 0;
            for (tblVietES entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
                count++;
                progress.setProgress(count);
            }
            ULog.i(this, "===== words load finish");
            progress.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    private void insertDataFR() {
        FrEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
            if (Constant.isMyDebug) {
                word = (FrEntity) Common.getObjectJson(activity, FrEntity.class, Constant.FILE_FR);

            } else {
                word = (FrEntity) Common.getDataDecrypt(activity, FrEntity.class, Constant.FILE_FR);
            }

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

            progress.setMax(word.listData.size());
            ULog.i(this, "===== words size data :" + word.listData.size());
            int count = 0;
            for (tblVietFR entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
                count++;
                progress.setProgress(count);
            }
            ULog.i(this, "===== words load finish");
            progress.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    private void insertDataIT() {
        ItEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
            if (Constant.isMyDebug) {
                word = (ItEntity) Common.getObjectJson(activity, ItEntity.class, Constant.FILE_IT);

            } else {
                word = (ItEntity) Common.getDataDecrypt(activity, ItEntity.class, Constant.FILE_IT);
            }

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

            progress.setMax(word.listData.size());
            ULog.i(this, "===== words size data :" + word.listData.size());
            int count = 0;
            for (tblVietIT entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
                count++;
                progress.setProgress(count);
            }
            ULog.i(this, "===== words load finish");
            progress.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    private void insertDataRU() {
        RuEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
            if (Constant.isMyDebug) {
                word = (RuEntity) Common.getObjectJson(activity, RuEntity.class, Constant.FILE_RU);

            } else {
                word = (RuEntity) Common.getDataDecrypt(activity, RuEntity.class, Constant.FILE_RU);
            }

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

            progress.setMax(word.listData.size());
            ULog.i(this, "===== words size data :" + word.listData.size());
            int count = 0;
            for (tblVietRU entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
                count++;
                progress.setProgress(count);
            }
            ULog.i(this, "===== words load finish");
            progress.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    public interface ICreateTable {
        public void iFinishCreate();
    }

}
