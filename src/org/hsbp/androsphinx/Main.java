package org.hsbp.androsphinx;
import java.util.Arrays;

public class Main {
	public static void main(String[] args) {
		System.loadLibrary("test");
		Sphinx.Challenge c = new Sphinx.Challenge("shitty password\0".toCharArray());
		byte[] secret = new byte[32];
		Arrays.fill(secret, (byte)' ');
		byte[] resp = Sphinx.respond(c.challenge, secret);
		byte[] rwd = Sphinx.finish("shitty password\0".toCharArray(), c.blindingFactor, resp);
		dump(rwd);
	}

	private static void dump(byte[] value) {
		for (int i = 0; i < value.length; i++) {
			System.out.print(String.format("%02x", value[i]));
		}
		System.out.println();
	}
}
