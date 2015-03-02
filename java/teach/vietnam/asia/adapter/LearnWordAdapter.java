package teach.vietnam.asia.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import teach.vietnam.asia.R;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

public class LearnWordAdapter extends BaseAdapter {

	private Activity activity;
	private int widthScreen;
	private int heightScreen;

	private List lstData;
	private LayoutInflater layoutInflater;
    private String lang;

	public LearnWordAdapter(Activity activity, List lstData, String lang) {
		this.activity = activity;
		this.lstData = lstData;
        this.lang = lang;
		layoutInflater = LayoutInflater.from(activity);

		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		widthScreen = (metrics.widthPixels - 50) / 3;
		heightScreen = (int) (widthScreen * 1.3);
		ULog.i(LearnWordAdapter.class, "getView width: " + widthScreen + "; height:" + heightScreen);

	}

	@Override
	public int getCount() {
		return lstData.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint({ "InflateParams", "DefaultLocale" })
	public View getView(int index, View convertView, ViewGroup viewGroup) {
		int resourceId;
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.learn_word_item, null);
			viewHolder.imgWord = (ImageView) convertView.findViewById(R.id.imgWord);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			viewHolder.llWord = (LinearLayout) convertView.findViewById(R.id.llWord);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		try {
//			LinearLayout.LayoutParams param;
			// param = (GridViewLayoutParams) viewHolder.llWord.getLayoutParams();
			// param.height = heightScreen;

			viewHolder.llWord.setLayoutParams(new GridView.LayoutParams(LayoutParams.MATCH_PARENT, heightScreen));


            viewHolder.tvName.setText(Utility.getVi(lstData.get(index), lang));
            resourceId = Utility.getResourcesID(activity, Utility.getImg(lstData.get(index), lang));

//            viewHolder.tvName.setText(arrViet.get(index).getVi());
//			resourceId = Utility.getResourcesID(activity, arrViet.get(index).getImg());
			if (resourceId > 0) {
				viewHolder.imgWord.setImageResource(resourceId);
			} else
				ULog.i(LearnWordAdapter.class, "getView image not found ");

		} catch (Exception e) {
			ULog.e(this, "number error:" + e.getMessage());
		}
		return convertView;
	}

	public class ViewHolder {
		LinearLayout llWord;
		TextView tvName;
		ImageView imgWord;
	}


}