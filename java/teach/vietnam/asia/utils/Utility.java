package teach.vietnam.asia.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.google.android.gms.analytics.HitBuilders;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import teach.vietnam.asia.BuildConfig;
import teach.vietnam.asia.R;
import teach.vietnam.asia.activity.MyApplication;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.tblRecEN;
import teach.vietnam.asia.entity.tblRecENDao;
import teach.vietnam.asia.entity.tblRecES;
import teach.vietnam.asia.entity.tblRecESDao;
import teach.vietnam.asia.entity.tblRecFR;
import teach.vietnam.asia.entity.tblRecFRDao;
import teach.vietnam.asia.entity.tblRecIT;
import teach.vietnam.asia.entity.tblRecITDao;
import teach.vietnam.asia.entity.tblRecJA;
import teach.vietnam.asia.entity.tblRecJADao;
import teach.vietnam.asia.entity.tblRecKO;
import teach.vietnam.asia.entity.tblRecKODao;
import teach.vietnam.asia.entity.tblRecRU;
import teach.vietnam.asia.entity.tblRecRUDao;
import teach.vietnam.asia.entity.tblVietEN;
import teach.vietnam.asia.entity.tblVietENDao;
import teach.vietnam.asia.entity.tblVietES;
import teach.vietnam.asia.entity.tblVietESDao;
import teach.vietnam.asia.entity.tblVietFR;
import teach.vietnam.asia.entity.tblVietFRDao;
import teach.vietnam.asia.entity.tblVietIT;
import teach.vietnam.asia.entity.tblVietITDao;
import teach.vietnam.asia.entity.tblVietJA;
import teach.vietnam.asia.entity.tblVietJADao;
import teach.vietnam.asia.entity.tblVietKO;
import teach.vietnam.asia.entity.tblVietKODao;
import teach.vietnam.asia.entity.tblVietRU;
import teach.vietnam.asia.entity.tblVietRUDao;

public class Utility {
    public static int parseInt(final String num) {
        try {
            return Integer.parseInt(num);
        } catch (final Exception e) {
            return -1;
        }
    }

    public static String ArrToString(String[] arr) {
        String str = "";
        if (arr == null || arr.length == 0)
            return "";
        if (arr.length == 1) {
            str = arr[0];
        } else {
            for (int i = 0; i < arr.length - 1; i++) {
                if (!arr[i].contains("ttt"))
                    str += arr[i] + " - ";
            }
            str += arr[arr.length - 1];
        }
        return str;
    }

    public static AlertDialog dialogWifi(final Activity activity) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // create layout for dialog
        LinearLayout layout = new LinearLayout(activity);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(parms);
        layout.setGravity(Gravity.CLIP_VERTICAL);
        layout.setPadding(2, 2, 2, 2);

        TextView tv = new TextView(activity);
        tv.setText(activity.getString(R.string.connect_wifi));
        tv.setPadding(10, 30, 10, 30);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(16);

        // create layout for textview
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.addView(tv, tvParams);

        builder.setView(layout);
        builder.setPositiveButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(activity.getString(R.string.turn_on_wifi), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
                activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    // Check if Internet Network is active
    public static boolean checkNetwork(Activity activity) {
        boolean wifiDataAvailable = false;
        boolean mobileDataAvailable = false;
        ConnectivityManager conManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = conManager.getAllNetworkInfo();
        for (NetworkInfo netInfo : networkInfo) {
            if (netInfo.getTypeName().equalsIgnoreCase("WIFI"))
                if (netInfo.isConnected())
                    wifiDataAvailable = true;
            if (netInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                if (netInfo.isConnected())
                    mobileDataAvailable = true;
        }
        return wifiDataAvailable || mobileDataAvailable;
    }

    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                ULog.e("Utility", "Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }

//	/**
//	 * set event tracking
//	 *
//	 * @param category
//	 *            : The event category
//	 * @param label
//	 *            : The event label
//	 */
//	public static void setEventGA(String action, String label) {
//		try {
//			if (!BuildConfig.DEBUG) {
//				MyApplication mInstance = MyApplication.getInstance();
//				Tracker tracker = mInstance.getTrackerApp();
//				tracker.send(new HitBuilders.EventBuilder().setCategory("BUS").setAction(action).setLabel(label).build());
//			}
//
//		} catch (Exception e) {
//			ULog.e("Utility", "setEventGA Error:" + e.getMessage());
//		}
//	}
//
//	public static void setScreenNameGA(String name) {
//		// int count;
//		try {
//			if (!BuildConfig.DEBUG) {
//				MyApplication mInstance = MyApplication.getInstance();
//				Tracker tracker = mInstance.getTrackerApp();
//				tracker.set(Fields.SCREEN_NAME, name);
//				tracker.send(MapBuilder.createAppView().build());
//			}
//		} catch (Exception e) {
//			ULog.e("Utility", "setScreenNameGA Error:" + e.getMessage());
//		}
//	}

    public static int getResId(String variableName, Context context, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean checkGps(Context context) {
        try {
            ULog.i("Utility", "checkGPS");
            LocationManager myLocManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (myLocManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER))
                return true;
        } catch (Exception e) {
            ULog.e("Utility", "checkGps error:" + e.getMessage());
        }
        return false;
    }

//    public static void showDialogGPS(final Activity context) {
//        // DisplayMetrics displaymetrics = new DisplayMetrics();
//        // getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        // int width = (int) (displaymetrics.widthPixels * 0.94);
//        // int height = (int) (displaymetrics.heightPixels * 0.77);
//        //
//        // WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
//        // params.width = width;
//        // params.height = height;
//        // getActivity().getWindow().setAttributes(params);
//
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_gps);
//
//        dialog.show();
//        dialog.setCanceledOnTouchOutside(false);
//        // LinearLayout lnNotice = (LinearLayout)dialog.findViewById(R.id.lnNotice);
//        TextView tvMsg = (TextView) dialog.findViewById(R.id.tvMsg);
//        Button btnSettings = (Button) dialog.findViewById(R.id.btnSettings);
//        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
//
//        //
//        // lnNotice.getLayoutParams().width = width;
//        // lnNotice.getLayoutParams().height = height;
//        tvMsg.setText(String.format(context.getString(R.string.msg_alert_gps), context.getString(R.string.app_name)));
//        btnSettings.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                dialog.dismiss();
//                Intent gpsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                context.startActivity(gpsIntent);
//            }
//        });
//
//        btnCancel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View arg0) {
//                dialog.dismiss();
//            }
//        });
//
//    }

    public static int getResourcesID(Context context, String name) {
        try {
            if (name.equals(""))
                return -1;
            return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        } catch (Exception e) {
            ULog.e("Utility", "get resource error:" + e.getMessage());
            return -1;
        }
    }

    public static int randomPos(int lenght, int... params) {
        Random r;
        int i;
        boolean b = false;

        for (int j = 0; j < 100; j++) {
            r = new Random();
            i = r.nextInt(lenght);
            for (int value : params) {
                if (i == value) {
                    b = false;
                    break;
                } else
                    b = true;
            }
            if (b)
                return i;
        }
        return 0;
    }

    public static boolean isSpeechRecognition(Context context) {
        try {
            // Check to see if a recognition activity is present
            PackageManager pm = context.getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
            if (activities == null || activities.size() == 0) {
                ULog.e(Utility.class, "checkSpeed NOT SUPORT!!!");
                return false;
            } else
                return true;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * Asking the permission for installing Google Voice Search. If permission granted – sent user to Google Play
     *
     * @param context – Activity, that initialized installing
     */
    public static void installGoogleVoiceSearch(final Context context) {

        // creating a dialog asking user if he want to install the Voice Search
        Dialog dialog = new AlertDialog.Builder(context).setMessage(context.getString(R.string.msg_recognition_voice))
                .setTitle(context.getString(R.string.msg_title_recognition))
                .setPositiveButton(context.getString(R.string.install), new DialogInterface.OnClickListener() {

                    // Install Button click handler
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            // creating an Intent for opening applications page in Google Play
                            // Voice Search package name: com.google.android.voicesearch
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.PACKAGE_VOICE));
                            // setting flags to avoid going in application history (Activity call stack)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            // sending an Intent
                            context.startActivity(intent);
                        } catch (Exception ex) {
                            // if something going wrong doing nothing
                        }
                    }
                }).setNegativeButton(context.getString(R.string.cancel), null).create();

        dialog.show();
    }

    /**
     * Asking the permission for installing Premium app. If permission granted – sent user to Google Play
     *
     * @param context – Activity, that initialized installing
     */
    public static void installPremiumApp(final Context context) {

        // creating a dialog asking user if he want to install the Voice Search
        Dialog dialog = new AlertDialog.Builder(context).setMessage(context.getString(R.string.msg_premium_app))
                .setTitle(context.getString(R.string.msg_title_premium))
                .setPositiveButton(context.getString(R.string.install), new DialogInterface.OnClickListener() {

                    // Install Button click handler
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            // creating an Intent for opening applications page in Google Play
                            // Voice Search package name: com.google.android.voicesearch
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.PACKAGE_PREMIUM));
                            // setting flags to avoid going in application history (Activity call stack)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            // sending an Intent
                            context.startActivity(intent);
                        } catch (Exception ex) {
                            // if something going wrong doing nothing
                        }
                    }
                }).setNegativeButton(context.getString(R.string.cancel), null).create();

        dialog.show();
    }

    /**
     * Asking the permission for installing Premium app. If permission granted – sent user to Google Play
     *
     * @param context – Activity, that initialized installing
     */
    public static void confirmUpdate(final Context context) {

        // creating a dialog asking user if he want to install the Voice Search
        Dialog dialog = new AlertDialog.Builder(context).setMessage(context.getString(R.string.msg_update_app))
                .setTitle(context.getString(R.string.msg_title_update))
                .setPositiveButton(context.getString(R.string.update), new DialogInterface.OnClickListener() {

                    // Install Button click handler
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            // creating an Intent for opening applications page in Google Play
                            // Voice Search package name: com.google.android.voicesearch
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.PACKAGE_UPDATE));
                            // setting flags to avoid going in application history (Activity call stack)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                            // sending an Intent
                            context.startActivity(intent);
                        } catch (Exception ex) {
                            // if something going wrong doing nothing
                        }
                    }
                }).setNegativeButton(context.getString(R.string.cancel), null).create();

        dialog.show();
    }

    public static void confirmCloseApp(final Activity activity) {

        // creating a dialog asking user if he want to install the Voice Search
        Dialog dialog = new AlertDialog.Builder(activity).setMessage(activity.getString(R.string.configm_replay))
                .setTitle(activity.getString(R.string.vietnamese))
                .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {

                    // Install Button click handler
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            activity.finish();
                        } catch (Exception ex) {
                            // if something going wrong doing nothing
                        }
                    }
                }).setNegativeButton(activity.getString(R.string.cancel), null).create();

        dialog.show();
    }

    /**
     * Showing google speech input dialog
     */
    public static void promptSpeechInput(Activity activity, int result) {
        ULog.i(Utility.class, "input locate: " + Locale.getDefault());
        if (!Utility.isSpeechRecognition(activity)) {
            Utility.installGoogleVoiceSearch(activity);
            return;
        }
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, activity.getString(R.string.speech_prompt));
        try {
            activity.startActivityForResult(intent, result);
        } catch (ActivityNotFoundException a) {
            ULog.e(Utility.class, "input error:" + a.getMessage());
            a.printStackTrace();
            Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Showing google speech input dialog
     */
    public static void promptSpeechInput(Activity activity, int result, String lang) {
        ULog.i(Utility.class, "input locate: " + Locale.getDefault());
        if (!Utility.isSpeechRecognition(activity)) {
            Utility.installGoogleVoiceSearch(activity);
            return;
        }
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, lang);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, activity.getString(R.string.speech_prompt));
        try {
            activity.startActivityForResult(intent, result);
        } catch (ActivityNotFoundException a) {
            ULog.e(Utility.class, "input error:" + a.getMessage());
            a.printStackTrace();
            Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    public static String stripNonDigits(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c > 47 && c < 58) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static long convertToLong(String number) {
        try {
            return Long.parseLong(number);
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getMacAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wInfo = wifiManager.getConnectionInfo();
            return wInfo.getMacAddress();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * set event tracking
     */
    public static void setEventGA(String action, String label) {
        try {
            if (!BuildConfig.DEBUG) {
                MyApplication mInstance = MyApplication.getInstance();
                Tracker tracker = mInstance.getTrackerApp();
                tracker.send(new HitBuilders.EventBuilder().setCategory("BUS").setAction(action).setLabel(label).build());
            }

        } catch (Exception e) {
            ULog.e("Utility", "setEventGA Error:" + e.getMessage());
        }
    }

    public static void setScreenNameGA(String name) {
        // int count;
        try {
            if (!BuildConfig.DEBUG) {
                MyApplication mInstance = MyApplication.getInstance();
                Tracker tracker = mInstance.getTrackerApp();
                tracker.set(Fields.SCREEN_NAME, name + " - lang:" + Locale.getDefault().getLanguage());
                tracker.send(MapBuilder.createAppView().build());
            }
        } catch (Exception e) {
            ULog.e("Utility", "setScreenNameGA Error:" + e.getMessage());
        }
    }

//    public static void copyDataBase(Context context) {
//        ULog.i("Database",
//                "New database is being copied to device!");
////        getApplicationContext()
//        ContextWrapper cw =new ContextWrapper(context);
//        String DB_PATH =cw.getFilesDir().getAbsolutePath()+ "/databases/"; //edited to databases
//        byte[] buffer = new byte[1024];
//        OutputStream myOutput = null;
//        int length;
//        // Open your local db as the input stream
//        InputStream myInput = null;
//        try {
//            myInput = context.getAssets().open(Constant.DB_NAME);
//            // transfer bytes from the inputfile to the
//            // outputfile
//            myOutput = new FileOutputStream(DB_PATH + Constant.DB_NAME);
//            while ((length = myInput.read(buffer)) > 0) {
//                myOutput.write(buffer, 0, length);
//            }
//            myOutput.close();
//            myOutput.flush();
//            myInput.close();
//            ULog.i("Database", "New database has been copied to device!");
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    //////

    public static AbstractDao getDao(Context context, String lang) {
        AbstractDao dao;
        DaoMaster daoMaster = ((MyApplication) context.getApplicationContext()).daoMaster;

        if (lang.equals("ja"))
            dao = daoMaster.newSession().getTblVietJADao();
        else if (lang.equals("ko"))
            dao = daoMaster.newSession().getTblVietKODao();
        else if (lang.equals("ru"))
            dao = daoMaster.newSession().getTblVietRUDao();
        else if (lang.equals("fr"))
            dao = daoMaster.newSession().getTblVietFRDao();
        else if (lang.equals("it"))
            dao = daoMaster.newSession().getTblVietITDao();
        else if (lang.equals("es"))
            dao = daoMaster.newSession().getTblVietESDao();
        else
            dao = daoMaster.newSession().getTblVietENDao();
        return dao;
    }

    public static String getTableName(String lang) {
        Property p;
        if (lang.equals("ja"))
            return tblVietJADao.TABLENAME;
        else if (lang.equals("ko"))
            return tblVietKODao.TABLENAME;
        else if (lang.equals("ru"))
            return tblVietRUDao.TABLENAME;
        else if (lang.equals("fr"))
            return tblVietFRDao.TABLENAME;
        else if (lang.equals("it"))
            return tblVietITDao.TABLENAME;
        else if (lang.equals("es"))
            return tblVietESDao.TABLENAME;
        else
            return tblVietENDao.TABLENAME;
    }

    public static Property getO1(String lang) {
        Property p;
        if (lang.equals("ja"))
            p = tblVietJADao.Properties.O1;
        else if (lang.equals("ko"))
            p = tblVietKODao.Properties.O1;
        else if (lang.equals("ru"))
            p = tblVietRUDao.Properties.O1;
        else if (lang.equals("fr"))
            p = tblVietFRDao.Properties.O1;
        else if (lang.equals("it"))
            p = tblVietITDao.Properties.O1;
        else if (lang.equals("es"))
            p = tblVietESDao.Properties.O1;
        else
            p = tblVietENDao.Properties.O1;
        return p;
    }

    public static Property getO2(String lang) {
        Property p;
        if (lang.equals("ja"))
            p = tblVietJADao.Properties.O2;
        else if (lang.equals("ko"))
            p = tblVietKODao.Properties.O2;
        else if (lang.equals("ru"))
            p = tblVietRUDao.Properties.O2;
        else if (lang.equals("fr"))
            p = tblVietFRDao.Properties.O2;
        else if (lang.equals("it"))
            p = tblVietITDao.Properties.O2;
        else if (lang.equals("es"))
            p = tblVietESDao.Properties.O2;
        else
            p = tblVietENDao.Properties.O2;
        return p;
    }

    public static Property getKind(String lang) {
        Property p;
        if (lang.equals("ja"))
            p = tblVietJADao.Properties.Kind;
        else if (lang.equals("ko"))
            p = tblVietKODao.Properties.Kind;
        else if (lang.equals("ru"))
            p = tblVietRUDao.Properties.Kind;
        else if (lang.equals("fr"))
            p = tblVietFRDao.Properties.Kind;
        else if (lang.equals("it"))
            p = tblVietITDao.Properties.Kind;
        else if (lang.equals("es"))
            p = tblVietESDao.Properties.Kind;
        else
            p = tblVietENDao.Properties.Kind;
        return p;
    }

    public static Property getLevel(String lang) {
        Property p;
        if (lang.equals("ja"))
            p = tblVietJADao.Properties.Level;
        else if (lang.equals("ko"))
            p = tblVietKODao.Properties.Level;
        else if (lang.equals("ru"))
            p = tblVietRUDao.Properties.Level;
        else if (lang.equals("fr"))
            p = tblVietFRDao.Properties.Level;
        else if (lang.equals("it"))
            p = tblVietITDao.Properties.Level;
        else if (lang.equals("es"))
            p = tblVietESDao.Properties.Level;
        else
            p = tblVietENDao.Properties.Level;
        return p;
    }

    /**
     * get filename by language
     *
     * @param lang
     * @return
     */
    public static String getFileNameDB(String lang) {

        if (lang.equals("ja"))
            return Constant.FILE_JA;
        else if (lang.equals("ko"))
            return Constant.FILE_KO;
        else if (lang.equals("ru"))
            return Constant.FILE_RU;
        else if (lang.equals("fr"))
            return Constant.FILE_FR;
        else if (lang.equals("it"))
            return Constant.FILE_IT;
        else if (lang.equals("es"))
            return Constant.FILE_ES;
        else
            return Constant.FILE_EN;
    }

    public static void deleteTableLang(String lang, SQLiteDatabase db) {
        if (lang.equals("ja"))
            tblVietJADao.dropTable(db, true);
        else if (lang.equals("ko"))
            tblVietKODao.dropTable(db, true);
        else if (lang.equals("ru"))
            tblVietRUDao.dropTable(db, true);
        else if (lang.equals("fr"))
            tblVietFRDao.dropTable(db, true);
        else if (lang.equals("it"))
            tblVietITDao.dropTable(db, true);
        else if (lang.equals("es"))
            tblVietESDao.dropTable(db, true);
        else
            tblVietENDao.dropTable(db, true);
    }

    public static void CreateTableLang(String lang, SQLiteDatabase db) {
        if (lang.equals("ja"))
            tblVietJADao.createTable(db, true);
        else if (lang.equals("ko"))
            tblVietKODao.createTable(db, true);
        else if (lang.equals("ru"))
            tblVietRUDao.createTable(db, true);
        else if (lang.equals("fr"))
            tblVietFRDao.createTable(db, true);
        else if (lang.equals("it"))
            tblVietITDao.createTable(db, true);
        else if (lang.equals("es"))
            tblVietESDao.createTable(db, true);
        else
            tblVietENDao.createTable(db, true);
    }


    public static String getImg(Object entity, String lang) {
        if (lang.equals(Constant.JA))
            return ((tblVietJA) entity).getImg();
        else if (lang.equals(Constant.KO))
            return ((tblVietKO) entity).getImg();
        else if (lang.equals(Constant.FR))
            return ((tblVietFR) entity).getImg();
        else if (lang.equals(Constant.RU))
            return ((tblVietRU) entity).getImg();
        else if (lang.equals(Constant.IT))
            return ((tblVietIT) entity).getImg();
        else if (lang.equals(Constant.ES))
            return ((tblVietES) entity).getImg();
        else
            return ((tblVietEN) entity).getImg();
    }

    public static String getVi(Object entity, String lang) {
        if (lang.equals(Constant.JA))
            return ((tblVietJA) entity).getVi();
        else if (lang.equals(Constant.KO))
            return ((tblVietKO) entity).getVi();
        else if (lang.equals(Constant.FR))
            return ((tblVietFR) entity).getVi();
        else if (lang.equals(Constant.RU))
            return ((tblVietRU) entity).getVi();
        else if (lang.equals(Constant.IT))
            return ((tblVietIT) entity).getVi();
        else if (lang.equals(Constant.ES))
            return ((tblVietES) entity).getVi();
        else
            return ((tblVietEN) entity).getVi();
    }

    public static String getO1(Object entity, String lang) {
        if (lang.equals(Constant.JA))
            return ((tblVietJA) entity).getO1();
        else if (lang.equals(Constant.KO))
            return ((tblVietKO) entity).getO1();
        else if (lang.equals(Constant.FR))
            return ((tblVietFR) entity).getO1();
        else if (lang.equals(Constant.RU))
            return ((tblVietRU) entity).getO1();
        else if (lang.equals(Constant.IT))
            return ((tblVietIT) entity).getO1();
        else if (lang.equals(Constant.ES))
            return ((tblVietES) entity).getO1();
        else
            return ((tblVietEN) entity).getO1();
    }

    public static String getO2(Object entity, String lang) {
        if (lang.equals(Constant.JA))
            return ((tblVietJA) entity).getO2();
        else if (lang.equals(Constant.KO))
            return ((tblVietKO) entity).getO2();
        else if (lang.equals(Constant.FR))
            return ((tblVietFR) entity).getO2();
        else if (lang.equals(Constant.RU))
            return ((tblVietRU) entity).getO2();
        else if (lang.equals(Constant.IT))
            return ((tblVietIT) entity).getO2();
        else if (lang.equals(Constant.ES))
            return ((tblVietES) entity).getO2();
        else
            return ((tblVietEN) entity).getO2();
    }

    public static String getDefaultWord(Object entity, String lang) {
        if (lang.equals(Constant.JA))
            return ((tblVietJA) entity).getDefault_word();
        else if (lang.equals(Constant.KO))
            return ((tblVietKO) entity).getDefault_word();
        else if (lang.equals(Constant.FR))
            return ((tblVietFR) entity).getDefault_word();
        else if (lang.equals(Constant.RU))
            return ((tblVietRU) entity).getDefault_word();
        else if (lang.equals(Constant.IT))
            return ((tblVietIT) entity).getDefault_word();
        else if (lang.equals(Constant.ES))
            return ((tblVietES) entity).getDefault_word();
        else
            return ((tblVietEN) entity).getDefault_word();
    }

    //(String vi, Integer kind, String img, String o1, String o2, Integer level, String default_word)
    public static Object getDataObject(String lang, String vi, String o1) {
        if (lang.equals(Constant.JA))
            return new tblVietJA(vi, 0, "", o1, "", 0, "");
        else if (lang.equals(Constant.KO))
            return new tblVietKO(vi, 0, "", o1, "", 0, "");
        else if (lang.equals(Constant.FR))
            return new tblVietFR(vi, 0, "", o1, "", 0, "");
        else if (lang.equals(Constant.RU))
            return new tblVietRU(vi, 0, "", o1, "", 0, "");
        else if (lang.equals(Constant.IT))
            return new tblVietIT(vi, 0, "", o1, "", 0, "");
        else if (lang.equals(Constant.ES))
            return new tblVietES(vi, 0, "", o1, "", 0, "");
        else
            return new tblVietEN(vi, 0, "", o1, "", 0, "");
    }

    public static void setDataObject(String lang, Object object, String default_word) {
        if (lang.equals(Constant.JA)) {
            tblVietJA vn = (tblVietJA) object;
            vn.setDefault_word(default_word);
        } else if (lang.equals(Constant.KO)) {
            tblVietKO vn = (tblVietKO) object;
            vn.setDefault_word(default_word);
        } else if (lang.equals(Constant.FR)) {
            tblVietFR vn = (tblVietFR) object;
            vn.setDefault_word(default_word);
        } else if (lang.equals(Constant.RU)) {
            tblVietRU vn = (tblVietRU) object;
            vn.setDefault_word(default_word);
        } else if (lang.equals(Constant.IT)) {
            tblVietIT vn = (tblVietIT) object;
            vn.setDefault_word(default_word);
        } else if (lang.equals(Constant.ES)) {
            tblVietES vn = (tblVietES) object;
            vn.setDefault_word(default_word);
        } else {
            tblVietEN vn = (tblVietEN) object;
            vn.setDefault_word(default_word);
        }
    }


    ///////////recognize

    public static AbstractDao getRecDao(Context context, String lang) {
        AbstractDao dao;
        DaoMaster daoMaster = ((MyApplication) context.getApplicationContext()).daoMaster;

        if (lang.equals("ja"))
            dao = daoMaster.newSession().getTblRecJADao();
        else if (lang.equals("ko"))
            dao = daoMaster.newSession().getTblRecKODao();
        else if (lang.equals("ru"))
            dao = daoMaster.newSession().getTblRecRUDao();
        else if (lang.equals("fr"))
            dao = daoMaster.newSession().getTblRecFRDao();
        else if (lang.equals("it"))
            dao = daoMaster.newSession().getTblRecITDao();
        else if (lang.equals("es"))
            dao = daoMaster.newSession().getTblRecESDao();
        else
            dao = daoMaster.newSession().getTblRecENDao();
        return dao;
    }

    /**
     * delete table Rec
     *
     * @param lang
     * @param db
     */
    public static void deleteTableRec(String lang, SQLiteDatabase db) {
        if (lang.equals("ja"))
            tblRecJADao.dropTable(db, true);
        else if (lang.equals("ko"))
            tblRecKODao.dropTable(db, true);
        else if (lang.equals("ru"))
            tblRecRUDao.dropTable(db, true);
        else if (lang.equals("fr"))
            tblRecFRDao.dropTable(db, true);
        else if (lang.equals("it"))
            tblRecITDao.dropTable(db, true);
        else if (lang.equals("es"))
            tblRecESDao.dropTable(db, true);
        else
            tblRecENDao.dropTable(db, true);
    }

    /**
     * create table Rec
     *
     * @param lang
     * @param db
     */
    public static void CreateTableRec(String lang, SQLiteDatabase db) {
        if (lang.equals("ja"))
            tblRecJADao.createTable(db, true);
        else if (lang.equals("ko"))
            tblRecKODao.createTable(db, true);
        else if (lang.equals("ru"))
            tblRecRUDao.createTable(db, true);
        else if (lang.equals("fr"))
            tblRecFRDao.createTable(db, true);
        else if (lang.equals("it"))
            tblRecITDao.createTable(db, true);
        else if (lang.equals("es"))
            tblRecESDao.createTable(db, true);
        else
            tblRecENDao.createTable(db, true);
    }

    ///
    public static String getREC_VN(Object entity, String lang) {
        if (lang.equals(Constant.JA))
            return ((tblRecJA) entity).getVn();
        else if (lang.equals(Constant.KO))
            return ((tblRecKO) entity).getVn();
        else if (lang.equals(Constant.FR))
            return ((tblRecFR) entity).getVn();
        else if (lang.equals(Constant.RU))
            return ((tblRecRU) entity).getVn();
        else if (lang.equals(Constant.IT))
            return ((tblRecIT) entity).getVn();
        else if (lang.equals(Constant.ES))
            return ((tblRecES) entity).getVn();
        else
            return ((tblRecEN) entity).getVn();
    }

    ///recognize
    public static String getREC_Ex(Object entity, String lang) {
        if (lang.equals(Constant.JA))
            return ((tblRecJA) entity).getEx();
        else if (lang.equals(Constant.KO))
            return ((tblRecKO) entity).getEx();
        else if (lang.equals(Constant.FR))
            return ((tblRecFR) entity).getEx();
        else if (lang.equals(Constant.RU))
            return ((tblRecRU) entity).getEx();
        else if (lang.equals(Constant.IT))
            return ((tblRecIT) entity).getEx();
        else if (lang.equals(Constant.ES))
            return ((tblRecES) entity).getEx();
        else
            return ((tblRecEN) entity).getEx();
    }

    ///recognize
    public static String getREC_Ot(Object entity, String lang) {
        if (lang.equals(Constant.JA))
            return ((tblRecJA) entity).getOt();
        else if (lang.equals(Constant.KO))
            return ((tblRecKO) entity).getOt();
        else if (lang.equals(Constant.FR))
            return ((tblRecFR) entity).getOt();
        else if (lang.equals(Constant.RU))
            return ((tblRecRU) entity).getOt();
        else if (lang.equals(Constant.IT))
            return ((tblRecIT) entity).getOt();
        else if (lang.equals(Constant.ES))
            return ((tblRecES) entity).getOt();
        else
            return ((tblRecEN) entity).getOt();
    }

    ///recognize
    public static Property getREC_GroupID(String lang) {
        if (lang.equals(Constant.JA))
            return tblRecJADao.Properties.Group_id;
        else if (lang.equals(Constant.KO))
            return tblRecKODao.Properties.Group_id;
        else if (lang.equals(Constant.FR))
            return tblRecFRDao.Properties.Group_id;
        else if (lang.equals(Constant.RU))
            return tblRecRUDao.Properties.Group_id;
        else if (lang.equals(Constant.IT))
            return tblRecITDao.Properties.Group_id;
        else if (lang.equals(Constant.ES))
            return tblRecESDao.Properties.Group_id;
        else
            return tblRecENDao.Properties.Group_id;
    }

    public static String getRecTableName(String lang) {
        Property p;
        if (lang.equals("ja"))
            return tblRecJADao.TABLENAME;
        else if (lang.equals("ko"))
            return tblRecKODao.TABLENAME;
        else if (lang.equals("ru"))
            return tblRecRUDao.TABLENAME;
        else if (lang.equals("fr"))
            return tblRecFRDao.TABLENAME;
        else if (lang.equals("it"))
            return tblRecITDao.TABLENAME;
        else if (lang.equals("es"))
            return tblRecESDao.TABLENAME;
        else
            return tblRecENDao.TABLENAME;
    }

    /**
     * get filename by language
     *
     * @param lang
     * @return
     */
    public static String getFileNameRecDB(String lang) {

        if (lang.equals("ja"))
            return Constant.REC_JA;
        else if (lang.equals("ko"))
            return Constant.REC_KO;
        else if (lang.equals("ru"))
            return Constant.REC_RU;
        else if (lang.equals("fr"))
            return Constant.REC_FR;
        else if (lang.equals("it"))
            return Constant.REC_IT;
        else if (lang.equals("es"))
            return Constant.REC_ES;
        else
            return Constant.REC_EN;
    }

    //(Integer group_id, Integer word_id, String vn, String ex, String ot)
    public static Object getRecDataObject(String lang, int group_id, int word_id, String vn, String ex, String ot) {
        if (lang.equals(Constant.JA))
            return new tblRecJA(group_id, word_id, vn, ex, ot);
        else if (lang.equals(Constant.KO))
            return new tblRecKO(group_id, word_id, vn, ex, ot);
        else if (lang.equals(Constant.FR))
            return new tblRecFR(group_id, word_id, vn, ex, ot);
        else if (lang.equals(Constant.RU))
            return new tblRecRU(group_id, word_id, vn, ex, ot);
        else if (lang.equals(Constant.IT))
            return new tblRecIT(group_id, word_id, vn, ex, ot);
        else if (lang.equals(Constant.ES))
            return new tblRecES(group_id, word_id, vn, ex, ot);
        else
            return new tblRecEN(group_id, word_id, vn, ex, ot);
    }

    public static Object getRecDataObject(String lang, Object obj) {
        if (lang.equals(Constant.JA)) {
            tblRecJA obj1 = (tblRecJA) obj;
            return new tblRecJA(obj1.getGroup_id(), obj1.getWord_id(), obj1.getVn(), obj1.getEx(), obj1.getOt());
        } else if (lang.equals(Constant.KO)) {
            tblRecKO obj1 = (tblRecKO) obj;
            return new tblRecKO(obj1.getGroup_id(), obj1.getWord_id(), obj1.getVn(), obj1.getEx(), obj1.getOt());
        } else if (lang.equals(Constant.FR)) {
            tblRecFR obj1 = (tblRecFR) obj;
            return new tblRecFR(obj1.getGroup_id(), obj1.getWord_id(), obj1.getVn(), obj1.getEx(), obj1.getOt());
        } else if (lang.equals(Constant.RU)) {
            tblRecRU obj1 = (tblRecRU) obj;
            return new tblRecRU(obj1.getGroup_id(), obj1.getWord_id(), obj1.getVn(), obj1.getEx(), obj1.getOt());
        } else if (lang.equals(Constant.IT)) {
            tblRecIT obj1 = (tblRecIT) obj;
            return new tblRecIT(obj1.getGroup_id(), obj1.getWord_id(), obj1.getVn(), obj1.getEx(), obj1.getOt());
        } else if (lang.equals(Constant.ES)) {
            tblRecES obj1 = (tblRecES) obj;
            return new tblRecES(obj1.getGroup_id(), obj1.getWord_id(), obj1.getVn(), obj1.getEx(), obj1.getOt());
        } else {
            tblRecEN obj1 = (tblRecEN) obj;
            return new tblRecEN(obj1.getGroup_id(), obj1.getWord_id(), obj1.getVn(), obj1.getEx(), obj1.getOt());
        }
    }
}
