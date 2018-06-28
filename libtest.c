#include <jni.h>
#include "sphinx.h"
#include <sodium.h>

JNIEXPORT void JNICALL Java_org_hsbp_androsphinx_Sphinx_challenge(JNIEnv *env, jobject ignore, jbyteArray pwd, jbyteArray bfac, jbyteArray chal) {
	jbyte* bufferPtrPwd = (*env)->GetByteArrayElements(env, pwd, NULL);
	jbyte* bufferPtrBfac = (*env)->GetByteArrayElements(env, bfac, NULL);
	jbyte* bufferPtrChal = (*env)->GetByteArrayElements(env, chal, NULL);
	jsize pwdLen = (*env)->GetArrayLength(env, pwd);

	sphinx_challenge(bufferPtrPwd, pwdLen, bufferPtrBfac, bufferPtrChal);

	(*env)->ReleaseByteArrayElements(env, pwd, bufferPtrPwd, JNI_ABORT);
	(*env)->ReleaseByteArrayElements(env, bfac, bufferPtrBfac, 0);
	(*env)->ReleaseByteArrayElements(env, chal, bufferPtrChal, 0);
}

JNIEXPORT jbyteArray JNICALL Java_org_hsbp_androsphinx_Sphinx_respond(JNIEnv *env, jobject ignore, jbyteArray chal, jbyteArray secret) {
	jbyte* bufferPtrChal = (*env)->GetByteArrayElements(env, chal, NULL);
	jbyte* bufferPtrSecret = (*env)->GetByteArrayElements(env, secret, NULL);

	jbyteArray resp = (*env)->NewByteArray(env, SPHINX_255_SER_BYTES);
	jbyte* bufferPtrResp = (*env)->GetByteArrayElements(env, resp, NULL);

	int result = sphinx_respond(bufferPtrChal, bufferPtrSecret, bufferPtrResp);

	(*env)->ReleaseByteArrayElements(env, resp, bufferPtrResp, result ? JNI_ABORT : 0);
	(*env)->ReleaseByteArrayElements(env, chal, bufferPtrChal, JNI_ABORT);
	(*env)->ReleaseByteArrayElements(env, secret, bufferPtrSecret, JNI_ABORT);

	return result ? NULL : resp;
}

JNIEXPORT jbyteArray JNICALL Java_org_hsbp_androsphinx_Sphinx_finish(JNIEnv *env, jobject ignore, jbyteArray bfac, jbyteArray resp) {
	jbyte* bufferPtrBfac = (*env)->GetByteArrayElements(env, bfac, NULL);
	jbyte* bufferPtrResp = (*env)->GetByteArrayElements(env, resp, NULL);

	jbyteArray rwd = (*env)->NewByteArray(env, SPHINX_255_SER_BYTES);
	jbyte* bufferPtrRwd = (*env)->GetByteArrayElements(env, rwd, NULL);
	
	int result = sphinx_finish(bufferPtrBfac, bufferPtrResp, bufferPtrRwd);

	(*env)->ReleaseByteArrayElements(env, rwd, bufferPtrRwd, result ? JNI_ABORT : 0);
	(*env)->ReleaseByteArrayElements(env, resp, bufferPtrResp, JNI_ABORT);
	(*env)->ReleaseByteArrayElements(env, bfac, bufferPtrBfac, JNI_ABORT);

	return result ? NULL : rwd;
}

JNIEXPORT jbyteArray JNICALL Java_org_hsbp_androsphinx_Sphinx_randomBytes(JNIEnv *env, jobject ignore, jint bytes) {
	if (bytes < 1) return NULL;

	jbyteArray randomBytes = (*env)->NewByteArray(env, bytes);
	jbyte* bufferRandomBytes = (*env)->GetByteArrayElements(env, randomBytes, NULL);

	randombytes_buf(bufferRandomBytes, bytes);

	(*env)->ReleaseByteArrayElements(env, randomBytes, bufferRandomBytes, 0);

	return randomBytes;
}

JNIEXPORT jbyteArray JNICALL Java_org_hsbp_androsphinx_Sphinx_genericHash(JNIEnv *env, jobject ignore, jbyteArray data, jbyteArray salt, jint length) {
	if (length < 1) return NULL;

	jbyteArray hash = (*env)->NewByteArray(env, length);
	jbyte* bufferHash = (*env)->GetByteArrayElements(env, hash, NULL);
	jbyte* bufferData = (*env)->GetByteArrayElements(env, data, NULL);
	jbyte* bufferSalt = (*env)->GetByteArrayElements(env, salt, NULL);
	jsize dataLen = (*env)->GetArrayLength(env, data);
	jsize saltLen = (*env)->GetArrayLength(env, salt);

	crypto_generichash(bufferHash, length, bufferData, dataLen, bufferSalt, saltLen);

	(*env)->ReleaseByteArrayElements(env, hash, bufferHash, 0);
	(*env)->ReleaseByteArrayElements(env, data, bufferData, JNI_ABORT);
	(*env)->ReleaseByteArrayElements(env, salt, bufferSalt, JNI_ABORT);
	return hash;
}

JNIEXPORT void JNICALL Java_org_hsbp_androsphinx_Sphinx_keyPair(JNIEnv *env, jobject ignore, jbyteArray pk, jbyteArray sk) {
	jbyte* bufferPK = (*env)->GetByteArrayElements(env, pk, NULL);
	jbyte* bufferSK = (*env)->GetByteArrayElements(env, sk, NULL);

	crypto_sign_keypair(bufferPK, bufferSK);

	 (*env)->ReleaseByteArrayElements(env, pk, bufferPK, 0);
	 (*env)->ReleaseByteArrayElements(env, sk, bufferSK, 0);
}
