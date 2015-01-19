package teach.vietnam.asia.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import teach.vietnam.asia.R;
import teach.vietnam.asia.utils.ULog;

public class MenuRecognizeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> listData;
    private LayoutInflater layoutInflater;
//    ViewHolder viewHolder;

    public MenuRecognizeAdapter(Context context, ArrayList<String> listData) {
        this.context = context;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

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
    @SuppressLint({"InflateParams", "DefaultLocale"})
    public View getView(int index, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.menu_recognize_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvWord = (TextView) convertView.findViewById(R.id.tvWord);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        try {
            viewHolder.tvWord.setText(listData.get(index));

        } catch (Exception e) {
            ULog.e(this, "getview error:" + e.getMessage());
        }
        return convertView;
    }

    static class ViewHolder {
        TextView tvWord;
    }
}