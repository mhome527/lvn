package teach.vietnam.asia.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import teach.vietnam.asia.R;
import teach.vietnam.asia.entity.tblViet;
import teach.vietnam.asia.utils.NumberToWord;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
//import android.widget.SectionIndexer;
import android.widget.TextView;

@SuppressLint("DefaultLocale")
public class SearchAllAdapter extends BaseAdapter implements SectionIndexer {

	private Context context;
	private List<tblViet> listData;
	private List<tblViet> listData2;
	private LayoutInflater layoutInflater;
	private String lang = "";
	private String[] alpha;

	public SearchAllAdapter(Context context, List<tblViet> listData) {
		int i = 0;
		this.context = context;
		this.listData = listData;
		listData2 = new ArrayList<tblViet>();
		try {
			ULog.i(SearchAllAdapter.class, "SearchAllAdapter");
			listData2.addAll(listData);
			layoutInflater = LayoutInflater.from(context);
			lang = context.getString(R.string.language);

			// /

			alpha = null;
			alpha = new String[listData.size()];
            if (lang.equals("ja")) {
                for (tblViet viet : listData) {
                    alpha[i++] = viet.getJa().toString().replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
                }
            } else if (lang.equals("ko")) {
                for (tblViet viet : listData) {
                    alpha[i++] = viet.getKo().toString().replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
                }
            } else{
                for (tblViet viet : listData) {
                    alpha[i++] = viet.getEn().toString().replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
                }
            }
		} catch (Exception e) {
			ULog.e(SearchAllAdapter.class, "SearchAllAdapter Error: " + e.getMessage());
		}

	}

	// public LearnAdapter(Context context) {
	// mContext = context;
	// }

	public int getCount() {
		return listData.size();
	}

	public tblViet getItem(int position) {
		return listData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View view, ViewGroup viewGroup) {
		int resourceId;
		final ViewHolder holder;
		String phrases;
		if (view == null) {
			holder = new ViewHolder();
			view = layoutInflater.inflate(R.layout.search_item, null);
			holder.tvOther = (TextView) view.findViewById(R.id.tvOther);
			holder.tvVn = (TextView) view.findViewById(R.id.tvVn);
			holder.imgWord = (ImageView) view.findViewById(R.id.imgWord);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (lang.equals("ja"))
			holder.tvOther.setText(Html.fromHtml(listData.get(position).getJa()));
		else if (lang.equals("ko"))
			holder.tvOther.setText(Html.fromHtml(listData.get(position).getKo()));
		else
			holder.tvOther.setText(Html.fromHtml(listData.get(position).getEn()));

		phrases = String.format(listData.get(position).getVi(), "<u>" + listData.get(position).getDefault_word() + "</u>");
		holder.tvVn.setText(Html.fromHtml(phrases));

		// img.setScaleType(ImageView.ScaleType.FIT_XY);
		resourceId = Utility.getResourcesID(context, listData.get(position).getImg());
		if (resourceId > 0) {
			holder.imgWord.setImageResource(resourceId);
			// holder.imgWord.setTag(resourceId);
		} else {
			holder.imgWord.setImageResource(0);
			ULog.i(SearchAllAdapter.class, "dont image load");
		}
		return view;
	}

	public class ViewHolder {
		TextView tvOther;
		TextView tvVn;
		ImageView imgWord;
	}

    private void resetAlphaSearch(){
        int i = 0;
        alpha = null;
        alpha = new String[listData.size()];
        if (lang.equals("ja")) {
            for (tblViet viet : listData) {
                alpha[i++] = viet.getJa().toString().replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
            }
        } else if (lang.equals("ko")) {
            for (tblViet viet : listData) {
                alpha[i++] = viet.getKo().toString().replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
            }
        } else{
            for (tblViet viet : listData) {
                alpha[i++] = viet.getEn().toString().replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
            }
        }
    }

	@SuppressLint("DefaultLocale")
	public void filter(String charText) {
		String word1 = "", word2 = "";
        long number;
        tblViet tmp;

		try {
			charText = charText.toLowerCase(Locale.getDefault()).trim();
			ULog.i(SearchAllAdapter.this, "filter key: " + charText);
			listData.clear();
			if (charText.length() == 0) {
				listData.addAll(listData2);
			} else {
				for (tblViet vi : listData2) {
					if (lang.toLowerCase().equals("ja")) {
						word1 = vi.getJa();
						word2 = vi.getJa2();
                        if (word1.contains(charText) || charText.contains(word1)) {
                            listData.add(vi);
                        } else if (!word2.equals("") && (word2.contains(charText) || charText.contains(word2))) {
                            listData.add(vi);
                        }
					} else if (lang.toLowerCase().equals("ko")) {
						word1 = vi.getKo().toLowerCase();
						if (word1.contains(charText) || charText.contains(word1)) {
							listData.add(vi);
						}
					} else {
						word1 = vi.getEn().toLowerCase();
						if (word1.contains(charText) || charText.contains(word1)) {
							listData.add(vi);
						}
					}
				}
			}

            if (listData.size() == 0) {
                if (!charText.equals("")) {
                    number = Utility.convertToLong(charText);
                    if (number > -1) {
                        tmp = new tblViet();
                        tmp.setVi(NumberToWord.getWordFromNumber(number));
                        tmp.setEn(charText);
                        tmp.setJa(charText);
                        tmp.setJa2(charText);
                        tmp.setKo(charText);
                        listData.add(tmp);
                    }

                }
            }
            resetAlphaSearch();
			notifyDataSetChanged();
		} catch (Exception e) {
			ULog.e(SearchAllAdapter.this, "filter error:" + e.getMessage());
		}
	}

	@Override
	public int getPositionForSection(int section) {
		return section;
	}

	@Override
	public int getSectionForPosition(int arg0) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		return alpha;
	}

}