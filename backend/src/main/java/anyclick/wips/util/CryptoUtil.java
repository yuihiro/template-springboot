package anyclick.wips.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptoUtil {

	private static final Logger log = LoggerFactory.getLogger(CryptoUtil.class);

	private static final String TRANSFORM = "AES/CBC/PKCS5Padding";
	private static final String HASH = "SHA-256";

	private static Cipher ecipher = null;
	private static Cipher dcipher = null;

	public static String encrypt(String text, String key) {
		String result = null;
		if (ecipher == null) {
			initCipher(key);
		}
		byte[] value = null;
		try {
			value = ecipher.doFinal(text.getBytes("UTF-8"));
			result = Base64.getEncoder().encodeToString(value);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String decrypt(String text, String key) {
		//log.debug("decrypt : " + text + "/" + key);
		String result = null;
		if (ecipher == null) {
			initCipher(key);
		}
		byte[] decodeText = Base64.getDecoder().decode(text.getBytes());
		byte[] value = null;
		try {
			value = dcipher.doFinal(decodeText);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		try {
			result = new String(value, "UTF8");
		} catch (UnsupportedEncodingException e) {
			log.debug("decrypt : " + text + "/" + key);
			e.printStackTrace();
		}
		return result;
	}

	private static void initCipher(String str) {
		try {
			ecipher = Cipher.getInstance(TRANSFORM);
			dcipher = Cipher.getInstance(TRANSFORM);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		SecretKeySpec keySpec = getKeySpec(str);
		IvParameterSpec ivSpec = getIvSpec();
		try {
			ecipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
			dcipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
	}

	private static SecretKeySpec getKeySpec(String str) {
		byte[] key = null;
		try {
			key = hash(str);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new SecretKeySpec(key, "AES");
	}

	private static IvParameterSpec getIvSpec() {
		byte[] iv = new byte[16];
		for (int i = 0; i < 16; i++) {
			iv[i] = 0x00;
		}
		return new IvParameterSpec(iv);
	}

	private static byte[] hash(String str) throws UnsupportedEncodingException {
		byte bytes[] = null;
		try {
			MessageDigest sh = MessageDigest.getInstance(HASH);
			sh.update(str.getBytes("UTF-8"), 0, str.length());
			bytes = sh.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return bytes;
	}

	private static String toHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}
}
