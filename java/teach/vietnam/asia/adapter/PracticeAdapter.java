package teach.vietnam.asia.adapter;

import java.util.List;
import java.util.Locale;

import teach.vietnam.asia.R;
//import teach.vietnam.asia.entity.tblKind;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PracticeAdapter extends BaseAdapter {

//	private Context context;
//	private List<tblKind> listData;
//	private LayoutInflater layoutInflater;
//
//	public PracticeAdapter(Context context, List<tblKind> listData) {
//		ULog.i(PracticeAdapter.class, "PracticeAdapter locale:" + Locale.getDefault().getLanguage());
//		this.context = context;
//		this.listData = listData;
//		layoutInflater = LayoutInflater.from(context);
//	}
//
//	// public LearnAdapter(Context context) {
//	// mContext = context;
//	// }
//
	public int getCount() {
//		return listData.size();
        return 0;
	}
//
	public Object getItem(int position) {
		return position;
	}
//
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View view, ViewGroup viewGroup) {
//		int resourceId;
//		final ViewHolder holder;
//		if (view == null) {
//			holder = new ViewHolder();
//			view = layoutInflater.inflate(R.layout.practice_kind_item, null);
//			holder.tvTitle1 = (TextView) view.findViewById(R.id.tvTitle1);
//			holder.tvTitle2 = (TextView) view.findViewById(R.id.tvTitle2);
//			holder.imgKind = (ImageView) view.findViewById(R.id.imgKind);
//
//			view.setTag(holder);
//		} else {
//			holder = (ViewHolder) view.getTag();
//		}
//
//
//		if (Locale.getDefault().getLanguage().equals("ja"))
//			holder.tvTitle1.setText(listData.get(position).getJa());
//		if (Locale.getDefault().getLanguage().equals("ko"))
//			holder.tvTitle1.setText(listData.get(position).getKo());
//		else
//			holder.tvTitle1.setText(listData.get(position).getEn());
//
//		holder.tvTitle2.setText(listData.get(position).getVi());
//		resourceId = Utility.getResourcesID(context, listData.get(position).getImg());
//		if (resourceId > 0)
//			holder.imgKind.setBackgroundResource(resourceId);
//		// holder.tvTitle1.setText(text)
//
		return view;
	}

//	public class ViewHolder {
//		TextView tvTitle1;
//		TextView tvTitle2;
//		ImageView imgKind;
//	}

}