package teach.vietnam.asia.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;

import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.MenuRecognizeAdapter;
import teach.vietnam.asia.db.DBDataRecognize;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.DaoSession;
import teach.vietnam.asia.fragment.BaseFragment;
import teach.vietnam.asia.fragment.LearnRecoginzeFragment;
import teach.vietnam.asia.fragment.TestRecognizeFragment;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;
import teach.vietnam.asia.view.MainMenuLayout;

public class RecognizeMainActicity extends BaseActivity implements BaseFragment.OnFragmentInteractionListener, FragmentManager.OnBackStackChangedListener, DBDataRecognize.IFinishSave {

    public ProgressDialog progressDialog;
    private boolean mShowingBack = false;
    private DaoMaster daoMaster;
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

        Utility.setScreenNameGA("RecognizeMainActicity - lang:" + Locale.getDefault().getLanguage());

    }

    @Override
    public void onBackPressed() {
        if (menuLayout != null && menuLayout.isMenuShown()) {
            menuLayout.toggleMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new DBDataRecognize(this, this).execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    protected void reloadData() {
        new LoadData().execute();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQ_CODE_SPEECH_INPUT:
                if (resultCode == RESULT_OK && null != data) {

                    Fragment fragment;
                    fragment = getFragmentManager().findFragmentById(R.id.container);
                    if (fragment != null && fragment instanceof LearnRecoginzeFragment) {
                        ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        ((LearnRecoginzeFragment) fragment).setTextVoid(result.get(0));
                    }
                }
                break;
        }
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
        if (fragment != null && fragment instanceof LearnRecoginzeFragment) {
            ((LearnRecoginzeFragment) fragment).setCurrentPage(number);
        } else {
            onFragmentInteraction(LearnRecoginzeFragment.class, number);
            ULog.i(RecognizeMainActicity.class, "Test: " + number);
        }
        hideMenu();
    }

    @Override
    public void isFinish(boolean b) {
        if (b) {
            new LoadData().execute();
        } else {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }

    //get amount pager
    private class LoadData extends AsyncTask<Void, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            if (progressDialog == null) {
//                progressDialog = new ProgressDialog(RecognizeMainActicity.this);
//                progressDialog.setCancelable(false);
//                progressDialog.setCanceledOnTouchOutside(false);
//            }
//
//            if (!isFinishing() && !progressDialog.isShowing()) {
//                progressDialog.show();
//                ULog.i(RecognizeMainActicity.class, "LoadData progress...");
//            }
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
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                    ULog.i(RecognizeMainActicity.class, "hide  progress...");
//                }

                if (isFinishing()) {
                    return;
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
            cond = "SELECT GROUP_CONCAT(VN) FROM  " + Utility.getRecTableName(lang) + " GROUP BY " + Utility.getREC_GroupID(lang).name;
            ULog.i(RecognizeMainActicity.class, "SQL: " + cond);
            lstData = new ArrayList<String>();
            try {
                Cursor c = session.getDatabase().rawQuery(cond, null);
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

}
