CFLAGS=-I/usr/lib/jvm/java-8-openjdk-amd64/include -I/usr/lib/jvm/java-8-openjdk-amd64/include/linux -shared -fPIC  -L. -l:libsphinx.so

libtest.so: libtest.c libtest
	mv libtest libtest.so

libtest: libtest.c
