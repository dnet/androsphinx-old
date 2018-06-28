package org.hsbp.androsphinx;

import java.nio.charset.Charset;
import java.nio.*;
import java.util.Arrays;

public final class Sphinx {

	public final static byte SPHINX_255_SCALAR_BYTES = 32, SPHINX_255_SER_BYTES = 32;
	public final static byte crypto_sign_PUBLICKEYBYTES = 32, crypto_sign_SECRETKEYBYTES = 64;

	static {
		System.loadLibrary("test");
	}

	public final static class Challenge {
		public final byte[] blindingFactor, challenge;

		public Challenge(char[] pwd) {
			blindingFactor = new byte[SPHINX_255_SCALAR_BYTES];
			challenge = new byte[SPHINX_255_SER_BYTES];
			challenge(toBytes(pwd), blindingFactor, challenge);
		}
	}

	// SRC: https://stackoverflow.com/a/9670279

	private static byte[] toBytes(char[] chars) {
		CharBuffer charBuffer = CharBuffer.wrap(chars);
		ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
				byteBuffer.position(), byteBuffer.limit());
		Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
		Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
		return bytes;
	}

	public final static class KeyPair {
		public final byte[] publicKey, secretKey;

		public KeyPair() {
			publicKey = new byte[crypto_sign_PUBLICKEYBYTES];
			secretKey = new byte[crypto_sign_SECRETKEYBYTES];
			keyPair(publicKey, secretKey);
		}
	}

	private native static void challenge(byte[] pwd, byte[] bfac, byte[] chal);
	public native static byte[] respond(byte[] chal, byte[] secret);
	public native static byte[] finish(byte[] bfac, byte[] resp);

	public native static byte[] randomBytes(int bytes);
	public native static byte[] genericHash(byte[] data, byte[] salt, int length);
	private native static void keyPair(byte[] pk, byte[] sk);
}
