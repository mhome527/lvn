package teach.vietnam.asia.activity;

import teach.vietnam.asia.R;
import teach.vietnam.asia.sound.AudioPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class BodyActivity extends BaseActivity implements OnClickListener {
	private Button btnHead;
	private TextView tvViet;
	private TextView tvOther;
	private AudioPlayer audio;

	@Override
	protected int getViewLayoutId() {
		return R.layout.body_layout;
	}

	@Override
	protected void initView(Bundle savedInstanceState) {
		btnHead = getViewChild(R.id.btnHead);
		tvViet = getViewChild(R.id.tvViet);
		tvOther = getViewChild(R.id.tvOther);
		initData();
	}

	private void initData() {
		audio = new AudioPlayer(BodyActivity.this);

		setListenerView(R.id.imgSpeak, this);
		setListenerView(R.id.btnHead, this);
		setListenerView(R.id.btnHair, this);
		setListenerView(R.id.btnEyebrow, this);
		setListenerView(R.id.btnEye, this);
		setListenerView(R.id.btnCheek, this);
		setListenerView(R.id.btnNeck, this);
		setListenerView(R.id.btnChest, this);
		setListenerView(R.id.btnArm, this);
		setListenerView(R.id.btnHand, this);
		setListenerView(R.id.btnknee, this);
		setListenerView(R.id.btnFoot, this);
		setListenerView(R.id.btnForehead, this);
		setListenerView(R.id.btnEar, this);
		setListenerView(R.id.btnNose, this);
		setListenerView(R.id.btnMouth, this);
		setListenerView(R.id.btnShoulder, this);
		setListenerView(R.id.btnHip, this);
		setListenerView(R.id.btnFinger, this);
		setListenerView(R.id.btnWrist, this);
		setListenerView(R.id.btnHeel, this);
		setListenerView(R.id.btnToes, this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgSpeak:
			speakNumber();
			break;
		case R.id.btnHead:
			tvViet.setText(btnHead.getText().toString());
			tvOther.setText(btnHead.getTag().toString());
			speakNumber();
			break;
		case R.id.btnHair:
			tvOther.setText(getViewChild(R.id.btnHair).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnHair)).getText().toString());
			speakNumber();
			break;
		case R.id.btnEyebrow:
			tvOther.setText(getViewChild(R.id.btnEyebrow).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnEyebrow)).getText().toString());
			speakNumber();
			break;
		case R.id.btnEye:
			tvOther.setText(getViewChild(R.id.btnEye).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnEye)).getText().toString());
			speakNumber();
			break;
		case R.id.btnCheek:
			tvOther.setText(getViewChild(R.id.btnCheek).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnCheek)).getText().toString());
			speakNumber();
			break;
		case R.id.btnNeck:
			tvOther.setText(getViewChild(R.id.btnNeck).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnNeck)).getText().toString());
			speakNumber();
			break;
		case R.id.btnChest:
			tvOther.setText(getViewChild(R.id.btnChest).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnChest)).getText().toString());
			speakNumber();
			break;
		case R.id.btnArm:
			tvOther.setText(getViewChild(R.id.btnArm).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnArm)).getText().toString());
			speakNumber();
			break;
		case R.id.btnHand:
			tvOther.setText(getViewChild(R.id.btnHand).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnHand)).getText().toString());
			speakNumber();
			break;
		case R.id.btnknee:
			tvOther.setText(getViewChild(R.id.btnknee).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnknee)).getText().toString());
			speakNumber();
			break;
		case R.id.btnFoot:
			tvOther.setText(getViewChild(R.id.btnFoot).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnFoot)).getText().toString());
			speakNumber();
			break;
		case R.id.btnForehead:
			tvOther.setText(getViewChild(R.id.btnForehead).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnForehead)).getText().toString());
			speakNumber();
			break;
		case R.id.btnEar:
			tvOther.setText(getViewChild(R.id.btnEar).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnEar)).getText().toString());
			speakNumber();
			break;
		case R.id.btnNose:
			tvOther.setText(getViewChild(R.id.btnNose).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnNose)).getText().toString());
			speakNumber();
			break;
		case R.id.btnMouth:
			tvOther.setText(getViewChild(R.id.btnMouth).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnMouth)).getText().toString());
			speakNumber();
			break;
		case R.id.btnShoulder:
			tvOther.setText(getViewChild(R.id.btnShoulder).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnShoulder)).getText().toString());
			speakNumber();
			break;
		case R.id.btnHip:
			tvOther.setText(getViewChild(R.id.btnHip).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnHip)).getText().toString());
			speakNumber();
			break;
		case R.id.btnFinger:
			tvOther.setText(getViewChild(R.id.btnFinger).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnFinger)).getText().toString());
			speakNumber();
			break;
		case R.id.btnWrist:
			tvOther.setText(getViewChild(R.id.btnWrist).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnWrist)).getText().toString());
			speakNumber();
			break;
		case R.id.btnHeel:
			tvOther.setText(getViewChild(R.id.btnHeel).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnHeel)).getText().toString());
			speakNumber();
			break;
		case R.id.btnToes:
			tvOther.setText(getViewChild(R.id.btnToes).getTag().toString());
			tvViet.setText(((Button) getViewChild(R.id.btnToes)).getText().toString());
			speakNumber();
			break;
		}

		super.onClick(v);
	}

    @Override
    protected void onPause() {
        super.onPause();
        if(audio !=null)
            audio.stopAll();
    }

    @Override
	protected void onResume() {
		super.onResume();
	}

	private void speakNumber() {
		audio.speakWord(tvViet.getText().toString());
	}

}
