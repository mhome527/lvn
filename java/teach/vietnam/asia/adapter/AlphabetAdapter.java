package teach.vietnam.asia.adapter;

import java.util.List;
import teach.vietnam.asia.R;
import teach.vietnam.asia.entity.TblAlphabetEx;
import teach.vietnam.asia.utils.ULog;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AlphabetAdapter extends BaseAdapter {

	private Context context;
	private List<TblAlphabetEx> listData;
	private LayoutInflater layoutInflater;
	

	public AlphabetAdapter(Context context, List<TblAlphabetEx> listData) {
		this.context = context;
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}

	// public LearnAdapter(Context context) {
	// mContext = context;
	// }

	public int getCount() {
		return listData.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	// public View getView(int pos, View convertView, ViewGroup arg2) {

	// if (convertView == null) {
	// convertView = layoutInflater.inflate(R.layout.taxi_item, null);
	// }
	// Override this method according to your need
	@SuppressLint({ "InflateParams", "DefaultLocale" })
	public View getView(int index, View convertView, ViewGroup viewGroup) {
		String symbol;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.alphabet_item, null);
		}
		try {
			TextView tvAlphabet = (TextView) convertView.findViewById(R.id.tvAlphabet);
			TextView tvAlphabetRight = (TextView) convertView.findViewById(R.id.tvAlphabetRight);

			symbol = listData.get(index).getSymbol();
			if (symbol != null && !symbol.equals("")) {
				tvAlphabet.setText(listData.get(index).getSymbol());
				tvAlphabetRight.setText("");
			} else {
				tvAlphabet.setText(listData.get(index).getAlphabet().toUpperCase());
				tvAlphabetRight.setText(listData.get(index).getAlphabet().toLowerCase());
			}
			Typeface tf = Typeface.createFromAsset(context.getAssets(), "aachenb.ttf");
			tvAlphabet.setTypeface(tf, Typeface.NORMAL);
			tvAlphabetRight.setTypeface(tf, Typeface.NORMAL);
//			ULog.i(AlphabetAdapter.class, "getView symbol:" + symbol);
		} catch (Exception e) {
			ULog.e(this, "alphabet error:" + e.getMessage());
		}

		return convertView;
	}
}