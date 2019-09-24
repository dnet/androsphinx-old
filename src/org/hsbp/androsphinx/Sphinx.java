package org.hsbp.androsphinx;

import java.nio.charset.Charset;
import java.nio.*;
import java.util.Arrays;

public final class Sphinx {

	public final static byte SPHINX_255_SCALAR_BYTES = 32, SPHINX_255_SER_BYTES = 32;
	public final static byte crypto_sign_PUBLICKEYBYTES = 32, crypto_sign_SECRETKEYBYTES = 64;

	static {
		System.loadLibrary("sphinxjni");
	}

	public final static class Challenge {
		public final byte[] blindingFactor, challenge;

		public Challenge(char[] pwd) {
			blindingFactor = new byte[SPHINX_255_SCALAR_BYTES];
			challenge = new byte[SPHINX_255_SER_BYTES];
			challenge(toBytes(pwd), blindingFactor, challenge);
		}
	}

	public static byte[] finish(char[] pwd, byte[] bfac, byte[] resp) {
		return finish(toBytes(pwd), bfac, resp);
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

		public KeyPair(byte[] secretKey) {
			if (secretKey.length != crypto_sign_SECRETKEYBYTES) {
				throw new IllegalArgumentException("Invalid key length");
			}
			this.secretKey = secretKey;
			publicKey = new byte[crypto_sign_PUBLICKEYBYTES];
			secretKeyToPublicKey(publicKey, secretKey);
		}

		public KeyPair(byte[] publicKey, byte[] secretKey) throws IllegalArgumentException {
			if (publicKey.length != crypto_sign_PUBLICKEYBYTES ||
					secretKey.length != crypto_sign_SECRETKEYBYTES) {
				throw new IllegalArgumentException("Invalid key length");
			}
			this.publicKey = publicKey;
			this.secretKey = secretKey;
		}

		public byte[] sign(byte[] message) {
			return Sphinx.sign(secretKey, message);
		}
	}

	private native static void challenge(byte[] pwd, byte[] bfac, byte[] chal);
	public native static byte[] respond(byte[] chal, byte[] secret);
	public native static byte[] finish(byte[] pwd, byte[] bfac, byte[] resp);

	public native static byte[] randomBytes(int bytes);
	public native static byte[] genericHash(byte[] data, byte[] salt, int length);
	private native static void keyPair(byte[] pk, byte[] sk);
	private native static void secretKeyToPublicKey(byte[] pk, byte[] sk);
	public native static byte[] sign(byte[] sk, byte[] message);
}
