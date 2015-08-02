package teach.vietnam.asia.db;

import android.os.AsyncTask;

import teach.vietnam.asia.BuildConfig;
import teach.vietnam.asia.R;
import teach.vietnam.asia.activity.BaseActivity;
import teach.vietnam.asia.activity.MyApplication;
import teach.vietnam.asia.activity.RecognizeMainActicity;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.DaoSession;
import teach.vietnam.asia.entity.EnRecEntity;
import teach.vietnam.asia.entity.EsRecEntity;
import teach.vietnam.asia.entity.FrRecEntity;
import teach.vietnam.asia.entity.ItRecEntity;
import teach.vietnam.asia.entity.JaRecEntity;
import teach.vietnam.asia.entity.KoRecEntity;
import teach.vietnam.asia.entity.RuRecEntity;
import teach.vietnam.asia.entity.tblRecEN;
import teach.vietnam.asia.entity.tblRecES;
import teach.vietnam.asia.entity.tblRecFR;
import teach.vietnam.asia.entity.tblRecIT;
import teach.vietnam.asia.entity.tblRecJA;
import teach.vietnam.asia.entity.tblRecKO;
import teach.vietnam.asia.entity.tblRecRU;
import teach.vietnam.asia.utils.Common;
import teach.vietnam.asia.utils.Prefs;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

/**
 * Created by admin on 3/3/15.
 */
@Deprecated
public class DBDataRecognize extends AsyncTask<Void, Void, Boolean> {

    public DaoMaster daoMaster;
    private String initData = "";
//    private RecognizeMainActicity activity;
    private BaseActivity activity;
    //    private ProgressDialog progressDialog;
    private Prefs pref;
    private String lang;

    private IFinishSave iFinishSave;

    public DBDataRecognize(BaseActivity activity) {
        this.activity = activity;
        lang = activity.lang;
        daoMaster = ((MyApplication) activity.getApplicationContext()).daoMaster;
    }

//    public DBDataRecognize(RecognizeMainActicity activity, IFinishSave iFinishSave) {
//        this.activity = activity;
//        this.iFinishSave = iFinishSave;
//    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ULog.i(DBDataRecognize.this, "onPreExecute loading........................");
//        lang = activity.lang;
//        pref = new Prefs(activity.getApplicationContext());
//
//        initData = pref.getStringValue("", lang + "rc");
//        if (initData.equals("") || !initData.equals(activity.getString(R.string.db_value_rc))) {
//            activity.progressDialog = new ProgressDialog(activity);
//            activity.progressDialog.setMessage(activity.getString(R.string.msg_now_loading));
//            activity.progressDialog.setProgressStyle(activity.progressDialog.STYLE_HORIZONTAL);
//            activity.progressDialog.setCancelable(false);
//            activity.progressDialog.setCanceledOnTouchOutside(false);
//            activity.progressDialog.show();
//        } else
//            ULog.i(DBDataLanguage.class, "Don't create db, lang:" + lang);
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            ULog.i(DBDataRecognize.this, "Loading....");
            daoMaster = ((MyApplication) activity.getApplicationContext()).daoMaster;
//            initData = activity.pref.getStringValue("", lang + "rc");
//            if (initData.equals("") || !initData.equals(activity.getString(R.string.db_value_rc))) {
//                Utility.deleteTableRec(lang, daoMaster.getDatabase());
//                Utility.CreateTableRec(lang, daoMaster.getDatabase());
//
                insertTable();
//            } else {
//                ULog.i(RecognizeMainActicity.class, "Don't insert map");
//                return false;
//            }

        } catch (Exception e) {
            ULog.e(RecognizeMainActicity.class, "load data fail");
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (result)
            pref.putStringValue(activity.getString(R.string.db_value_rc), lang + "rc");

//        if (!activity.isFinishing() && activity.progressDialog != null && activity.progressDialog.isShowing())
//            activity.progressDialog.dismiss();
//        if (iFinishSave != null)
//            iFinishSave.isFinish(result);
//        new LoadData().execute();
    }

    public void insertTable() {
        lang = "ja";
        insertDataJA();
        lang = "ko";
        insertDataKo();
        lang = "ru";
        insertDataRU();
        lang = "fr";
        insertDataFR();
        lang = "it";
        insertDataIT();
        lang = "es";
        insertDataEs();


//        if (lang.equals("ja"))
//            insertDataJA();
//        else if (lang.equals("ko"))
//            insertDataKo();
//        else if (lang.equals("ru"))
//            insertDataRU();
//        else if (lang.equals("fr"))
//            insertDataFR();
//        else if (lang.equals("it"))
//            insertDataIT();
//        else if (lang.equals("es"))
//            insertDataEs();
//        else
//            insertDataEN();
    }

    private void insertDataEN() {
        EnRecEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
            word = (EnRecEntity) Common.getObjectJson(activity, EnRecEntity.class, Utility.getFileNameRecDB(lang));


            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

            ULog.i(this, "=====insertDataEN words size data :" + word.listData.size());
//            activity.progressDialog.setMax(word.listData.size());

            int count = 0;
            for (tblRecEN entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
//                count++;
//                activity.progressDialog.setProgress(count);
            }
            ULog.i(this, "insertDataEN ===== words load finish");
//            activity.progressDialog.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    private void insertDataJA() {
        JaRecEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
            word = (JaRecEntity) Common.getObjectJson(activity, JaRecEntity.class, Utility.getFileNameRecDB(lang));

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }
//            activity.progressDialog.setMax(word.listData.size());
            ULog.i(this, "=====insertDataJA words size data :" + word.listData.size());
//            int count = 0;
            for (tblRecJA entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
//                count++;
//                activity.progressDialog.setProgress(count);
            }
            ULog.i(this, "insertDataJA ===== words load finish");
//            activity.progressDialog.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    private void insertDataKo() {
        KoRecEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
            word = (KoRecEntity) Common.getObjectJson(activity, KoRecEntity.class, Utility.getFileNameRecDB(lang));


            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

//            activity.progressDialog.setMax(word.listData.size());
            ULog.i(this, "===== words size data :" + word.listData.size());
//            int count = 0;
            for (tblRecKO entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
//                count++;
//                activity.progressDialog.setProgress(count);
            }
            ULog.i(this, "insertDataKo ===== words load finish");
//            activity.progressDialog.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    private void insertDataEs() {
        EsRecEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
            word = (EsRecEntity) Common.getObjectJson(activity, EsRecEntity.class, Utility.getFileNameRecDB(lang));


            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

//            activity.progressDialog.setMax(word.listData.size());
            ULog.i(this, "===== words size data :" + word.listData.size());
//            int count = 0;
            for (tblRecES entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
//                count++;
//                activity.progressDialog.setProgress(count);
            }
            ULog.i(this, "insertDataEs ===== words load finish");
//            activity.progressDialog.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    private void insertDataFR() {
        FrRecEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
            word = (FrRecEntity) Common.getObjectJson(activity, FrRecEntity.class, Utility.getFileNameRecDB(lang));


            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

//            activity.progressDialog.setMax(word.listData.size());
            ULog.i(this, "===== words size data :" + word.listData.size());
//            int count = 0;
            for (tblRecFR entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
//                count++;
//                activity.progressDialog.setProgress(count);
            }
            ULog.i(this, "insertDataFR ===== words load finish");
//            activity.progressDialog.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    private void insertDataIT() {
        ItRecEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
            word = (ItRecEntity) Common.getObjectJson(activity, ItRecEntity.class, Utility.getFileNameRecDB(lang));


            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

//            activity.progressDialog.setMax(word.listData.size());
            ULog.i(this, "===== words size data :" + word.listData.size());
//            int count = 0;
            for (tblRecIT entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
//                count++;
//                activity.progressDialog.setProgress(count);
            }
            ULog.i(this, "insertDataIT ===== words load finish");
//            activity.progressDialog.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    private void insertDataRU() {
        RuRecEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
            word = (RuRecEntity) Common.getObjectJson(activity, RuRecEntity.class, Utility.getFileNameRecDB(lang));

            if (word == null) {
                ULog.e(DBDataLanguage.class, "Can't load Json");
                return;
            }

//            activity.progressDialog.setMax(word.listData.size());
            ULog.i(this, "===== words size data :" + word.listData.size());
//            int count = 0;
            for (tblRecRU entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
//                count++;
//                activity.progressDialog.setProgress(count);
            }
            ULog.i(this, "===== insertDataRU words load finish");
//            activity.progressDialog.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(DBDataLanguage.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    public interface IFinishSave {
        void isFinish(boolean b);
    }


}

