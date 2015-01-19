package teach.vietnam.asia.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import de.greenrobot.dao.query.QueryBuilder;
import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.LearnWordAdapter;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.tblViet;
import teach.vietnam.asia.entity.tblVietDao;
import teach.vietnam.asia.sound.AudioPlayer;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

public class LearnWordsActivity extends BaseActivity implements OnClickListener {

    private LearnWordAdapter adapter;
    private tblVietDao dao;
    private GridView gridWord;
    private TextView tvViet;
    private TextView tvOther;

    private DaoMaster daoMaster;
    private ProgressDialog progressDialog;
    private AudioPlayer audio;
    private int kind = 1;
    private int kind2 = -1;
    private List<tblViet> lstData;

    @Override
    protected int getViewLayoutId() {
        return R.layout.learn_word_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        gridWord = getViewChild(R.id.gridWord);
        tvViet = getViewChild(R.id.tvViet);
        tvOther = getViewChild(R.id.tvOther);
        setListenerView(R.id.btnSpeak, this);
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
                    Utility.installPremiumApp(LearnWordsActivity.this);
                // ULog.i(LearnWordsActivity.class, "onClick NOT PREMIUM");
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
    }

    private void speakWord() {
        audio.speakWord(tvViet.getText().toString());
    }

//	private void speakWord(String strNumber) {
//		int count = 0;
//		String soundName;
//
//		if (strNumber.equals(""))
//			return;
//
//		String[] strSound = strNumber.split(" ");
//
//		for (String name : strSound) {
//			soundName = Common.getNameSound(name);
//			ULog.i(NumberActivity.class, "speakNumber" + soundName);
//			if (!soundName.equals(""))
//				strSound[count] = "sound/" + soundName + ".mp3";
//			count++;
//		}
//		ULog.i(NumberActivity.class, "audioAll:" + strSound);
//
//		if (strSound.length > 0)
//			audio.playSound(strSound);
//	}

    private void setInitData() {
        audio = new AudioPlayer(LearnWordsActivity.this);
        kind = getIntent().getIntExtra(Constant.INTENT_KIND, 1);
        gridWord.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
                if (Locale.getDefault().getLanguage().equals("ja")) {
                    tvViet.setText(lstData.get(postion).getVi());
                    tvOther.setText(lstData.get(postion).getJa());
                } else if (Locale.getDefault().getLanguage().equals("ko")) {
                    tvViet.setText(lstData.get(postion).getVi());
                    tvOther.setText(lstData.get(postion).getKo());
                } else {
                    tvViet.setText(lstData.get(postion).getVi());
                    tvOther.setText(lstData.get(postion).getEn());
                }
                if (Constant.isPro)
                    speakWord();
                else
                    ULog.i(LearnWordsActivity.class, "onItemClick NOT PREMIUM");
            }
        });

    }

    private class LoadData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(LearnWordsActivity.this);
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
            String lang;
            try {
                daoMaster = ((MyApplication) getApplication()).daoMaster;
                dao = daoMaster.newSession().getTblVietDao();
                qb = dao.queryBuilder();
                lang = LearnWordsActivity.this.getString(R.string.language);
//                if (lang.equals("ja")) {
//                    qb.where(tblVietDao.Properties.Ja.notEq(""));
//                }else if (lang.equals("ko")) {
//                    qb.where(tblVietDao.Properties.Ko.notEq(""));
//                }else{
//                    qb.where(tblVietDao.Properties.En.notEq(""));
//                }
//
//                if(kind==12) {
//                    qb.whereOr(tblVietDao.Properties.Kind.eq(kind), tblVietDao.Properties.Kind.eq(5));
//                }else{
//                    qb.where(tblVietDao.Properties.Kind.eq(kind));
//                }

                if (kind == 12) {
                    if (lang.equals("ja")) {
                        qb.where(tblVietDao.Properties.Ja.notEq(""), qb.or(tblVietDao.Properties.Kind.eq(kind),
                                tblVietDao.Properties.Kind.eq(5)));
                    } else if (lang.equals("ko")) {
                        qb.where(tblVietDao.Properties.Ko.notEq(""), qb.or(tblVietDao.Properties.Kind.eq(kind),
                                tblVietDao.Properties.Kind.eq(5)));
                    } else {
                        qb.where(tblVietDao.Properties.En.notEq(""), qb.or(tblVietDao.Properties.Kind.eq(kind),
                                tblVietDao.Properties.Kind.eq(5)));
                    }
                } else if (kind == 1) {
                    if (lang.equals("ja")) {
                        qb.where(tblVietDao.Properties.Ja.notEq(""), qb.or(tblVietDao.Properties.Kind.eq(kind),
                                tblVietDao.Properties.Kind.eq(2)));
                    } else if (lang.equals("ko")) {
                        qb.where(tblVietDao.Properties.Ko.notEq(""), qb.or(tblVietDao.Properties.Kind.eq(kind),
                                tblVietDao.Properties.Kind.eq(2)));
                    } else {
                        qb.where(tblVietDao.Properties.En.notEq(""), qb.or(tblVietDao.Properties.Kind.eq(kind),
                                tblVietDao.Properties.Kind.eq(2)));
                    }
                } else {
                    if (lang.equals("ja")) {
                        qb.where(tblVietDao.Properties.Ja.notEq(""), tblVietDao.Properties.Kind.eq(kind));
                    } else if (lang.equals("ko")) {
                        qb.where(tblVietDao.Properties.Ko.notEq(""), tblVietDao.Properties.Kind.eq(kind));
                    } else {
                        qb.where(tblVietDao.Properties.En.notEq(""), tblVietDao.Properties.Kind.eq(kind));
                    }

                }

                ULog.i(this, "===data db:" + qb.list().size());
                lstData = qb.list();
            } catch (Exception e) {
                ULog.e(LearnWordsActivity.class, "load data error:" + e.getMessage());
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void param) {
            super.onPostExecute(param);

            if (!isFinishing()) {
                progressDialog.dismiss();
            }
            if (lstData != null && lstData.size() > 0) {
                adapter = new LearnWordAdapter(LearnWordsActivity.this, lstData);
                gridWord.setAdapter(adapter);
                if (Locale.getDefault().getLanguage().equals("ja")) {
                    tvViet.setText(lstData.get(0).getVi());
                    tvOther.setText(lstData.get(0).getJa());
                } else if (Locale.getDefault().getLanguage().equals("ko")) {
                    tvViet.setText(lstData.get(0).getVi());
                    tvOther.setText(lstData.get(0).getKo());
                } else {
                    tvViet.setText(lstData.get(0).getVi());
                    tvOther.setText(lstData.get(0).getEn());
                }
            }
        }

    }

}
