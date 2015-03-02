package teach.vietnam.asia.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import teach.vietnam.asia.R;
import teach.vietnam.asia.utils.NumberToWord;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

//import android.widget.SectionIndexer;

@SuppressLint("DefaultLocale")
public class SearchAllAdapter extends BaseAdapter implements SectionIndexer {

    private Context context;
    private List listData;
    private List listData2;
    private LayoutInflater layoutInflater;
    private String lang = "";
    private String[] alpha;

    public SearchAllAdapter(Context context, List listData) {
        int i = 0;
        this.context = context;
        this.listData = listData;
        listData2 = new ArrayList();
        try {
            ULog.i(SearchAllAdapter.class, "SearchAllAdapter");
            listData2.addAll(listData);
            layoutInflater = LayoutInflater.from(context);
            lang = context.getString(R.string.language);

            // /

            alpha = null;
            alpha = new String[listData.size()];

//            for (tblVietEN viet : listData) {
//                alpha[i++] = viet.getO1().toString().replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
//            }
            for (Object viet : listData) {
                alpha[i++] = Utility.getO1(viet, lang).replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
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

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

//    public tblVietEN getItem(int position) {
//        return listData.get(position);
//    }

    //    public tblVietEN getItem(int position) {
//        return listData.get(position);
//    }

    public String getDataVi(int position){
        return Utility.getVi(listData.get(position), lang);
    }

    public String getDataDefaultWord(int position){
        return Utility.getDefaultWord(listData.get(position), lang);
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

        holder.tvOther.setText(Html.fromHtml(Utility.getO1(listData.get(position), lang)));
//		if (lang.equals("ja"))
//			holder.tvOther.setText(Html.fromHtml(listData.get(position).getJa()));
//		else if (lang.equals("ko"))
//			holder.tvOther.setText(Html.fromHtml(listData.get(position).getKo()));
//		else
//			holder.tvOther.setText(Html.fromHtml(listData.get(position).getEn()));

        phrases = String.format(Utility.getVi(listData.get(position), lang), "<u>" + Utility.getDefaultWord(listData.get(position), lang) + "</u>");
        holder.tvVn.setText(Html.fromHtml(phrases));

        // img.setScaleType(ImageView.ScaleType.FIT_XY);
        resourceId = Utility.getResourcesID(context, Utility.getImg(listData.get(position), lang));
        if (resourceId > 0) {
            holder.imgWord.setImageResource(resourceId);
            // holder.imgWord.setTag(resourceId);
        } else {
            holder.imgWord.setImageResource(0);
            ULog.i(SearchAllAdapter.class, "dont image load");
        }
        return view;
    }

    private void resetAlphaSearch() {
        int i = 0;
        alpha = null;
        alpha = new String[listData.size()];
//        for (tblVietEN viet : listData) {
//            alpha[i++] = viet.getO1().toString().replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
//        }

        for (Object viet : listData) {
            alpha[i++] = Utility.getO1(viet, lang).replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
        }

    }

    @SuppressLint("DefaultLocale")
    public void filter(String charText) {
        String word1 = "", word2 = "";
        long number;
//        tblVietEN tmp;
        Object tmp;
        try {
            charText = charText.toLowerCase(Locale.getDefault()).trim();
            ULog.i(SearchAllAdapter.this, "filter key: " + charText);
            listData.clear();
            if (charText.length() == 0) {
                listData.addAll(listData2);
            } else {
//                for (tblVietEN vi : listData2) {
//                    word1 = vi.getO1();
//                    word2 = vi.getO2();
//                    if (word1.contains(charText) || charText.contains(word1)) {
//                        listData.add(vi);
//                    } else if (!word2.equals("") && (word2.contains(charText) || charText.contains(word2))) {
//                        listData.add(vi);
//                    }
//                }

                for (Object vi : listData2) {
                    word1 = Utility.getO1(vi, lang);
                    word2 = Utility.getO2(vi, lang);
                    if (word1.contains(charText) || charText.contains(word1)) {
                        listData.add(vi);
                    } else if (!word2.equals("") && (word2.contains(charText) || charText.contains(word2))) {
                        listData.add(vi);
                    }
                }
            }

            if (listData.size() == 0) {
                if (!charText.equals("")) {
                    number = Utility.convertToLong(charText);
                    if (number > -1) {
//                        tmp = new tblVietEN();
//                        tmp.setVi(NumberToWord.getWordFromNumber(number));
//                        tmp.setO1(charText);
                        tmp = Utility.getDataObject(lang, NumberToWord.getWordFromNumber(number), charText);

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

    public class ViewHolder {
        TextView tvOther;
        TextView tvVn;
        ImageView imgWord;
    }

}