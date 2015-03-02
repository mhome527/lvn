package teach.vietnam.asia.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import teach.vietnam.asia.R;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

public class LearnPagerAdapter extends PagerAdapter {

	private LayoutInflater inflater;
	private Context context;
	private List listData;
    private String lang;

//	int[] flag;

	public LearnPagerAdapter(Context context, List listData, String lang) {
		this.context = context;
		this.listData = listData;
        this.lang = lang;
	}

	// public LearnPagerAdapter(Context context, int[] flag) {
	// this.context = context;
	// this.flag = flag;
	// }

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);

	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		int resourceId;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.learn_food_item, container, false);

		resourceId = Utility.getResourcesID(context, Utility.getImg(listData.get(position), lang));
		if (resourceId > 0) {
			// Locate the ImageView in viewpager_item.xml
			ImageView imgflag = (ImageView) itemView.findViewById(R.id.imgFruit);
			// Capture position and set to the ImageView
			imgflag.setImageResource(resourceId);
		} else
			ULog.e(LearnPagerAdapter.class, "dont load resource");
		// Add viewpager_item.xml to ViewPager
		((ViewPager) container).addView(itemView);

		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((LinearLayout) object);

	}

}
