package teach.vietnam.asia.sound;

import java.util.ArrayList;

import teach.vietnam.asia.BuildConfig;
import teach.vietnam.asia.activity.NumberActivity;
import teach.vietnam.asia.utils.Common;
import teach.vietnam.asia.utils.NumberToWord;
import teach.vietnam.asia.utils.ULog;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

public class AudioPlayer {
    private ArrayList<MediaPlayer> mPlayerList;
    private Context context;
    private AudioPlay facePlay;
    public boolean isSlowly = false;

    public interface AudioPlay {
        public void finishAudio(String fileName);
    }

    public AudioPlayer(Context context) {
        this.context = context;
    }

    public AudioPlayer(Context context, AudioPlay facePlay) {
        this.context = context;
        this.facePlay = facePlay;
    }

//    public void stop() {
//        if (mMediaPlayer != null) {
//            mMediaPlayer.release();
//            mMediaPlayer = null;
//        }
//    }

    public void stopAll() {
//        stop();
        MediaPlayer mPlayerT;
        try {
            if (mPlayerList == null || mPlayerList.size() < 1)
                return;

            for (int i = 0; i < mPlayerList.size(); i++) {
                mPlayerT = mPlayerList.get(i);
                if (mPlayerT != null) {
                    if (mPlayerT.isPlaying())
                        mPlayerT.stop();
                    mPlayerList.remove(mPlayerT);
                    mPlayerT.release();
                    mPlayerT = null;
                    i = 0;
                }
            }
        } catch (Exception e) {
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

    public void speakWord(String strWord) {

        if (strWord.equals(""))
            return;

        String[] strSound = strWord.split(" ");
        speakWord(strSound);

    }

    public void speakWord(String[] strSound) {
        String soundName, number;
        String[] strNumber;
        ArrayList<String> listSound = new ArrayList<String>();
        for (String name : strSound) {
            soundName = Common.getNameSound(name.trim());
            ULog.i(NumberActivity.class, "speakNumber name:" + name + ", sound:" + soundName);
            if (!soundName.equals(""))
                listSound.add("sound/" + soundName + ".mp3");
            else {
                number = NumberToWord.getWordFromNumber(name);
                ULog.i(AudioPlayer.class, "speakWord number: " + number);
                if (!number.equals("")) {
                    strNumber = number.split(" ");
                    for (String num : strNumber) {
                        soundName = Common.getNameSound(num);
                        if (!soundName.equals(""))
                            listSound.add("sound/" + soundName + ".mp3");
                    }
                }
            }
        }
//        ULog.i(NumberActivity.class, "audioAll:" + strSound);

        if (listSound.size() > 0)
            playSound(listSound.toArray(new String[listSound.size()]));
    }


    private void playSound(String[] arrSound) {
        try {
            stopAll();
            mPlayerList = new ArrayList<MediaPlayer>();
            mPlayerList.clear();

            for (final String fileName : arrSound) {
                try {
                    if (!fileName.equals("")) {
                        MediaPlayer mPlayerT = new MediaPlayer();

                        AssetFileDescriptor descriptor = context.getAssets().openFd(fileName);
                        mPlayerT.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                        mPlayerT.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                try {
                                    ULog.i(AudioPlayer.class, "onCompletion name:" + fileName + "; list:" + mPlayerList.size());
                                    if (isSlowly)
                                        Thread.sleep(500);
//                                    stop();
                                    if (facePlay != null)
                                        facePlay.finishAudio(fileName);

                                    mPlayerList.remove(mp);
                                    mp.stop();
                                    mp.release();
                                    mp = null;

                                    if (mPlayerList.size() > 0)
//                                        mPlayerList.get(0).setOnPreparedListener(prepareSound);
                                        mPlayerList.get(0).start();

                                } catch (Exception e) {
                                    ULog.e(AudioPlayer.class, "!!!!!! Completion Error:" + e.getMessage());
                                    if (BuildConfig.DEBUG)
                                        e.printStackTrace();
                                }
                            }
                        });

                        descriptor.close();
                        descriptor = null;

                        mPlayerT.prepare();
                        mPlayerT.setVolume(1f, 1f);
                        mPlayerT.setLooping(false);

                        mPlayerList.add(mPlayerT);
                    }

                } catch (Exception e) {
                    ULog.e(AudioPlayer.class, "playSound2 error:" + e.getMessage());
                    if (BuildConfig.DEBUG)
                        e.printStackTrace();
                }

            }

            mPlayerList.get(0).start();
//            mPlayerList.get(0).setOnPreparedListener(prepareSound);
        } catch (Exception e) {
            ULog.e(AudioPlayer.class, "playSound Error: " + e.getMessage());
            if (BuildConfig.DEBUG)
                e.printStackTrace();
        }
    }

//    private MediaPlayer.OnPreparedListener prepareSound = new MediaPlayer.OnPreparedListener(){
//
//        @Override
//        public void onPrepared(MediaPlayer mediaPlayer) {
//            mediaPlayer.start();
//        }
//    };


    //    @SuppressLint("NewApi")
//    private void playSound(String[] arrSound) {
//        try {
//            stopAll();
//            mPlayerList = new ArrayList<MediaPlayer>();
//            mPlayerList.clear();
//
//            for (final String fileName : arrSound) {
//                try {
//                    if (!fileName.equals("")) {
//                        MediaPlayer mPlayerT = new MediaPlayer();
//
//                        AssetFileDescriptor descriptor = context.getAssets().openFd(fileName);
//                        mPlayerT.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
//                        mPlayerT.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//                                try {
//                                    ULog.i(AudioPlayer.class, "onCompletion name:" + fileName);
//                                    if (isSlowly)
//                                        Thread.sleep(1000);
//                                    stop();
//                                    if (facePlay != null)
//                                        facePlay.finishAudio(fileName);
//
//
//                                } catch (Exception e) {
//                                    ULog.e(AudioPlayer.class, "Completion Error:" + e.getMessage());
//                                }
//                            }
//                        });
//
//                        descriptor.close();
//
//                        mPlayerT.prepare();
//                        mPlayerT.setVolume(1f, 1f);
//                        mPlayerT.setLooping(false);
//
//                        mPlayerList.add(mPlayerT);
//                    }
//
//                } catch (Exception e) {
//                    ULog.e(AudioPlayer.class, "playSound2 error:" + e.getMessage());
//                }
//
//            }
//            for (int i = 0; i < mPlayerList.size() - 1; i++) {
//                mPlayerList.get(i).setNextMediaPlayer(mPlayerList.get(i + 1));
//
//            }
//
//            mPlayerList.get(0).start();
//        } catch (Exception e) {
//            ULog.e(AudioPlayer.class, "playSound Error: " + e.getMessage());
//        }
//    }

}
