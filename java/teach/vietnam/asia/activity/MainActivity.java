package teach.vietnam.asia.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import java.util.Locale;

import teach.vietnam.asia.BuildConfig;
import teach.vietnam.asia.R;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.DaoSession;
import teach.vietnam.asia.entity.MapNameEntity;
import teach.vietnam.asia.entity.tblMapName;
import teach.vietnam.asia.utils.Common;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

public class MainActivity extends BaseActivity implements OnClickListener {

    private boolean isLoading = true;
    private Class clsForm;
    private int mKind = 0;
    private boolean isClick = false;
    private ProgressDialog progress;

    @Override
    protected int getViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initView();

        String initData = pref.getStringValue("", Constant.JSON_MAPNAME_NAME);
        if (initData.equals("") || !initData.equals(Constant.KEY_UPDATE)) {
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.msg_now_loading));
            progress.setProgressStyle(progress.STYLE_HORIZONTAL);
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);
            isLoading = true;

            new CreateInitData().execute();
        }else
            isLoading = false;

    }

    private void initView() {

        setListenerView(R.id.btnSearch, this);
        setListenerView(R.id.btnFruit, this);
        setListenerView(R.id.btnRecognize, this);
        setListenerView(R.id.btnFood, this);
        setListenerView(R.id.btnAlphabet, this);
        setListenerView(R.id.btnNumber, this);
        setListenerView(R.id.btnColor, this);
        setListenerView(R.id.btnAnimal, this);
        setListenerView(R.id.btnBody, this);
//        setListenerView(R.id.btnFurniture, this);
        setListenerView(R.id.btnOther, this);
        setListenerView(R.id.btnDialog, this);
        setListenerView(R.id.btnPractice, this);
        setListenerView(R.id.btnGrammar, this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFruit:
                moveToForm(LearnWordsActivity.class, 1);
                break;
            case R.id.btnRecognize:
                moveToForm(RecognizeMainActicity.class, 0);
                break;
            case R.id.btnFood:
                moveToForm(LearnFoodActivity.class, 3);
                break;
            case R.id.btnAlphabet:
                moveToForm(AlphabetActivity.class);
                break;
            case R.id.btnNumber:
                moveToForm(NumberActivity.class);
                break;
            case R.id.btnColor:
                moveToForm(ColorActivity.class);
                break;
            case R.id.btnAnimal:
                moveToForm(LearnWordsActivity.class, 4);
                break;
            case R.id.btnBody:
                moveToForm(BodyActivity.class);
                break;
//            case R.id.btnFurniture:
//                moveToForm(LearnWordsActivity.class, 5);
//                break;
            case R.id.btnOther:
                moveToForm(LearnWordsActivity.class, 12);
                break;
            case R.id.btnDialog:
                moveToForm(PhrasesActivity.class);
                break;
            case R.id.btnPractice:
                moveToForm(PracticeActivity.class);
                break;
            case R.id.btnGrammar:
                moveToForm(GrammarActivity.class);
                break;
//		case R.id.btnSearch:
//			startActivity2(SearchAllActivity.class);
//			break;		
        }

        super.onClick(v);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isClick = false;
        Locale current = this.getResources().getConfiguration().locale;
        ULog.i(MainActivity.class, "resume ========================= lang:" + current.toString().toLowerCase());
    }

    @Override
    public void onBackPressed() {
        if (!isLoading) {
            Utility.confirmCloseApp(MainActivity.this);
//            super.onBackPressed();
        }
    }

    private void moveToForm(Class cls) {
        moveToForm(cls, 0);
    }

    private void moveToForm(Class cls, int kind) {
        if (isClick)
            return;
        isClick = true;
        if (isLoading) {
            clsForm = cls;
            mKind = kind;
            if (progress != null)
                progress.show();
        } else {
            startActivity2(cls, kind);
        }
    }

    private class CreateInitData extends AsyncTask<Void, Void, Boolean> {
        private String initData = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ULog.i(CreateInitData.this, "onPreExecute loading........................");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                ULog.i(CreateInitData.this, "Loading....");
                initData = pref.getStringValue("", Constant.JSON_MAPNAME_NAME);
                if (initData.equals("") || !initData.equals(Constant.KEY_UPDATE)) {
                    insertData();
                } else {
                    ULog.i(SplashActivity.class, "Don't insert map");
                }

            } catch (Exception e) {
                ULog.e(SplashActivity.class, "load data fail");
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                pref.putStringValue(Constant.KEY_UPDATE, Constant.JSON_MAPNAME_NAME);
            }
            if (!isFinishing() && progress != null && progress.isShowing())
                progress.dismiss();

            if (clsForm != null)
                startActivity2(clsForm, mKind);
            clsForm = null;
            mKind = 0;
            isLoading = false;

        }
    }

    private void insertData() {
        DaoMaster daoMaster;
        MapNameEntity mapName;
        try {
            daoMaster = ((MyApplication) getApplicationContext()).daoMaster;
            DaoSession mDaoSession = daoMaster.newSession();
            if (Constant.isMyDebug) {
                mapName = (MapNameEntity) Common.getObjectJson(this, MapNameEntity.class, Constant.JSON_MAPNAME_NAME);
            } else {
                mapName = (MapNameEntity) Common.getDataDecrypt(this, MapNameEntity.class, Constant.JSON_MAPNAME_NAME);
            }

            if (mapName == null) {
                ULog.e(SplashActivity.class, "Can't load Json");
                return;
            }

            ULog.i(this, "===== map name size data :" + mapName.listData.size());
            progress.setMax(mapName.listData.size());
            int count=0;
            for (tblMapName entity : mapName.listData) {
                count++;
                // ULog.i(this, "===== Insert data :" + entity.getAlphabet());
                mDaoSession.insertOrReplace(entity);
                progress.setProgress(count);
            }


        } catch (Exception e) {
            ULog.e(SplashActivity.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }
}
