package teach.vietnam.asia.adapter;

import teach.vietnam.asia.R;
import teach.vietnam.asia.utils.NumberToWord;
import teach.vietnam.asia.utils.ULog;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NumberAdapter extends BaseAdapter {

	private Context context;
	private int[] arrNumber;
	private LayoutInflater layoutInflater;

	public NumberAdapter(Context context, int[] arrNumber) {
		this.context = context;
		this.arrNumber = arrNumber;
		layoutInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		return arrNumber.length;
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
		long number;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.number_item, null);
		}
		try {

			TextView tvNumber = (TextView) convertView.findViewById(R.id.tvNumber);
			TextView tvWord = (TextView) convertView.findViewById(R.id.tvWord);

			number = arrNumber[index];
			if (number < 100)
				tvNumber.setTextSize(40);
			else if (number <= 10000)
				tvNumber.setTextSize(32);
			else if (number <= 100000)
				tvNumber.setTextSize(27);
			else if (number <= 1000000)
				tvNumber.setTextSize(22);
			else if (number <= 10000000)
				tvNumber.setTextSize(20);
			else
				tvNumber.setTextSize(16);
			tvNumber.setText(number + "");
			// tvNumber.setText(listData.get(index).tblgen.getNumber() + "");

			tvWord.setText(NumberToWord.getWordFromNumber(number + ""));

			Typeface tf = Typeface.createFromAsset(context.getAssets(), "aachenb.ttf");
			tvNumber.setTypeface(tf, Typeface.NORMAL);
			tvWord.setTypeface(tf, Typeface.NORMAL);

		} catch (Exception e) {
			ULog.e(this, "number error:" + e.getMessage());
		}
		return convertView;
	}
}