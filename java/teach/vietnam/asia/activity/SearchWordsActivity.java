package teach.vietnam.asia.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import de.greenrobot.dao.query.QueryBuilder;
import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.SearchAdapter;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.tblViet;
import teach.vietnam.asia.entity.tblVietDao;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class SearchWordsActivity extends BaseActivity implements OnClickListener {

	private ListView lstSearch;
	private EditText edtSearch;
	private SearchAdapter adapter;
	private tblVietDao dao;
	private DaoMaster daoMaster;
	private ProgressDialog progressDialog;
	private final int REQ_CODE_SPEECH_INPUT = 1000;
	private int position;
	private List<tblViet> lstData;

	@Override
	protected int getViewLayoutId() {
		return R.layout.search_layout;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		lstSearch = getViewChild(R.id.lstSearch);
		edtSearch = getViewChild(R.id.edtSearch);

		setInitData();

		position = getIntent().getIntExtra(Constant.INTENT_POSITION, 0);
		new LoadData().execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgRecording:
			Utility.promptSpeechInput(SearchWordsActivity.this, REQ_CODE_SPEECH_INPUT);
			break;
		}
		super.onClick(v);
	}

	/**
	 * Receiving speech input
	 * */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case REQ_CODE_SPEECH_INPUT: {
			if (resultCode == RESULT_OK && null != data) {

				ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				edtSearch.setText(result.get(0));
			}
			break;
		}

		}
	}

	private void setInitData() {
		hideMenuHome();
		setListenerView(R.id.imgRecording, this);
		edtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				String text = edtSearch.getText().toString().toLowerCase(Locale.getDefault());
				adapter.filter(text);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
		});

		lstSearch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				Intent intentData = new Intent();
				intentData.putExtra(Constant.INTENT_POSITION, position);
				intentData.putExtra(Constant.INTENT_WORD, lstData.get(pos).getVi());
				SearchWordsActivity.this.setResult(RESULT_OK, intentData);
				SearchWordsActivity.this.finish();
			}
		});
	}

	private class LoadData extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(SearchWordsActivity.this);
				progressDialog.setCancelable(false);
				progressDialog.setCanceledOnTouchOutside(false);
			}

			if (!isFinishing()) {
				progressDialog.show();
			}
		}

		@SuppressLint("DefaultLocale")
		@Override
		protected Void doInBackground(Void... params) {
			QueryBuilder<tblViet> qb;
			String lang;
			try {
				daoMaster = ((MyApplication) getApplication()).daoMaster;
				dao = daoMaster.newSession().getTblVietDao();
				qb = dao.queryBuilder();

				qb.where(tblVietDao.Properties.Kind.notEq(11));

				lang = SearchWordsActivity.this.getString(R.string.language);
				if (lang.equals("ja"))
					qb.orderAsc(tblVietDao.Properties.Ja);
				else if (lang.equals("ko"))
					qb.orderAsc(tblVietDao.Properties.Ko);
				else
					qb.orderAsc(tblVietDao.Properties.En);

				ULog.i(this, "===data db:" + qb.list().size());
				lstData = qb.list();
			} catch (Exception e) {
				ULog.e(SearchWordsActivity.class, "load data error:" + e.getMessage());
				return null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (!isFinishing()) {
				progressDialog.dismiss();
			}
			if (lstData != null && lstData.size() > 0) {

				adapter = new SearchAdapter(SearchWordsActivity.this, lstData);
				lstSearch.setAdapter(adapter);
				// adapter.notifyDataSetChanged();
			}

		}

	}

}
