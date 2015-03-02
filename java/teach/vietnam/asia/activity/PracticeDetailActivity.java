package teach.vietnam.asia.activity;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;
import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.PracticeFooterAdapter;
import teach.vietnam.asia.adapter.PracticePagerAdapter;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.DaoSession;
import teach.vietnam.asia.entity.tblVietENDao;
import teach.vietnam.asia.sound.AudioPlayer;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

public class PracticeDetailActivity extends BaseActivity implements OnClickListener {

    public TextView tvAns;
    private ViewPager pagerExceriese;
    private GridView gridPage;
    private PracticePagerAdapter adapterPage;
    private PracticeFooterAdapter adapterFooter;
    private tblVietENDao dao;
    private DaoMaster daoMaster;
    private ProgressDialog progressDialog;
    private AudioPlayer audio;
    private int currPage = 0;
    private int kind = 1;
    private int level = 1;
    private int max_level = 1;
//    private String lang;

    @Override
    protected int getViewLayoutId() {
        return R.layout.practice_detail_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        pagerExceriese = getViewChild(R.id.pagerExceriese);
        gridPage = getViewChild(R.id.gridPage);
        tvAns = getViewChild(R.id.tvAns);

        tvAns.setVisibility(View.INVISIBLE);

        setListenerView(R.id.imgSpeak, this);
//        setInitData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgSpeak:
//                ULog.i(PracticeDetailActivity.class, "onClick" + adapterPage.lstData.get(currPage).getVi());
                if (Constant.isPro)
                    audio.speakWord(Utility.getVi(adapterPage.lstData.get(currPage), lang));
                else
                    Utility.installPremiumApp(PracticeDetailActivity.this);
                break;
        }
        super.onClick(v);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (audio != null)
            audio.stopAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        lang = PracticeDetailActivity.this.getString(R.string.language);
        setInitData();
    }

    @Override
    protected void reloadData() {

    }

    private void setInitData() {
        kind = getIntent().getIntExtra(Constant.INTENT_KIND, 1);

        ULog.i(PracticeDetailActivity.class, "setInitData kind:" + kind);
        audio = new AudioPlayer(PracticeDetailActivity.this);
        pagerExceriese.setPageMargin(-80);
        pagerExceriese.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currPage = position;
//                ULog.i(PracticeDetailActivity.class, "PageSelected Word:" + adapterPage.lstData.get(currPage).getVi());

                if (Constant.isPro) {
                    audio.speakWord(Utility.getVi(adapterPage.lstData.get(currPage), lang));
                    tvAns.setVisibility(View.INVISIBLE);
                } else {
                    tvAns.setText(Utility.getVi(adapterPage.lstData.get(currPage), lang));
                    tvAns.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        gridPage.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // viewPager.setCurrentItem(position, true);
                if (adapterFooter.currentPage == position)
                    return;
                currPage = 0;
                adapterFooter.currentPage = position;
                adapterFooter.notifyDataSetChanged();
                level = position + 1;
                tvAns.setText("");
                new LoadData().execute();
            }
        });
        new LoadData().execute();
        new LoadLevel().execute();
    }

    public int getMaxLevel(DaoSession session) {
        String cond;

        // lstNumberEx = new ArrayList<TblNumberEx>();
        cond = "SELECT MAX(level) FROM  " + Utility.getTableName(lang) + " WHERE " + Utility.getKind(lang).columnName + "=" + kind;

        try {
            Cursor c = session.getDatabase().rawQuery(cond, null);
            if (c.moveToFirst()) {
                level = c.getInt(0);
                ULog.i(PracticeDetailActivity.class, "getLevel max level:" + level);
            }
            c.close();
        } catch (Exception e) {
            ULog.e(PracticeDetailActivity.class, "getListNumber Error:" + e.getMessage());
        }
        return level;
    }

    private class LoadData extends AsyncTask<Void, Void, List> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(PracticeDetailActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }

            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected List doInBackground(Void... params) {
            QueryBuilder qb;
            AbstractDao dao;
            try {

                dao = Utility.getDao(PracticeDetailActivity.this, lang);
                qb = dao.queryBuilder();
                qb.where(Utility.getKind(lang).eq(kind), Utility.getLevel(lang).eq(level), Utility.getO1(lang).notEq(""));
                qb.orderAsc(Utility.getLevel(lang));
                ULog.i(this, "===data db:" + qb.list().size());

            } catch (Exception e) {
                ULog.e(PracticeDetailActivity.class, "load data error:" + e.getMessage());
                return null;
            }
            return qb.list();
        }

        @Override
        protected void onPostExecute(List lstData) {
            super.onPostExecute(lstData);
            try {
                if (!isFinishing()) {
                    progressDialog.dismiss();
                }
                if (lstData != null && lstData.size() > 0) {
//                    ULog.i(PracticeDetailActivity.this, "image:" + lstData.get(0).getImg() + "; size:" + lstData.size());
                    adapterPage = new PracticePagerAdapter(PracticeDetailActivity.this, lstData, lang);
                    pagerExceriese.setAdapter(adapterPage);
                    if (!Constant.isPro)
                        tvAns.setText(Utility.getVi(adapterPage.lstData.get(0), lang));
                }
            } catch (Exception e) {
                ULog.e(PracticeDetailActivity.class, "onPostExecute Error: " + e.getMessage());
            }
        }

    }

    private class LoadLevel extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                daoMaster = ((MyApplication) getApplication()).daoMaster;
                max_level = getMaxLevel(daoMaster.newSession());
            } catch (Exception e) {
                ULog.e(PracticeDetailActivity.class, "load data error:" + e.getMessage());
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                // if (!isFinishing()) {
                // progressDialog.dismiss();
                // }
                if (max_level > 0) {
                    adapterFooter = new PracticeFooterAdapter(PracticeDetailActivity.this, max_level);
                    gridPage.setAdapter(adapterFooter);
                    gridPage.setLayoutParams(new LinearLayout.LayoutParams(120 * max_level, LinearLayout.LayoutParams.WRAP_CONTENT));
                    gridPage.setNumColumns(max_level);

                }
//                else {
//                    PracticeDetailActivity.this.setVisibilityView(R.id.llLevel, false);
//                }
            } catch (Exception e) {
                ULog.e(PracticeDetailActivity.class, "onPostExecute Error: " + e.getMessage());
            }
        }

    }

}
