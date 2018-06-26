package org.hsbp.androsphinx;

import java.nio.charset.Charset;
import java.nio.*;
import java.util.Arrays;

public final class Sphinx {

	public final static byte SPHINX_255_SCALAR_BYTES = 32, SPHINX_255_SER_BYTES = 32;

	static {
		System.loadLibrary("test");
	}

	public static ChallengeResult challenge(char[] pwd) {
		byte[] blindingFactor = new byte[SPHINX_255_SCALAR_BYTES],
				challenge = new byte[SPHINX_255_SER_BYTES];
		challenge(toBytes(pwd), blindingFactor, challenge);
		return new ChallengeResult(blindingFactor, challenge);
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

	public final static class ChallengeResult {
		public final byte[] blindingFactor, challenge;

		public ChallengeResult(byte[] blindingFactor, byte[] challenge) {
			this.blindingFactor = blindingFactor;
			this.challenge = challenge;
		}
	}

	private native static void challenge(byte[] pwd, byte[] bfac, byte[] chal);
	public native static byte[] respond(byte[] chal, byte[] secret);
	public native static byte[] finish(byte[] bfac, byte[] resp);
}
