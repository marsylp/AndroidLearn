# include <jni.h>
# include "com_mars_ndksimple_01_HelloNDK.h"

JNIEXPORT jstring JNICALL
Java_com_mars_ndksimple_101_HelloNDK_sayHello(JNIEnv *env, jobject thiz)
{
    return (*env) -> NewStringUTF(env,"Welcome to NDK!");
}

