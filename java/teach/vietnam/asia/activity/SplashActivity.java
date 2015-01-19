package teach.vietnam.asia.activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import teach.vietnam.asia.BuildConfig;
import teach.vietnam.asia.R;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.DaoSession;
import teach.vietnam.asia.entity.VNEntity;
import teach.vietnam.asia.entity.tblViet;
import teach.vietnam.asia.utils.Common;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;

public class SplashActivity extends BaseActivity {

    public DaoMaster daoMaster;
    private ProgressDialog progress;
    @Override
    protected int getViewLayoutId() {
        return R.layout.splash_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        try {
            ULog.i(this, "======== Product value:" + Constant.isPro);
            TextView tvTitle = getViewChild(R.id.tvTitle);

            new CreateInitData().execute();

            Typeface tf = Typeface.createFromAsset(getAssets(), "aachenb.ttf");
            tvTitle.setTypeface(tf, Typeface.BOLD);
        } catch (Exception e) {
            ULog.e(SplashActivity.class, "initView Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private class CreateInitData extends AsyncTask<Void, Void, Boolean> {
        private String initData = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ULog.i(CreateInitData.this, "onPreExecute....");
            try {
                daoMaster = ((MyApplication) getApplicationContext()).daoMaster;

                initData = pref.getStringValue("", Constant.JSON_WORDS_NAME);
                if (initData.equals("") || !initData.equals(Constant.KEY_UPDATE)) {
                    progress = new ProgressDialog(SplashActivity.this);
                    progress.setMessage(getString(R.string.msg_now_loading));
                    progress.setProgressStyle(progress.STYLE_HORIZONTAL);
                    progress.setCancelable(false);
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();
                }

            } catch (Exception e) {
                ULog.e(SplashActivity.class, "CreateInitData onPreExecute Error: " + e.getMessage());
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            // String initData = "";

            try {
                ULog.i(CreateInitData.this, "Loading....");
                // initData = BaseActivity.pref.getStringValue("", Constant.JSON_WORDS_NAME);
                if (initData.equals("")|| !initData.equals(Constant.KEY_UPDATE)) {
                    DaoMaster.dropAllTables(daoMaster.getDatabase(), true);
                    DaoMaster.createAllTables(daoMaster.getDatabase(), true);
                    insertData();
                } else {
                    ULog.i(SplashActivity.class, "Don't insert");

                    Thread.sleep(1200);
                }
                // BaseActivity.pref.putStringValue(Constant.PREF_INIT, Constant.PREF_INIT);

            } catch (Exception e) {
                ULog.e(SplashActivity.class, "load data fail");
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result)
                pref.putStringValue(Constant.KEY_UPDATE, Constant.JSON_WORDS_NAME);

            if (!isFinishing() && progress != null && progress.isShowing())
                progress.dismiss();

            SplashActivity.this.startActivity2(MainActivity.class);
//            SplashActivity.this.startActivity2(RecognizeMainActicity.class);
//            SplashActivity.this.startActivity2(AndroidDatabaseManager.class);

            SplashActivity.this.finish();
        }
    }

    private void insertData() {
        VNEntity word;
        try {
            DaoSession mDaoSession = daoMaster.newSession();
            if (Constant.isMyDebug) {
                word = (VNEntity) Common.getObjectJson(this, VNEntity.class, Constant.JSON_WORDS_NAME);

            } else {
                word = (VNEntity) Common.getDataDecrypt(this, VNEntity.class, Constant.JSON_WORDS_NAME);
            }

            if (word == null) {
                ULog.e(SplashActivity.class, "Can't load Json");
                return;
            }

            ULog.i(this, "===== words size data :" + word.listData.size());
            int count = 0;
            for (tblViet entity : word.listData) {
                mDaoSession.insertOrReplace(entity);
                count++;
                progress.setProgress(count);
            }
            ULog.i(this, "===== words load finish");
            progress.dismiss();
            mDaoSession.clear();

        } catch (Exception e) {
            ULog.e(SplashActivity.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }
}
