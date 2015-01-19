package teach.vietnam.asia.adapter;

import teach.vietnam.asia.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PracticeFooterAdapter extends BaseAdapter {
	public int currentPage = 0;
	private LayoutInflater inflater;
	private int size;

	public PracticeFooterAdapter(Context context, int size) {
		inflater = LayoutInflater.from(context);
		this.size = size;
	}

	@Override
	public int getCount() {
		return size;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder tag;
//		ULog.i(TAG, "getoview pos:" + position +"; curr:" + currentPage);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.practice_footer_item, null);
			tag = new ViewHolder();
			tag.tvNum = (TextView) convertView.findViewById(R.id.tvNum);

			convertView.setTag(tag);
		} else {
			tag = (ViewHolder) convertView.getTag();
		}

		tag.tvNum.setText((position + 1) + "");
		if (currentPage == position)
			tag.tvNum.setBackgroundResource(R.drawable.circle_red);
		else
			tag.tvNum.setBackgroundResource(0);

		return convertView;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	static class ViewHolder {
		TextView tvNum;
	}
}