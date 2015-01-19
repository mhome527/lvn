package teach.vietnam.asia.utils;

//import java.io.File;
//import java.io.FileInputStream;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptData {
	private SecretKeySpec skeySpec;
	private Cipher cipher;

	public EncryptData(byte[] keyraw) throws Exception {
		if (keyraw == null) {
			byte[] bytesOfMessage = "pZGUgRGV2ZWxvcGVyIFJlbGF0aW9uczR".getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(bytesOfMessage);

			skeySpec = new SecretKeySpec(bytes, "AES");
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		} else {

			skeySpec = new SecretKeySpec(keyraw, "AES");
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

		}
	}

	public EncryptData(String passphrase) throws Exception {
		byte[] bytesOfMessage = passphrase.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] thedigest = md.digest(bytesOfMessage);
		skeySpec = new SecretKeySpec(thedigest, "AES");

		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	}

	public EncryptData() throws Exception {
		byte[] bytesOfMessage = "".getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] thedigest = md.digest(bytesOfMessage);
		skeySpec = new SecretKeySpec(thedigest, "AES");

		skeySpec = new SecretKeySpec(new byte[16], "AES");
		cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	}

	public byte[] encrypt(byte[] plaintext) throws Exception {
		// returns byte array encrypted with key

		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

		byte[] ciphertext = cipher.doFinal(plaintext);

		return ciphertext;
	}

	public byte[] decrypt(byte[] ciphertext) throws Exception {
		// returns byte array decrypted with key
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);

		byte[] plaintext = cipher.doFinal(ciphertext);

		return plaintext;
	}

//	@SuppressWarnings("resource")
//	public static byte[] getData() {
//		File file = null;
//		FileInputStream fileStream;
//		// byte[] arrE, arrD;
//		try {
//			fileStream = new FileInputStream(file = new File("/home/huynhtran/test2.txt"));
//
//			// Instantiate array
//			byte[] arr = new byte[(int) file.length()];
//
//			// / read All bytes of File stream
//			fileStream.read(arr, 0, arr.length);
//
//			return arr;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

//	public static void main(String[] args) throws Exception {
//
//		EncryptData encrypter = new EncryptData(new byte[16]);
//
//		// byte[] encrypted = encrypter.encrypt(message.getBytes("UTF-8"));
//		byte[] encrypted = encrypter.encrypt(getData());
//		byte[] decrypted = encrypter.decrypt(encrypted);
//		System.out.println(new String(decrypted, "UTF-8"));
//
//	}

}
