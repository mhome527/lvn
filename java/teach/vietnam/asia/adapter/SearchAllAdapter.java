package teach.vietnam.asia.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
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
import teach.vietnam.asia.activity.BaseActivity;
import teach.vietnam.asia.utils.Constant;
import teach.vietnam.asia.utils.NumberToWord;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;


@SuppressLint("DefaultLocale")
public class SearchAllAdapter extends BaseAdapter implements SectionIndexer {

    private Context context;
    private List listData;
    private List listData2;
    private LayoutInflater layoutInflater;
    private String lang = "";
    private String[] alpha;
    boolean modify = false;

    public SearchAllAdapter(Context context, List listData) {
        int i = 0;
        String word;
        this.context = context;
        this.listData = listData;
        listData2 = new ArrayList();
        try {
            ULog.i(SearchAllAdapter.class, "SearchAllAdapter");
            listData2.addAll(listData);
            layoutInflater = LayoutInflater.from(context);
//            lang = context.getString(R.string.language);
            lang = BaseActivity.pref.getStringValue("en", Constant.EN);

            alpha = null;
            alpha = new String[listData.size()];

            for (Object viet : listData) {
//                alpha[i++] = Utility.getO1(viet, lang).replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
                word = android.text.Html.fromHtml(Utility.getO1(viet, lang)).toString();
                alpha[i++] = word.split(" ")[0];
            }

        } catch (Exception e) {
            ULog.e(SearchAllAdapter.class, "SearchAllAdapter Error: " + e.getMessage());
        }

    }


    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

//    public String getDataVi(int position){
//        return Utility.getVi(listData.get(position), lang);
//    }
//
//    public String getDataDefaultWord(int position){
//        return Utility.getDefaultWord(listData.get(position), lang);
//    }

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

        if(listData.size() <= position)
            return view;

        holder.tvOther.setText(Html.fromHtml(Utility.getO1(listData.get(position), lang)));

        phrases = String.format(Utility.getVi(listData.get(position), lang), "<u>" + Utility.getDefaultWord(listData.get(position), lang) + "</u>");
        holder.tvVn.setText(Html.fromHtml(phrases));

        // img.setScaleType(ImageView.ScaleType.FIT_XY);
        resourceId = Utility.getResourcesID(context, Utility.getImg(listData.get(position), lang));
        if (resourceId > 0) {
            holder.imgWord.setImageResource(resourceId);
            // holder.imgWord.setTag(resourceId);
        } else {
            //truong hop hinh la food
            resourceId = Utility.getResourcesID(context, "f_" + Utility.getImg(listData.get(position), lang));
            if (resourceId > 0)
                holder.imgWord.setImageResource(resourceId);
            else
                holder.imgWord.setImageResource(0);
//            ULog.i(SearchAllAdapter.class, "dont image load");
        }
        return view;
    }

    private void resetAlphaSearch() {
        int i = 0;
        String word;
        alpha = null;
        alpha = new String[listData.size()];

        for (Object viet : listData) {
//            alpha[i++] = Utility.getO1(viet, lang).replaceAll("<u>", "").replaceAll("</u>", "").split(" ")[0];
            word = android.text.Html.fromHtml(Utility.getO1(viet, lang)).toString();
            alpha[i++] = word.split(" ")[0];
        }

    }

    @SuppressLint("DefaultLocale")
    public void filter(String charText) {
        if(!modify)
            new ResetAdapter(charText).execute();
//        String word1, word2, wordVN;
//        long number;
//        Object tmp;
//        try {
//            charText = charText.toLowerCase(Locale.getDefault()).trim();
//            ULog.i(SearchAllAdapter.this, "filter key: " + charText);
//            listData.clear();
//            if (charText.length() == 0) {
//                listData.addAll(listData2);
//            } else {
//
//                for (Object vi : listData2) {
//
//                    wordVN = android.text.Html.fromHtml(Utility.getVi(vi, lang)).toString().toLowerCase();
//
//                    if (wordVN.contains(charText) || charText.contains(wordVN)) {
//                        listData.add(vi);
//                    } else {
//                        word1 = android.text.Html.fromHtml(Utility.getO1(vi, lang)).toString().toLowerCase();
//                        if (word1.contains(charText) || charText.contains(word1)) {
//                            listData.add(vi);
//                        } else {
//                            word2 = android.text.Html.fromHtml(Utility.getO2(vi, lang)).toString().toLowerCase();
//                            if (!word2.equals("") && (word2.contains(charText) || charText.contains(word2))) {
//                                listData.add(vi);
//                            }
//                        }
//                    }
//
//                }
//            }
//
//            if (listData.size() == 0) {
//                if (!charText.equals("")) {
//                    number = Utility.convertToLong(charText);
//                    if (number > -1) {
//                        tmp = Utility.getDataObject(lang, NumberToWord.getWordFromNumber(number), charText);
//                        listData.add(tmp);
//                    }
//                }
//            }
//            resetAlphaSearch();
//            notifyDataSetChanged();
//        } catch (Exception e) {
//            ULog.e(SearchAllAdapter.this, "filter error:" + e.getMessage());
//        }
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

    public class ResetAdapter extends AsyncTask<Void, Void, Boolean>{
        private String charText;
        public ResetAdapter(String charText){
            this.charText = charText;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String word1, word2, wordVN;
            long number;
            Object tmp;
            try {
                if(modify)
                    return false;
                modify = true;
                charText = charText.toLowerCase(Locale.getDefault()).trim();
                ULog.i(SearchAllAdapter.this, "filter key: " + charText);
                listData.clear();
                if (charText.length() == 0) {
                    listData.addAll(listData2);
                } else {

                    for (Object vi : listData2) {

                        wordVN = android.text.Html.fromHtml(Utility.getVi(vi, lang)).toString().toLowerCase();

                        if (wordVN.contains(charText) || charText.contains(wordVN)) {
                            listData.add(vi);
                        } else {
                            word1 = android.text.Html.fromHtml(Utility.getO1(vi, lang)).toString().toLowerCase();
                            if (word1.contains(charText) || charText.contains(word1)) {
                                listData.add(vi);
                            } else {
                                word2 = android.text.Html.fromHtml(Utility.getO2(vi, lang)).toString().toLowerCase();
                                if (!word2.equals("") && (word2.contains(charText) || charText.contains(word2))) {
                                    listData.add(vi);
                                }
                            }
                        }

                    }
                }

                if (listData.size() == 0) {
                    if (!charText.equals("")) {
                        number = Utility.convertToLong(charText);
                        if (number > -1) {
                            tmp = Utility.getDataObject(lang, NumberToWord.getWordFromNumber(number), charText);
                            listData.add(tmp);
                        }
                    }
                }
//                resetAlphaSearch();
//                notifyDataSetChanged();
            } catch (Exception e) {
                ULog.e(SearchAllAdapter.this, "filter error:" + e.getMessage());
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            super.onPostExecute(b);
            try {
                if (b) {
                    resetAlphaSearch();
                    SearchAllAdapter.this.notifyDataSetChanged();
                }
                modify = false;
            }catch(Exception e){
                ULog.e("SearchAllAdapter", "notify Error:" + e.getMessage());
            }
        }
    }

}