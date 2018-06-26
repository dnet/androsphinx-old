package org.hsbp.androsphinx;

public class Main {
	public static void main(String[] args) {
		System.loadLibrary("test");
		byte[] bfac = new byte[32], chal = new byte[32];
		challenge("shitty password\0".getBytes(), bfac, chal);
		byte[] secret = new byte[32];
		for (int i = 0; i < secret.length; i++) secret[i] = (byte)' ';
		byte[] resp = respond(chal, secret);
		byte[] rwd = finish(bfac, resp);
		dump(rwd);
	}

	private static void dump(byte[] value) {
		for (int i = 0; i < value.length; i++) {
			System.out.print(String.format("%02x", value[i]));
		}
		System.out.println();
	}

	private native static void challenge(byte[] pwd, byte[] bfac, byte[] chal);
	private native static byte[] respond(byte[] chal, byte[] secret);
	private native static byte[] finish(byte[] bfac, byte[] resp);
}
