CFLAGS=-I/usr/lib/jvm/java-8-openjdk-amd64/include -I/usr/lib/jvm/java-8-openjdk-amd64/include/linux -shared -fPIC  -L. -l:libsphinx.so -lsodium

libsphinxjni.so: libsphinxjni.c libsphinxjni
	mv libsphinxjni libsphinxjni.so

libsphinxjni: libsphinxjni.c
