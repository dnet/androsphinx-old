package org.hsbp.androsphinx;

public class Main {
	public static void main(String[] args) {
		System.loadLibrary("test");
		Sphinx.ChallengeResult cr = Sphinx.challenge("shitty password\0".toCharArray());
		byte[] secret = new byte[32];
		for (int i = 0; i < secret.length; i++) secret[i] = (byte)' ';
		byte[] resp = Sphinx.respond(cr.challenge, secret);
		byte[] rwd = Sphinx.finish(cr.blindingFactor, resp);
		dump(rwd);
	}

	private static void dump(byte[] value) {
		for (int i = 0; i < value.length; i++) {
			System.out.print(String.format("%02x", value[i]));
		}
		System.out.println();
	}
}
