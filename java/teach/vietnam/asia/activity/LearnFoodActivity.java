package teach.vietnam.asia.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import java.util.List;
import java.util.Locale;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;
import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.LearnFoodAdapter;
import teach.vietnam.asia.sound.AudioPlayer;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

@SuppressWarnings("deprecation")
public class LearnFoodActivity extends BaseActivity implements ViewFactory, OnClickListener {

    private Gallery gallery;
    private ImageSwitcher selectedImage;
//	private tblVietDao dao;
    // private ImageButton imgVolume;

    //	private DaoMaster daoMaster;
    private ProgressDialog progressDialog;
    private AudioPlayer audio;
    private TextView tvFood;
    private List lstData;
    private String lang = "";

    @Override
    protected int getViewLayoutId() {
        return R.layout.learn_food_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        // viewPager = getViewChild(R.id.pagerFruit);
        gallery = getViewChild(R.id.gallery1);
        tvFood = getViewChild(R.id.tvFood);
        setListenerView(R.id.btnSpeak, this);
        // imgVolume = getViewChild(R.id.imgVolume);

        setInitData();

        Utility.setScreenNameGA("LearnFoodActivity - lang:" + Locale.getDefault().getLanguage());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSpeak:
                if (Constant.isPro)
                    speakWord();
                else
                    Utility.installPremiumApp(LearnFoodActivity.this);
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

//        String tmp;
//        tmp = LearnFoodActivity.this.getString(R.string.language);
//        if (lang.equals("") || !lang.equals(tmp)) {
//            lang = tmp;
//
//        }
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

    @Override
    public View makeView() {
        ImageView iView = new ImageView(this);
        iView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        iView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        iView.setBackgroundColor(0xFFffffff);

        return iView;
    }

    private void setInitData() {
        audio = new AudioPlayer(LearnFoodActivity.this);
        // imgVolume.setOnClickListener(this);
        // adapter = new FruitPagerAdapter(this, mImageIds);
        // viewPager.setAdapter(adapter);
        // pagerFruit
        selectedImage = (ImageSwitcher) findViewById(R.id.selectedImage);
        selectedImage.setFactory(this);
        selectedImage.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        selectedImage.setOutAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));

        gallery.setSpacing(5);
        // gallery.setAdapter(new LearnAdapter(this));

        // clicklistener for Gallery
        gallery.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                int resourceId = 0;
                tvFood.setText(Utility.getVi(lstData.get(position), lang));
                // Toast.makeText(FruitActivity.this, "Your selected position = " + position, Toast.LENGTH_SHORT).show();
                // show the selected Image
                ImageView img = (ImageView) v.findViewById(R.id.imgFruit);
                try {
                    resourceId = Integer.parseInt(img.getTag().toString());

                } catch (Exception e) {
                    ULog.e(LearnFoodActivity.class, "onItemClick get resource error:" + e.getMessage());
                    resourceId = 0;
                }
                if (resourceId > 0) {
                    selectedImage.setImageResource(resourceId);
                } else
                    ULog.e(LearnFoodActivity.class, "dont load resource");

                if (Constant.isPro)
                    speakWord();
                // selectedImage.setImageResource(mImageIds[position]);
            }
        });
    }

    private void speakWord() {
        audio.speakWord(tvFood.getText().toString().trim());
    }

    private class LoadData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(LearnFoodActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }

            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            QueryBuilder qb;
            AbstractDao dao;

            try {
//				daoMaster = ((MyApplication) getApplication()).daoMaster;
//				dao = daoMaster.newSession().getTblVietDao();
//				qb = dao.queryBuilder();
//
//				qb.where(tblVietDao.Properties.Kind.eq(3));

                dao = Utility.getDao(LearnFoodActivity.this, lang);
                qb = dao.queryBuilder();
                qb.where(Utility.getKind(lang).eq(3));
                // qb.orderDesc(tblVietDao.Properties.Show);
                ULog.i(this, "===data db:" + qb.list().size());
                lstData = qb.list();
            } catch (Exception e) {
                ULog.e(LearnFoodActivity.class, "load data error:" + e.getMessage());
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);
            int resourceId;


            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

            if (isFinishing()) {
                return;
            }
            if (lstData != null && lstData.size() > 0) {
                resourceId = Utility.getResourcesID(LearnFoodActivity.this, Utility.getImg(lstData.get(0), lang));
                if (resourceId > 0) {
                    selectedImage.setImageResource(resourceId);
                } else
                    ULog.e(LearnFoodActivity.class, "dont load resource");

                gallery.setAdapter(new LearnFoodAdapter(LearnFoodActivity.this, lstData, lang));
                gallery.setSelection(2);
                tvFood.setText(Utility.getVi(lstData.get(0), lang));
            }else{
//                startActivity2(MainActivity.class);
                finish();
            }
        }

    }

}
