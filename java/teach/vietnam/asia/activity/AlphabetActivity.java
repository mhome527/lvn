package teach.vietnam.asia.activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.AlphabetAdapter;
import teach.vietnam.asia.entity.TblAlphabetEx;
import teach.vietnam.asia.sound.AudioPlayer;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

public class AlphabetActivity extends BaseActivity implements OnClickListener {
    private GridView gridWords;
    private AlphabetAdapter adapter;
    private ProgressDialog progressDialog;
    private AudioPlayer audio;
    // private List<tblAlphabet> lstData;
    private List<TblAlphabetEx> lstData;
    private String[] arrAlphabet;

    // private boolean male = true;

    @Override
    protected int getViewLayoutId() {
        return R.layout.alphabet_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        gridWords = getViewChild(R.id.gridWords);

        initData();
        new LoadData().execute();

        Utility.setScreenNameGA("AlphabetActivity - lang:" + Locale.getDefault().getLanguage());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgSpeakAll:
                speakAll();
                break;
        }
        super.onClick(v);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void reloadData() {

    }

    private void initData() {
        arrAlphabet = getResources().getStringArray(R.array.alphabet);
        lstData = new ArrayList<TblAlphabetEx>();

        setListenerView(R.id.imgSpeakAll, this);
        audio = new AudioPlayer(AlphabetActivity.this);
        gridWords.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
//                String strAudio = AlphabetActivity.this.lstData.get(position).getSound() ;
                String strAudio = AlphabetActivity.this.lstData.get(position).getSound();
                audio.speakWord(strAudio);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (audio != null)
            audio.stopAll();
    }

    private void speakAll() {
        audio.isSlowly = true;
        audio.speakWord(arrAlphabet);
    }

    private class LoadData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(AlphabetActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
            }

            if (!isFinishing()) {
                progressDialog.show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            TblAlphabetEx tbl;
//            String sound;
            try {
                for (String name : arrAlphabet) {
                    tbl = new TblAlphabetEx();
                    tbl.setAlphabet(name);
                    if (name.toLowerCase().equals("sắc"))
                        tbl.setSymbol("/");
                    else if (name.toLowerCase().equals("huyền"))
                        tbl.setSymbol("\\");
                    else if (name.toLowerCase().equals("hỏi"))
                        tbl.setSymbol("?");
                    else if (name.toLowerCase().equals("ngã"))
                        tbl.setSymbol("~");
                    else if (name.toLowerCase().equals("nặng"))
                        tbl.setSymbol(".");
//                    sound = Common.getNameSound(name);
                    tbl.setSound(name);
                    lstData.add(tbl);
                }

            } catch (Exception e) {
                ULog.e(AlphabetActivity.class, "load data error:" + e.getMessage());
                e.printStackTrace();
                return false;
            }
            // return qb.list();
            return true;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (!isFinishing()) {
                progressDialog.dismiss();
            }
            if (lstData != null && lstData.size() > 0) {
                AlphabetActivity.this.lstData = lstData;
                adapter = new AlphabetAdapter(AlphabetActivity.this, lstData);
                gridWords.setAdapter(adapter);
            }
        }
    }

    // ///

}
