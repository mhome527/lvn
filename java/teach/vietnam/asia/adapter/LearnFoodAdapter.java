package teach.vietnam.asia.adapter;

import java.util.List;
import teach.vietnam.asia.R;
import teach.vietnam.asia.entity.tblViet;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

@SuppressWarnings("deprecation")
public class LearnFoodAdapter extends BaseAdapter {
	
	private Context context;
	private List<tblViet> listData;
	private LayoutInflater layoutInflater;

//	private Integer[] mImageIds = { R.drawable.rect, R.drawable.cp_hn, R.drawable.hoanglong, R.drawable.ic_launcher };

	// private Integer[] mImageIds = { R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5,
	// R.drawable.image6, R.drawable.image7, R.drawable.image8 };

	public LearnFoodAdapter(Context context, List<tblViet> listData) {
		this.context = context;
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}

	
//	public LearnAdapter(Context context) {
//		mContext = context;
//	}

	public int getCount() {
		return listData.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	//public View getView(int pos, View convertView, ViewGroup arg2) {

//	if (convertView == null) {
//		convertView = layoutInflater.inflate(R.layout.taxi_item, null);
//	}
	// Override this method according to your need
	@SuppressLint("InflateParams")
	public View getView(int index, View convertView, ViewGroup viewGroup) {
		int resourceId;
		
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.learn_food_item, null);
		}
		
//		ImageView img = new ImageView(mContext);
		ImageView img = (ImageView)convertView.findViewById(R.id.imgFruit);
		img.setLayoutParams(new Gallery.LayoutParams(200, 250));
		img.setScaleType(ImageView.ScaleType.FIT_XY);
		resourceId = Utility.getResourcesID(context, listData.get(index).getImg());
		if (resourceId > 0) {
			img.setImageResource(resourceId);
			img.setTag(resourceId);
		} else
			ULog.e(LearnPagerAdapter.class, "dont load resource");

		return img;
	}
}