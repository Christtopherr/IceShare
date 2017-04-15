package br.univel.jshare.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class Util {

	
	public String getExtension(String name) {

		String extension = "";

		int i = name.lastIndexOf('.');
		if (i > 0) {
			extension = name.substring(i);
		}

		return extension;
	}
	
	public static byte[] createChecksum(String filename) throws Exception {
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	public static String getMD5Checksum(String filename) {
		byte[] b;
		try {
			b = createChecksum(filename);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String result = "";

		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	private String getString(byte[] buffer) {

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buffer.length; i++) {
			if ((0xff & buffer[i]) < 0x10) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(0xff & buffer[i]));
		}
		String md5 = sb.toString();

		return md5;
	}

	public String getNome(String nome) {

		int pos = nome.lastIndexOf(".");
		if (pos > 0) {
			nome = nome.substring(0, pos);
		}

		return nome;
	}

	public String getMD5(String path) {
		// TODO Auto-generated method stub
		return null;
	}

}
