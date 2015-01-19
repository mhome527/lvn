package teach.vietnam.asia.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import teach.vietnam.asia.BuildConfig;
import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.MenuRecognizeAdapter;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.DaoSession;
import teach.vietnam.asia.entity.RecognizeEntity;
import teach.vietnam.asia.entity.tblRecognize;
import teach.vietnam.asia.entity.tblRecognizeDao;
import teach.vietnam.asia.fragment.BaseFragment;
import teach.vietnam.asia.fragment.LearnRecoginzeFragment;
import teach.vietnam.asia.fragment.TestRecognizeFragment;
import teach.vietnam.asia.utils.Common;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.view.MainMenuLayout;

public class RecognizeMainActicity extends BaseActivity implements BaseFragment.OnFragmentInteractionListener, FragmentManager.OnBackStackChangedListener {

    private boolean mShowingBack = false;
    private DaoMaster daoMaster;
    private ProgressDialog progress;
    private Bundle savedInstanceState;
    private int amount = 0;
    private MainMenuLayout menuLayout;
    private ListView lstRecognize;
    private ArrayList<String> lstData;

    @Override
    protected int getViewLayoutId() {
//        return R.layout.activity_recognize_main_acticity;
        return R.layout.recognize_main_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ULog.i(RecognizeMainActicity.class, "initView");
        this.savedInstanceState = savedInstanceState;

        menuLayout = getViewChild(R.id.menuLayout);
        lstRecognize = getViewChild(R.id.lstRecognize);


        // Monitor back stack changes to ensure the action bar shows the appropriate button (either "photo" or "info").
        getFragmentManager().addOnBackStackChangedListener(this);

        /////
        setInitData();
    }

    @Override
    public void onBackPressed() {
        if (menuLayout != null && menuLayout.isMenuShown()) {
            menuLayout.toggleMenu();
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public void onClick(View v) {
//
//        switch(v.getId()){
//
//            default:
//                super.onClick(v);
//        }
//    }

    @Override
    public void onFragmentInteraction(Class cls, int currPage) {

        Fragment fr;
        if (mShowingBack) {
            getFragmentManager().popBackStack();
            return;
        }
        // Flip to the back.
        mShowingBack = true;
        fr = getFragment(cls, currPage);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.card_flip_left_in, R.anim.card_flip_left_out, R.anim.card_flip_right_in, R.anim.card_flip_right_out);
        ft.replace(R.id.container, fr);
        // Add this transaction to the back stack, allowing users to press Back to get to the front of the card.
        ft.addToBackStack(null);
        ft.commit();
    }

    private Fragment getFragment(Class cls, int currPage) {
        try {
            if (cls.isAssignableFrom(LearnRecoginzeFragment.class)) {
                return LearnRecoginzeFragment.newInstance(amount, currPage);
            } else {
                return TestRecognizeFragment.newInstance(currPage);
            }
        } catch (Exception e) {
            ULog.e(RecognizeMainActicity.class, "getFragment Error:" + e.getMessage());
        }
        return null;
    }

    @Override
    public void onBackStackChanged() {
        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
    }

    public void showMenu() {
        menuLayout.toggleMenu();
    }

    public void hideMenu() {
        if (menuLayout != null && menuLayout.isMenuShown()) {
            menuLayout.toggleMenu();
        }
    }

    private void setInitData() {
        String initData = pref.getStringValue("", Constant.JSON_RECOGNIZE_NAME);
        ULog.i(RecognizeMainActicity.class, "setInit: " + initData);
        if (initData.equals("") || !initData.equals(Constant.KEY_UPDATE)) {
            progress = new ProgressDialog(this);
            progress.setMessage(getString(R.string.msg_now_loading));
            progress.setProgressStyle(progress.STYLE_HORIZONTAL);
            progress.setCancelable(false);
            progress.setCanceledOnTouchOutside(false);

            new CreateInitData().execute();
        } else
            new LoadData().execute();

        lstRecognize.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                setPageLearn(pos);
            }
        });

    }

    private void setPageLearn(int number) {
        Fragment fragment;
        fragment = getFragmentManager().findFragmentById(R.id.container);
        if (fragment!=null && fragment instanceof LearnRecoginzeFragment) {
            ((LearnRecoginzeFragment) fragment).setCurrentPage(number);
        }else{
            onFragmentInteraction(LearnRecoginzeFragment.class,  number);
            ULog.i(RecognizeMainActicity.class, "Test: " + number);
        }
        hideMenu();
    }

    //get amount pager
    private class LoadData extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progress == null) {
                progress = new ProgressDialog(RecognizeMainActicity.this);
                progress.setCancelable(false);
                progress.setCanceledOnTouchOutside(false);
            }

            if (!isFinishing()) {
                progress.show();
                ULog.i(RecognizeMainActicity.class, "LoadData progress...");
            }
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                daoMaster = ((MyApplication) getApplication()).daoMaster;
                return getRecognizeData(daoMaster.newSession());

            } catch (Exception e) {
                ULog.e(RecognizeMainActicity.class, "load data error:" + e.getMessage());
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer num) {
            super.onPostExecute(num);
            try {
                if (!isFinishing()) {
                    progress.dismiss();
                }
                if (num > 0) {
                    MenuRecognizeAdapter adapter = new MenuRecognizeAdapter(RecognizeMainActicity.this, lstData);
                    lstRecognize.setAdapter(adapter);

                    if (savedInstanceState == null) {
                        ULog.i(RecognizeMainActicity.this, "Load data, num:" + num);
                        int currPage = pref.getIntValue(0, Constant.PREF_PAGE);

                        getFragmentManager().beginTransaction().add(R.id.container, LearnRecoginzeFragment.newInstance(num, currPage)).commit();
                    } else {
                        mShowingBack = (getFragmentManager().getBackStackEntryCount() > 0);
                    }

                    amount = num;
                }
            } catch (Exception e) {
                ULog.e(RecognizeMainActicity.class, "onPostExecute Error: " + e.getMessage());
            }
        }

        public int getRecognizeData(DaoSession session) {
            String cond;
            int count = 1;
            // lstNumberEx = new ArrayList<TblNumberEx>();
//            cond = "SELECT MAX(" + tblRecognizeDao.Properties.Group_id.name + ") FROM  " + tblRecognizeDao.TABLENAME;
            cond = "SELECT GROUP_CONCAT(VN) FROM  " + tblRecognizeDao.TABLENAME + " GROUP BY " + tblRecognizeDao.Properties.Group_id.name;
            ULog.i(RecognizeMainActicity.class, "SQL: " + cond);
            lstData = new ArrayList<String>();
            try {
                Cursor c = session.getDatabase().rawQuery(cond, null);
//                if (c.moveToFirst()) {
//                    level = c.getInt(0);
//                    ULog.i(RecognizeMainActicity.class, "getLevel max level:" + level);
//                }

                while (c.moveToNext()) {
                    lstData.add(count++ + ". " + c.getString(0).replace(",", " - "));
                }

                c.close();
            } catch (Exception e) {
                ULog.e(RecognizeMainActicity.class, "getListNumber Error:" + e.getMessage());
                return 0;
            }
            return lstData.size();
        }

    }

    /// Create data
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
                initData = pref.getStringValue("", Constant.JSON_RECOGNIZE_NAME);
                if (initData.equals("") || !initData.equals(Constant.KEY_UPDATE)) {
                    return insertData();
                } else {
                    ULog.i(RecognizeMainActicity.class, "Don't insert map");
                }

            } catch (Exception e) {
                ULog.e(RecognizeMainActicity.class, "load data fail");
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                pref.putStringValue(Constant.KEY_UPDATE, Constant.JSON_RECOGNIZE_NAME);
            }
            if (!isFinishing() && progress != null && progress.isShowing())
                progress.dismiss();

            new LoadData().execute();
        }
    }

    private boolean insertData() {
        DaoMaster daoMaster;
        RecognizeEntity recognizeEntity;
        try {
            daoMaster = ((MyApplication) getApplicationContext()).daoMaster;
            DaoSession mDaoSession = daoMaster.newSession();
//            if (Constant.isMyDebug) {
                recognizeEntity = (RecognizeEntity) Common.getObjectJson(this, RecognizeEntity.class, Constant.JSON_RECOGNIZE_NAME);
//            } else {
//                recognizeEntity = (RecognizeEntity) Common.getDataDecrypt(this, RecognizeEntity.class, Constant.JSON_RECOGNIZE_NAME);
//            }

            if (recognizeEntity == null) {
                ULog.e(RecognizeMainActicity.class, "Can't load Json");
                return false;
            }

            ULog.i(this, "===== map name size data :" + recognizeEntity.listData.size());
            progress.setMax(recognizeEntity.listData.size());
            int count = 0;
            for (tblRecognize entity : recognizeEntity.listData) {
                count++;
                // ULog.i(this, "===== Insert data :" + entity.getAlphabet());
                mDaoSession.insertOrReplace(entity);
                progress.setProgress(count);
            }


        } catch (Exception e) {
            ULog.e(RecognizeMainActicity.class, "Insert error:" + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
            return false;
        }
        return true;
    }
}
