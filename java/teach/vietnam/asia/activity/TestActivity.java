package teach.vietnam.asia.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import teach.vietnam.asia.BuildConfig;
import teach.vietnam.asia.entity.DaoMaster;
import teach.vietnam.asia.entity.tblMapName;
import teach.vietnam.asia.entity.tblMapNameDao;
import teach.vietnam.asia.entity.tblVietFR;
import teach.vietnam.asia.entity.tblVietFRDao;
import teach.vietnam.asia.utils.EncryptData;
import teach.vietnam.asia.utils.ULog;

public class TestActivity extends Activity {
    private static String TAG = "TestActivity";
	private String fileName = "VN.txt";
	private String fileNameOut = "VN.data";
//	private String fileName = "MapName.txt";
//	private String fileNameOut = "MapName.data";
	boolean b = true; // encrypt
//	 boolean b = false; //decrypt


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		testFunction();
//        testWord();
//		translate();
	}

	private void translate(){
		Intent i = new Intent();
		i.setAction(Intent.ACTION_VIEW);
		i.putExtra("key_text_input", "Me gusta la cerveza");
		i.putExtra("key_text_output", "");
		i.putExtra("key_language_from", "es");
		i.putExtra("key_language_to", "en");
		i.putExtra("key_suggest_translation", "");
		i.putExtra("key_from_floating_window", false);
		i.setComponent(
				new ComponentName(
						"com.google.android.apps.translate",
						"com.google.android.apps.translate.translation.TranslateActivity"));
		startActivity(i);
	}

    private void testWord(){
        DaoMaster daoMaster;
        tblMapNameDao dao;
//        tblVietRUDao daoRu;
        tblVietFRDao daoRu;
//        List<tblVietRU> lstRu;
        List<tblVietFR> lstRu;


        QueryBuilder<tblMapName> qb;
//        QueryBuilder<tblVietRU> qbRu;
        QueryBuilder<tblVietFR> qbRu;
        try {
            daoMaster = ((MyApplication) this.getApplicationContext()).daoMaster;

            dao = daoMaster.newSession().getTblMapNameDao();
//            qb = dao.queryBuilder();

//            daoRu = daoMaster.newSession().getTblVietRUDao();
//            qbRu = daoRu.queryBuilder();
//
// 		daoRu = daoMaster.newSession().getTblVietRUDao();
 		daoRu = daoMaster.newSession().getTblVietFRDao();
            qbRu = daoRu.queryBuilder();

            if (qbRu != null && qbRu.list().size() > 0) {
//                for(tblVietRU ru : qbRu.list()){
                for(tblVietFR ru : qbRu.list()){
                    String []strVi;
//                    ULog.i(this, "phrase: " + ru.getVi());
                    String strTmp = ru.getVi().replaceAll("!", "").replaceAll("\\?", "").replaceAll("[.]", " ").replaceAll(",", "").replaceAll("<u>", "").replaceAll("</u>", "").toLowerCase().toString();
					strTmp = strTmp.trim();
					strVi = strTmp.split(" ");
//					ULog.i(this, "phrase word = " + strTmp);

					for(String word : strVi){
                        qb = dao.queryBuilder();
						String word2 = word.trim();

                        qb.where(tblMapNameDao.Properties.Filename.in(word2));
                        if(qb.list().size()==0 && !word.equals(" ")){
                            ULog.i(this, "***************************** word = " + word2 +"=");
                        }
                    }

                }
            } else {
                ULog.i(TAG, "testWord NO data ");
            }

        } catch (Exception e) {
            ULog.e(TAG, "update fail error:" + e.getMessage());
        }
    }

	private void testFunction() {
		byte[] arrB;
		EncryptData eData;
		InputStream is;

		try {

			eData = new EncryptData(new byte[16]);
			if (b) {
				is = this.getAssets().open(fileName);

				byte[] fileBytes = new byte[is.available()];
				is.read(fileBytes);
				is.close();
				// arrB = Common.decrypt(fileBytes);
				// str = new String(arrB);

				writeGAToSDFile(fileNameOut, eData.encrypt(fileBytes));

			} else {
				is = this.getAssets().open(fileNameOut);
				byte[] fileBytes = new byte[is.available()];
				is.read(fileBytes);
				is.close();
				arrB = eData.decrypt(fileBytes);
				ULog.i(TestActivity.class, "testFunction text: " + new String(arrB));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void writeGAToSDFile(String filename, byte[] data) {
		String pathfile;


		try {

			pathfile = android.os.Environment.getExternalStorageDirectory() + "";
			ULog.i(TestActivity.class, "writeGAToSDFile:" + pathfile);
			File dir = new File(pathfile, "ENCRYPT_LVN");
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File f = new File(dir + File.separator + filename);
			FileOutputStream fOut = new FileOutputStream(f);
			fOut.write(data);
			fOut.close();
		} catch (FileNotFoundException e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();
			ULog.e(TestActivity.class, "writeGAToSDFile Error: " + e.getMessage());

		} catch (Exception e) {
			if (BuildConfig.DEBUG)
				e.printStackTrace();
			ULog.e(TestActivity.class, "writeGAToSDFile 2 Error: " + e.getMessage());
		}

	}


}
