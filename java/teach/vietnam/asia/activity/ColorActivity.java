package teach.vietnam.asia.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.ColorAdapter;
import teach.vietnam.asia.sound.AudioPlayer;
import teach.vietnam.asia.utils.ULog;

public class ColorActivity extends BaseActivity implements OnClickListener {

	private String[] colorName;
	private String[] colorValue;
	private GridView gridColor;
	private AudioPlayer audio;

	@Override
	protected int getViewLayoutId() {
		return R.layout.color_layout;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		initData();
	}

	private void initData() {
//		tvColor = getViewChild(R.id.tvColor);
		gridColor = getViewChild(R.id.gridColor);
		colorName = getResources().getStringArray(R.array.color_name);
		colorValue = getResources().getStringArray(R.array.color_value);
//		tvColor.setText(colorName[0] + ":" + colorValue[0]);

		gridColor.setAdapter(new ColorAdapter(this, colorName, colorValue));
		gridColor.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int postion, long arg3) {
				ULog.i(ColorActivity.class, "onItemClick");
//				tvColor.setText(colorName[postion] + ":" + colorValue[postion]);
				audio.speakWord(colorName[postion]);
			}
		});
		
		audio = new AudioPlayer(ColorActivity.this);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		// switch (v.getId()) {
		// case R.id.action_settings:
		// ULog.i(ColorActivity.class, "onClick pie1...");
		// break;
		// }
	}

    @Override
    protected void onPause() {
        super.onPause();
        if(audio !=null)
            audio.stopAll();
    }

    @Override
    protected void reloadData() {

    }
}
