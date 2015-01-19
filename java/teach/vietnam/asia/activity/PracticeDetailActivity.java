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

import de.greenrobot.dao.query.QueryBuilder;
import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.PracticeFooterAdapter;
import teach.vietnam.asia.adapter.PracticePagerAdapter;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.DaoSession;
import teach.vietnam.asia.entity.tblViet;
import teach.vietnam.asia.entity.tblVietDao;
import teach.vietnam.asia.sound.AudioPlayer;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;

public class PracticeDetailActivity extends BaseActivity implements OnClickListener {

    private ViewPager pagerExceriese;
    private GridView gridPage;
    public TextView tvAns;
    private PracticePagerAdapter adapterPage;
    private PracticeFooterAdapter adapterFooter;
    private tblVietDao dao;
    private DaoMaster daoMaster;
    private ProgressDialog progressDialog;
    private AudioPlayer audio;
    private int currPage = 0;
    private int kind = 1;
    private int level = 1;
    private int max_level = 1;
    private String lang;

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
        setInitData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgSpeak:
                ULog.i(PracticeDetailActivity.class, "onClick" + adapterPage.lstData.get(currPage).getVi());
                // audio.playSound(adapterPage.lstData.get(currPage).getAudio());
                audio.speakWord(adapterPage.lstData.get(currPage).getVi());
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

    private void setInitData() {
        kind = getIntent().getIntExtra(Constant.INTENT_KIND, 1);
        lang = PracticeDetailActivity.this.getString(R.string.language);
        ULog.i(PracticeDetailActivity.class, "setInitData kind:" + kind);
        audio = new AudioPlayer(PracticeDetailActivity.this);
        pagerExceriese.setPageMargin(-80);
        pagerExceriese.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currPage = position;
                ULog.i(PracticeDetailActivity.class, "PageSelected Word:" + adapterPage.lstData.get(currPage).getVi());

                if (Constant.isPro) {
                    audio.speakWord(adapterPage.lstData.get(currPage).getVi());
                    tvAns.setVisibility(View.INVISIBLE);
                } else {
                    tvAns.setText(adapterPage.lstData.get(currPage).getVi());
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
                new LoadData().execute();
            }
        });
        new LoadData().execute();
        new LoadLevel().execute();
    }

    private class LoadData extends AsyncTask<Void, Void, List<tblViet>> {

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
        protected List<tblViet> doInBackground(Void... params) {
            QueryBuilder<tblViet> qb;

            try {


                daoMaster = ((MyApplication) getApplication()).daoMaster;
                dao = daoMaster.newSession().getTblVietDao();
                qb = dao.queryBuilder();
//                qb.where(tblVietDao.Properties.Kind.eq(kind), tblVietDao.Properties.Level.eq(level));
                if (lang.equals("ja")) {
                    qb.where(tblVietDao.Properties.Kind.eq(kind), tblVietDao.Properties.Level.eq(level), tblVietDao.Properties.Ja.notEq(""));
                }else if (lang.equals("ko")) {
                    qb.where(tblVietDao.Properties.Kind.eq(kind), tblVietDao.Properties.Level.eq(level), tblVietDao.Properties.Ko.notEq(""));
                }else{
                    qb.where(tblVietDao.Properties.Kind.eq(kind), tblVietDao.Properties.Level.eq(level),tblVietDao.Properties.En.notEq(""));
                }
                qb.orderAsc(tblVietDao.Properties.Level);
                ULog.i(this, "===data db:" + qb.list().size());

            } catch (Exception e) {
                ULog.e(PracticeDetailActivity.class, "load data error:" + e.getMessage());
                return null;
            }
            return qb.list();
        }

        @Override
        protected void onPostExecute(List<tblViet> lstData) {
            super.onPostExecute(lstData);
            try {
                if (!isFinishing()) {
                    progressDialog.dismiss();
                }
                if (lstData != null && lstData.size() > 0) {
                    ULog.i(PracticeDetailActivity.this, "image:" + lstData.get(0).getImg() + "; size:" + lstData.size());
                    adapterPage = new PracticePagerAdapter(PracticeDetailActivity.this, lstData);
                    pagerExceriese.setAdapter(adapterPage);
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

    public int getMaxLevel(DaoSession session) {
        String cond;

        // lstNumberEx = new ArrayList<TblNumberEx>();
        cond = "SELECT MAX(level) FROM  " + tblVietDao.TABLENAME + " WHERE " + tblVietDao.Properties.Kind.columnName + "=" + kind;

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

}
