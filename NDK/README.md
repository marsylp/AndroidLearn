# NDK入门
## 1、创建so库
* Step-1. 创建一个*.java类，然后编译这个*.java生成*.class文件，使用javah -jni ***** (*.class 目录下的*.java类名) 生成*.h，例如：  
HelloNDK.java  
````
public class HelloNDK {
    static {
        System.loadLibrary("HelloNDK1");
    }
    public native String sayHello();
}
````
com_mars_ndksimple_HelloNDK.h

````
/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_mars_ndksimple_HelloNDK */

#ifndef _Included_com_mars_ndksimple_HelloNDK
#define _Included_com_mars_ndksimple_HelloNDK
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_mars_ndksimple_HelloNDK
 * Method:    sayHello
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_mars_ndksimple_HelloNDK_sayHello
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif
#endif
````  
* Step-2. 在app/src/main目录下创建jni文件，然后把第一步生成的*.h移动到该目录下，创建*.c、Android.mk、Application.mk文件，并根据*h来写.c中的代码。  
Android.mk  

````
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := HelloNDK1
LOCAL_SRC_FILES := com_mars_ndksimple_HelloNDK.c
include $(BUILD_SHARED_LIBRARY)
````
Application.mk

````
APP_ABI:=all
````
com_mars_ndksimple_HelloNDK.c

````
# include <jni.h>
# include "com_mars_ndksimple_HelloNDK.h"

JNIEXPORT jstring JNICALL
Java_com_mars_ndksimple_HelloNDK_sayHello(JNIEnv *env, jobject thiz)
{
    return (*env) -> NewStringUTF(env,"Welcome to NDK!");
}
````
Android.mk:[https://developer.android.google.cn/ndk/guides/android_mk]()
Application.mk:[https://developer.android.google.cn/ndk/guides/application_mk]()  
* Step-3. 在打开终端切换到jni目录下，使用命令：ndk-build生成so库即可

## 2、创建C/C++对象

* Step-1：创建JNIUtil.java类
在这个JNI接口中定义2个函数：newObject(),execute(long refer,int digit1,int digit2)。  
	* 其中newObject()函数会创建一个Adder对象，并将该对象指针传递回来给Java程序。  
	* 而execute()函数的refer参数，是用来让Java程序能将对象指针传进去给execute()函数，此时execute()就能由该指针而调用到之前newObject()函数所创建的对象。  
代码如下：

```
public class JNIUtil {
    static {
        System.loadLibrary("MyJni");
    }
    public static native long newObject();
    public static native long execute(long refer,int digit1,int digit2);
}
```
* Step-2: 创建Android.mk,Appliction.mk,*.c类,Adder.h

```
//Adder.h
#include <jni.h>
#ifdef __cplusplus
extern "C" {
#endif
typedef struct Adder Adder;
struct Adder{
    int (*exec)(int a, int b);
};

static int my_exec(int a, int b){
    return (a + b);
}

struct Adder *AdderNew(){
    // 构造式
    struct Adder *t = (Adder *)malloc(sizeof(Adder));
    t->exec = my_exec;
    return (void*)t;
}

#ifdef __cplusplus
}
#endif
```
```
//com_mars_ndksimple_JNIUtil.h
/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_mars_ndksimple_JNIUtil */

#ifndef _Included_com_mars_ndksimple_JNIUtil
#define _Included_com_mars_ndksimple_JNIUtil
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     com_mars_ndksimple_JNIUtil
 * Method:    newObject
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_com_mars_ndksimple_JNIUtil_newObject
  (JNIEnv *, jclass);

/*
 * Class:     com_mars_ndksimple_JNIUtil
 * Method:    execute
 * Signature: (JII)J
 */
JNIEXPORT jlong JNICALL Java_com_mars_ndksimple_JNIUtil_execute
  (JNIEnv *, jclass, jlong, jint, jint);

#ifdef __cplusplus
}
#endif
#endif
```

```
//Test.c
# include <jni.h>
# include "Adder.h"
# include "com_mars_ndksimple_JNIUtil.h"

JNIEXPORT jlong JNICALL
Java_com_mars_ndksimple_JNIUtil_newObject(JNIEnv *env,jclass c){
	//创建一个C对象
    Adder* ar = (Adder*)AdderNew();
    return (jlong)ar;
}

JNIEXPORT jlong JNICALL
Java_com_mars_ndksimple_JNIUtil_execute( JNIEnv *env, jclass c, jlong refer, jint digit_1, jint digit_2){
	//转成C对象的指针
    Adder* pa = (Adder*)refer;
    long result = pa->exec(digit_1, digit_2);
    return result;
}
```
* Step-3: 生成so库  

## 3、从C调用Java函数
* Step-1：在Java层创建CounterNative.java类,在其中创建4个本地方法：静态的nativeExecute()和一般的nativeSteup()、nativeExec()、nativeGetValue()；以及一个静态setValue(int value)和一个一般的setV(int value)函数。  
	* nativeExecute():调用Java层的setValue()方法，而nativeExec()和nativeGetValue()则会调用setV()方法。  
	* 代码如下

```
public class CounterNative {
    public static Handler mHandler;
    private int number;

    static {
        System.loadLibrary("MyCounter");
    }

    @SuppressLint("HandlerLeak")
    public CounterNative() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                MainActivity.ref.setTitle(msg.obj.toString());
            }
        };
        number = 5;
        nativeSetup();
    }

    public static void setValue(int value) {
        String str = "Value(static)" + String.valueOf(value);
        Message message = mHandler.obtainMessage(1, 1, 1, str);
        mHandler.sendMessage(message);
    }

    private void setV(int value) {
        String str = "Value = " + String.valueOf(value);
        Message message = mHandler.obtainMessage(1, 1, 1, str);
        mHandler.sendMessage(message);
    }

    private native void nativeSetup();

    public static native void nativeExecute(int n);

    public native void nativeExec(int n);

    public native void nativeGetValue();
}
```
* Step-2：在jni中创建Android.mk,Appliction.mk以及相应的 *.c文件，并将*.h文件移动到jni目录下。
*.c会实现CounterNative.java类中的本地方法，并在nativeSteup()函数中负责将m_calss、m_object、m_static_mid、m_mid、m_fid存储在C模块的静态区域。  
	* 当调用nativeExecute()时，处理完本地逻辑后根据m_calss、m_static_mid调用(*env)->CallStaticVoidMethod(env,m_class,m_mid_static,sum)方法回调CounterNative.java类的setValue()方法。
	* 当调用nativeGetValue，首先根据m_object, m_fid调用(int)(*env)->GetIntField(env, m_object, m_fid)方法，去取CounterNative.java类的全局变量number，然后计算出结果，在根据m_object,m_mid 调用(*env)->CallVoidMethod(env,m_object,m_mid,sum)回调setV()（nativeExec执行方法一样）;
	
代码如下：

```
# include <jni.h>
# include "com_mars_ndksimple_CounterNative.h"
jclass m_class;
jobject m_object;
jmethodID m_mid_static,m_mid;
jfieldID m_fid;

JNIEXPORT void JNICALL
Java_com_mars_ndksimple_CounterNative_nativeSetup(JNIEnv *env, jobject thiz)
{
	
	 //创建thiz所参考对象的类(即CounterNative类)
    jclass clazz = (*env)->GetObjectClass(env,thiz);
    //将clazz保存为全局变量
    m_class = (jclass)(*env)->NewGlobalRef(env,clazz);
    //将thiz保存为全局变量
    m_object = (jobject)(*env)->NewGlobalRef(env,thiz);
    //获取CounterNative类的setValue()方法的ID并将其保存在m_mid_static中
    m_mid_static = (*env)->GetStaticMethodID(env,m_class,"setValue","(I)V");
     //获取CounterNative类的setV()方法的ID并将其保存在m_mid中
    m_mid = (*env)->GetMethodID(env,m_class,"setV","(I)V");
    //获取CounterNative类的number变量的ID并将其保存在m_fid中
    m_fid = (*env)->GetFieldID(env,clazz,"number","I");
    return;
}

JNIEXPORT void JNICALL
Java_com_mars_ndksimple_CounterNative_nativeExecute(JNIEnv *env, jclass clazz, jint n)
{
    int i, sum = 0;
    for(i = 0; i <= n; i++) sum+=i;
    (*env)->CallStaticVoidMethod(env,m_class,m_mid_static,sum);
    return;
}

JNIEXPORT void JNICALL
Java_com_mars_ndksimple_CounterNative_nativeExec(JNIEnv *env, jobject thiz, jint n)
{
    int i, sum = 0;
    for(i = 0; i <= n; i++) sum+=i;
    (*env)->CallVoidMethod(env,m_object,m_mid,sum);
    return;
}

JNIEXPORT void JNICALL
Java_com_mars_ndksimple_CounterNative_nativeGetValue(JNIEnv *env, jobject thiz)
{
    int i, n, sum = 0;
    n = (int)(*env)->GetIntField(env, m_object, m_fid);
    for(i=0; i<=n; i++) sum+=i;
    (*env)->CallVoidMethod(env,m_object,m_mid,sum);
    return;
}

```
根据.c可得出：  
1、拿目前对象指针换取它的类ID：
```
	jclass clazz = (*env)->GetObjectClass(env,thiz);
```
2、拿目前类ID换取某方法的ID：
```
	m_mid = (*env)->GetMethodID(env,m_class,"setV","(I)V");
```
3、依据类ID和函数ID，调用指定类里的指定函数：
```
	(*env)->CallVoidMethod(env,m_object,m_mid,sum);
```
* Step-3:生成so库
## 4、从C创建Java对象
* Step-1: 在Java创建一个抽象类ANativeSimple，一个ActNative类，一个ResultValue类，以及一个ANativeSimple的子类NativieSimpleSub。其类图如下:  
![](https://github.com/marsylp/AndroidLearn/blob/master/NDK/NDKSimple01/image/UML-01.png)  
*  Step-2: 生成*.h文件，并创建Android.mk,Appliction.mk以及相应的 *.c文件，在*.c文件中最关键Java_com_mars_ndksimple_ANativeSimple_nativeSetup方法操作如下：  
	1、根据特定的类，得到clazz.
	```
	jclass rvClazz = (*env)->FindClass(env, "com/mars/ndksimple/ResultValue");
	```
	2、根据这个类中的<init>()构造式，得到methodID.
	```
	jmethodID constr = (*env)->GetMethodID(env, rvClazz, "<init>", "()V");
	```
	3、基于methodID,调用构造式(创建对象)。
	```
	    jobject ref = (*env)->NewObject(env, rvClazz, constr);
	 ```
	 其*.c代码如下。
	 
```
# include <jni.h>
# include "com_mars_ndksimple_ANativeSimple.h"
# include "com_mars_ndksimple_ActNative.h"
jobject m_object, m_rv_object ;
jfieldID m_fid;
jmethodID m_rv_mid;

JNIEXPORT void JNICALL
Java_com_mars_ndksimple_ANativeSimple_nativeSetup(JNIEnv *env, jobject thiz)
{
    jclass clazz = (*env)->GetObjectClass(env, thiz);
    m_object = (jobject)(*env)->NewGlobalRef(env, thiz);
    m_fid = (*env)->GetFieldID(env, clazz, "numb", "I");
    jclass rvClazz = (*env)->FindClass(env, "com/mars/ndksimple/ResultValue");
    jmethodID constr = (*env)->GetMethodID(env, rvClazz, "<init>", "()V");
    jobject ref = (*env)->NewObject(env, rvClazz, constr);
    m_rv_object = (jobject)(*env)->NewGlobalRef(env, ref);
    m_rv_mid = (*env)->GetMethodID(env, rvClazz, "setV", "(I)V");
    return;
}

JNIEXPORT jobject JNICALL
Java_com_mars_ndksimple_ActNative_nativeExec(JNIEnv *env, jclass clazz)
{
    int n, i, sum = 0;
    n = (int)(*env)->GetIntField(env, m_object, m_fid);
    for(i=0; i<=n; i++) sum+=i;
    (*env)->CallVoidMethod(env, m_rv_object, m_rv_mid, sum);
    return m_rv_object;
}
```
*  Step-3: 生成*.so库。
*  Step-4: 在Activity中创建NativieSimpleSub对象，然后调用ActNative类的本地方法nativeExec()生成ResultValue类的对象即可调用其getValue()方法获取值。

## 4、多个Java线程进入本地函数
每一个线程第一次进入VM调用本地函数时，VM会替它诞生一个相对映的JNIEnv对象，此对象可以存储该线程相关的数据值。如此可以避免线程因共享对象或数据而引发的线程冲突问题，也就有效提升了JNI环境下的多线程安全性。  
Java程序可能会有多个线程同时先后进入同一个本地函数里执行。有些平台充许你将私有的数据存储于JNIEnv的对象里，避免共享问题，但有些平台则否。所以你的私有数据不能或不想将它存于JNIEnv对象里，而是放在一般的变量里，就必须自己注意变量共享而产生的线程安全问题了。然而JNI提供了保证线程安全的机制。
在进入需要保护的程序时，调用:
```env->MonitorEnter(syncObj);```
这样当其他线程进来时就只能停下等待。
在执行完需要保护的程序时调用：
```env->MonitorExit(syncObj);```

## 5、本地线程进入Java层
主要代码示例：

```
void JNICALL Java_com_misoo_counter_CounterNative_nativeExec (JNIEnv *env, jobject thiz, jint numb){
n = numb;
//创建线程
pthread_create( &thread, NULL, trRun, NULL);
}
void* trRun( void* ){
int status;
JNIEnv *env; bool isAttached = false;
status = jvm->GetEnv((void **) &env, JNI_VERSION_1_4); if(status < 0) {
//向VM登记，要求VM创建一个JNIEnv对象，并将其指针值存入env里
status = jvm->AttachCurrentThread(&env, NULL); if(status < 0) return NULL;
isAttached = true;
}
sum = 0;
for(int i = 0; i<=n; i++) sum += i;
env->CallStaticVoidMethod(mClass, mid, sum);
if(isAttached) jvm->DetachCurrentThread(); 
return NULL;
}
```
