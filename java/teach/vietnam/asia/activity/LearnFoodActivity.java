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

import de.greenrobot.dao.query.QueryBuilder;
import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.LearnFoodAdapter;
import teach.vietnam.asia.adapter.LearnPagerAdapter;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.tblViet;
import teach.vietnam.asia.entity.tblVietDao;
import teach.vietnam.asia.sound.AudioPlayer;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

@SuppressWarnings("deprecation")
public class LearnFoodActivity extends BaseActivity implements ViewFactory, OnClickListener {

	private Gallery gallery;
	private ImageSwitcher selectedImage;
	private tblVietDao dao;
	// private ImageButton imgVolume;

	private DaoMaster daoMaster;
	private ProgressDialog progressDialog;
	private AudioPlayer audio;
	private TextView tvFood;
	private List<tblViet> lstData;

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
		new LoadData().execute();
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
        if(audio !=null)
            audio.stopAll();
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
				tvFood.setText(lstData.get(position).getVi());
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
					ULog.e(LearnPagerAdapter.class, "dont load resource");

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
			QueryBuilder<tblViet> qb;
			try {
				daoMaster = ((MyApplication) getApplication()).daoMaster;
				dao = daoMaster.newSession().getTblVietDao();
				qb = dao.queryBuilder();

				qb.where(tblVietDao.Properties.Kind.eq(3));
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
			if (!isFinishing()) {
				progressDialog.dismiss();
			}
			if (lstData != null && lstData.size() > 0) {
				resourceId = Utility.getResourcesID(LearnFoodActivity.this, lstData.get(0).getImg());
				if (resourceId > 0) {
					selectedImage.setImageResource(resourceId);
				} else
					ULog.e(LearnPagerAdapter.class, "dont load resource");

				gallery.setAdapter(new LearnFoodAdapter(LearnFoodActivity.this, lstData));
				gallery.setSelection(2);
				tvFood.setText(lstData.get(0).getVi());
			}
		}

	}

}
