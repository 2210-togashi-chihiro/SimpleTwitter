package chapter6.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

/**
 * 暗号化ユーティリティー
 */
public class CipherUtil {

	/**
	 * SHA-256で暗号化し、バイト配列をBase64エンコーディングします。
	 *
	 * @param target
	 *            暗号化対象の文字列
	 *
	 * @return 暗号化された文字列
	 */
	public static String encrypt(String target) {

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(target.getBytes());
			return Base64.encodeBase64URLSafeString(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}

/*
暗号化には、キーで復号化することができるアルゴリズムと、単純に復号化できないアルゴリズムがあります。
今回は単純に復号化できない｢SHA-256」というアルゴリズムを利用しています。
「SHA-256｣は｢ハッ シュアルゴリズム｣などと呼ばれます。

暗号化した文字列はバイト配列(byte[])に変換されます。
バイト配列よりは文字列の方が扱いやすいので、
Base64でエンコードしているのが次のCipherUtilクラスになります 。
*/
