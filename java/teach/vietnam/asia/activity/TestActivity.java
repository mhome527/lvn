package teach.vietnam.asia.activity;

import android.app.Activity;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import teach.vietnam.asia.BuildConfig;
import teach.vietnam.asia.utils.EncryptData;
import teach.vietnam.asia.utils.ULog;

public class TestActivity extends Activity {
	private String fileName = "VN.txt";
	private String fileNameOut = "VN.data";
//	private String fileName = "MapName.txt";
//	private String fileNameOut = "MapName.data";
	boolean b = true; // encrypt
//	 boolean b = false; //decrypt


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		testFunction();
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
