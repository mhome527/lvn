package teach.vietnam.asia.activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import teach.vietnam.asia.R;
import teach.vietnam.asia.adapter.NumberAdapter;
import teach.vietnam.asia.sound.AudioPlayer;
import teach.vietnam.asia.utils.NumberToWord;
import teach.vietnam.asia.utils.ULog;
import teach.vietnam.asia.utils.Utility;

public class NumberActivity extends BaseActivity implements OnClickListener {
    private EditText edtNumber;
    private TextView tvNumber;
    private GridView gridWords;
    private NumberAdapter adapter;
    private AudioPlayer audio;

    private final int REQ_CODE_SPEECH_INPUT = 1002;
    private int arrNumber[];

    @Override
    protected int getViewLayoutId() {
        return R.layout.number_layout;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        gridWords = getViewChild(R.id.gridWords);
        edtNumber = getViewChild(R.id.edtNumber);
        tvNumber = getViewChild(R.id.tvNumber);

        initData();
        // new LoadData().execute();
    }

    @Override
    protected void reloadData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgSpeak:
                speakNumber();
                // speakAll();
                break;
            case R.id.imgRecording:
                Utility.promptSpeechInput(NumberActivity.this, REQ_CODE_SPEECH_INPUT);
                break;
        }
        super.onClick(v);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (audio != null)
            audio.stopAll();
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String text;
        long number = -1;

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    try {
                        text = Utility.stripNonDigits(result.get(0).trim());
                        number = Long.parseLong(text);
                    } catch (Exception e) {
                        Toast.makeText(NumberActivity.this, NumberActivity.this.getString(R.string.talk_number), Toast.LENGTH_LONG).show();
                    }
                    if (number >= 0)
                        edtNumber.setText(number + "");
                }
                break;
            }

        }
    }

    private void initData() {
        try {
            setListenerView(R.id.imgSpeak, this);
            setListenerView(R.id.imgRecording, this);
            arrNumber = getResources().getIntArray(R.array.number);
            audio = new AudioPlayer(NumberActivity.this);

            adapter = new NumberAdapter(NumberActivity.this, arrNumber);
            gridWords.setAdapter(adapter);

            gridWords.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                    String word;
                    // String strAudio = "sound/" + lstNumberEx.get(position).getSound() + ".mp3";
                    //
                    edtNumber.setText(arrNumber[position] + "");
                    //
                    // ULog.i(NumberActivity.class, "audio:" + strAudio);
                    // audio.playSound(strAudio);
                    word = NumberToWord.getWordFromNumber(arrNumber[position]);
                    ULog.i(NumberActivity.class, "onItemClick number:" + word);
                    audio.speakWord(word);
                }
            });

            edtNumber.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String word;
                    try {
                        ULog.i(NumberActivity.class, "number:" + edtNumber.getText().toString());
                        word = NumberToWord.getWordFromNumber(edtNumber.getText().toString());
                        tvNumber.setText(word);
                    } catch (Exception e) {
                        ULog.e(NumberActivity.class, " Error: " + e.getMessage());
                    }
                    // ULog.i(NumberActivity.class, "word:" + edtNumber.getText().toString());

                }
            });

//			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//			imm.hideSoftInputFromWindow(edtNumber.getWindowToken(), 0);
//			hideKeyboard();
        } catch (Exception e) {
            ULog.e(NumberActivity.class, "initData Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

//	private void hideKeyboard() {
//	    InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
//	    inputManager.hideSoftInputFromWindow(edtNumber.getWindowToken(), 0);
//	    // check if no view has focus:
//	    View view = this.getCurrentFocus();
//	    if (view != null) {
//	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//	    }
//	}

    private void speakNumber() {
        audio.speakWord(tvNumber.getText().toString().trim());
    }

//	private void speakNumber(String strNumber) {
//		int count = 0;
//		String soundName;
//
//		if (strNumber.equals(""))
//			return;
//
//		String[] strSound = strNumber.split(" ");
//
//		for (String name : strSound) {
//			soundName = Common.getNameSound(name);
//			ULog.i(NumberActivity.class, "speakNumber" + soundName);
//			if (!soundName.equals(""))
////				strSound[count] = "sound/" + "a1c" + ".mp3";		
//			strSound[count] = "sound/" + soundName + ".mp3";
//
//			count++;
//		}
//		ULog.i(NumberActivity.class, "audioAll:" + strSound);
//
//		if (strSound.length > 0)
//			audio.playSound(strSound);
//	}

}
