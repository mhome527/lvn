package teach.vietnam.asia.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import teach.vietnam.asia.R;
import teach.vietnam.asia.db.DBDataSound;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;

public class SplashActivity extends BaseActivity implements DBDataSound.ICreateTable {

    public DaoMaster daoMaster;
//    private ProgressDialog progress;

    @Override
    protected int getViewLayoutId() {
        return R.layout.splash_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        try {
            ULog.i(this, "======== Product value:" + Constant.isPro);
            TextView tvTitle = getViewChild(R.id.tvTitle);

            new DBDataSound(this, this).execute();

            Typeface tf = Typeface.createFromAsset(getAssets(), "aachenb.ttf");
            tvTitle.setTypeface(tf, Typeface.BOLD);
        } catch (Exception e) {
            ULog.e(SplashActivity.class, "initView Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void reloadData() {

    }

    @Override
    public void iFinishCreate() {
        ULog.i(this, "======== load map data finish");
        SplashActivity.this.startActivity2(MainActivity.class);
        SplashActivity.this.finish();
    }

    ////

//    private class CreateInitData extends AsyncTask<Void, Void, Boolean> {
//        private String initData = "";
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            ULog.i(CreateInitData.this, "onPreExecute loading........................");
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//
//            try {
//                ULog.i(CreateInitData.this, "Loading....");
//                initData = pref.getStringValue("", Constant.JSON_MAPNAME_NAME);
//                if (initData.equals("") || !initData.equals(Constant.KEY_UPDATE)) {
//                    insertData();
//                } else {
//                    ULog.i(SplashActivity.class, "Don't insert map");
//                }
//
//            } catch (Exception e) {
//                ULog.e(SplashActivity.class, "load data fail");
//                return false;
//            }
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            super.onPostExecute(result);
//            if (result) {
//                pref.putStringValue(Constant.KEY_UPDATE, Constant.JSON_MAPNAME_NAME);
//            }
//            if (!isFinishing() && progress != null && progress.isShowing())
//                progress.dismiss();
//
//            SplashActivity.this.startActivity2(MainActivity.class);
//            SplashActivity.this.finish();
//
////            if (clsForm != null)
////                startActivity2(clsForm, mKind);
////            clsForm = null;
////            mKind = 0;
////            isLoading = false;
//
//        }
//    }
//
//    private void insertData() {
//        DaoMaster daoMaster;
//        MapNameEntity mapName;
//        try {
//            daoMaster = ((MyApplication) getApplicationContext()).daoMaster;
//            DaoSession mDaoSession = daoMaster.newSession();
//            if (Constant.isMyDebug) {
//                mapName = (MapNameEntity) Common.getObjectJson(this, MapNameEntity.class, Constant.JSON_MAPNAME_NAME);
//            } else {
//                mapName = (MapNameEntity) Common.getDataDecrypt(this, MapNameEntity.class, Constant.JSON_MAPNAME_NAME);
//            }
//
//            if (mapName == null) {
//                ULog.e(SplashActivity.class, "Can't load Json");
//                return;
//            }
//
//            ULog.i(this, "===== map name size data :" + mapName.listData.size());
//            progress.setMax(mapName.listData.size());
//            int count=0;
//            for (tblMapName entity : mapName.listData) {
//                count++;
//                // ULog.i(this, "===== Insert data :" + entity.getAlphabet());
//                mDaoSession.insertOrReplace(entity);
//                progress.setProgress(count);
//            }
//
//
//        } catch (Exception e) {
//            ULog.e(SplashActivity.class, "Insert error:" + e.getMessage());
//            if (BuildConfig.DEBUG)
//                e.printStackTrace();
//        }
//    }
}
