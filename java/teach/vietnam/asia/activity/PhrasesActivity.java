package teach.vietnam.asia.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.query.QueryBuilder;
import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.PhrasesAdapter;
import teach.vietnam.asia.sound.IAudioPlayer;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

public class PhrasesActivity extends BaseActivity implements OnClickListener, IAudioPlayer {
    public static final int REQUEST_CODE_SEARCH = 500;
    private final int REQUEST_CODE_SPEECH_INPUT = 1000;
    public boolean isClick = false;
    public boolean isSlowly = false;
    private EditText edtSearch;
    private ListView lstPhrases;
    private ProgressDialog progressDialog;
    private PhrasesAdapter adapter;
    private List lstData;
    private CheckBox ckbSpeed;
    private TextView tvhint;


    @Override
    protected int getViewLayoutId() {
        return R.layout.phrases_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        lstPhrases = getViewChild(R.id.lstPhrases);
        edtSearch = getViewChild(R.id.edtSearch);
        ckbSpeed = getViewChild(R.id.ckbSpeed);
        tvhint = getViewChild(R.id.tvHint);

        edtSearch.clearFocus();
        // tvText1.setText(Html
        // .fromHtml("<p>Description here dsf sdf sd <u>underline   </u>f asfd sadf <font size=\"3\" color=\"red\">This is some text!</font>dfas fsa fsd sdf asdf sdf sdf sdaf sdfsd</p>"));
//		setListenerView(R.id.tvText1, this);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initData();

        Utility.setScreenNameGA("PhrasesActivity");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgRecording:
                Utility.promptSpeechInput(PhrasesActivity.this, REQUEST_CODE_SPEECH_INPUT);
                break;
            case R.id.llSpeed:
                if (isSlowly) {
                    isSlowly = false;
                    ckbSpeed.setChecked(false);
                    adapter.setSlowly(false);
                } else {
                    isSlowly = true;
                    ckbSpeed.setChecked(true);
                    adapter.setSlowly(true);
                }
                break;
            case R.id.ckbSpeed:
                if (ckbSpeed.isChecked()) {
                    isSlowly = true;
                    adapter.setSlowly(true);
                } else {
                    isSlowly = false;
                    adapter.setSlowly(false);
                }
                break;
        }

        super.onClick(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        try {
            isClick = false;
            if (requestCode == REQUEST_CODE_SEARCH && resultCode == RESULT_OK) {
                int pos = data.getIntExtra(Constant.INTENT_POSITION, -1);
                String word = data.getStringExtra(Constant.INTENT_WORD);
                if (pos > -1) {
//                    lstData.get(pos).setDefault_word(word);
                    Utility.setDataObject(lang, lstData.get(pos), word);
                    adapter.notifyDataSetChanged();
                } else
                    ULog.i(PhrasesActivity.class, "dont get word");
            } else if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK) {
                if (data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edtSearch.setText(result.get(0));
                }
            }
        } catch (Exception e) {
            ULog.e(PhrasesActivity.class, "onActivityResult Error: " + e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adapter != null && adapter.audio != null)
            adapter.audio.stopAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isClick = false;
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

    private void initData() {
        setListenerView(R.id.imgRecording, this);
        setListenerView(R.id.ckbSpeed, this);
        setListenerView(R.id.llSpeed, this);
        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String text = edtSearch.getText().toString().toLowerCase();
                if (text == null || text.equals("")) {
                    ULog.e(PhrasesActivity.class, "text empty");
                    return;
                }
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });
    }

    @Override
    public void showWord(String word, boolean visible) {
        if (!isFinishing()) {
            if (visible)
                tvhint.setVisibility(View.VISIBLE);
            else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvhint.setVisibility(View.GONE);
                    }
                }, 1000);
            }

            tvhint.setText(word);
        }
    }

    private class LoadData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(PhrasesActivity.this);
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
                dao = Utility.getDao(PhrasesActivity.this, lang);
                qb = dao.queryBuilder();

                qb.where(Utility.getKind(lang).eq(11));
                // qb.orderAsc(tblKindDao.Properties.Pos);
                ULog.i(this, "===data db:" + qb.list().size());
                lstData = qb.list();

            } catch (Exception e) {
                ULog.e(PhrasesActivity.class, "loading data error:" + e.getMessage());
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

            if (isFinishing()) {
                return;
            }
            if (lstData != null && lstData.size() > 0) {
                ULog.i(PhrasesActivity.this, "load data size:" + lstData.size());
                adapter = new PhrasesAdapter(PhrasesActivity.this, lstData, PhrasesActivity.this);
                lstPhrases.setAdapter(adapter);
            } else {
                ULog.e(PhrasesActivity.class, "Load data Error");
                finish();
            }
        }

    }
}
