# NDK入门

在介绍NDK之间，我们先简单了解下JNI（Java Native Interface）它定义了Android从托管代码(Java/Kotlin)编译的字节码的方式，以本机代码(C/C++)进行交互。JNI支持从动态共享库加载代码。关于JNI介绍可以查阅：[https://developer.android.google.cn/training/articles/perf-jni]()

现在我们简单的来了解NDK，NDK（原生开发工具包）是一组可让你在Android应用中利用C和C++代码的工具。可用以从你的源代码构建，或者利用现有的预构建库。

NDK不适用于大多数初学的Android开发者，并且对多类型的Android应用没有什么价值。因为它不可避免地会增加开发过程的复杂性，所以通常不值得使用。但如果你需要执行以下操作，它可能很有用：

* 从设备获取卓越性能以用于计算密集型应用，例如游戏或者物理模拟。
* 重复使用你自己或者其他开发者的C或C++库。

通过上面的简单了解，我们对NDK有了简单的认识，接下来我们通过AndroidStudio来实战了解DNK的简单实用。关于NDK详细请查询：[https://developer.android.google.cn/ndk/guides/]()

首先请在AndroidStudio上安装好NDK环境，并配置好系统的环境变量，就可以动手开发了。

#### Step-1. 在AndroidStudio中创建一个开发项目
* 例如，建立一个名为NDKSimple的项目，然后新建一个HelloNDK.java类，定义如下：

```java
public class HelloNDK {
    static {
        System.loadLibrary("HelloNDK-01");
    }
    public native String sayHello();
}
```
#### Step-2. 编译HelloNDK.java得到HelloNDK.class
![](https://github.com/marsylp/AndroidLearn/blob/master/NDK/NDKSimple01/image/HelloNDK-01.png)
#### Step-3. 在HelloNDK.class根目录使用命令生产*.h头文件
打开终端，cd切换到HelloNDK.class这个文件所在的目录，输入如下命令:

```
这里以我的文件目录为例
cd /Users/obike/MarsDemo/NDKSimple/app/build/intermediates/javac/debug/compileDebugJavaWithJavac/classes
javah -jni com.mars.ndksimple.HelloNDK
```
![](https://github.com/marsylp/AndroidLearn/blob/master/NDK/NDKSimple01/image/HelloNDK-02.png)

最后得到一个***.h的文件,内容如下：

```
/* com_mars_ndksimple_HelloNDK.h */
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

```
然后在app/src/main文件夹下新建一个jni文件夹,并把刚生成的.h文件移动到jni文件中。
####Step-4. 创建.c、Android.mk、Application.mk文件
根据*.h头文件内容和JNI语法撰写*.c，如下：

```
# include <jni.h>
# include "com_mars_ndksimple_HelloNDK.h"

JNIEXPORT jstring JNICALL
Java_com_mars_ndksimple_HelloNDK_sayHello(JNIEnv *env, jobject thiz)
{
    return (*env) -> NewStringUTF(env,"Welcome to NDK!");
}

```
Android.mk

```
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := HelloNDK-01
LOCAL_SRC_FILES := com_mars_ndksimple_HelloNDK.c
include $(BUILD_SHARED_LIBRARY)
```
Application.mk

```
APP_ABI:=all
```
####Step-5. 生成.so库
回到终端，cd切换到jni文件下输入如下命令:ndk-build生成在Application.mk中配置各ABI架构下的so库，so库文件名就是在Android.mk中LOCAL_MODULE所指定的。如下图:

![](https://github.com/marsylp/AndroidLearn/blob/master/NDK/NDKSimple01/image/HelloNDK-03.png)

最后在app的build文件中加入如下代码，大功告成。
 
```
android {
    ...
    sourceSets {
        main() {
            jniLibs.srcDirs = ['src/main/libs']
            jni.srcDirs = [] //屏蔽掉默认的jni编译生成过程
        }
    }
}
```
####Step-6. 在Activity中调用
````
HelloNDK helloNDK = new HelloNDK();
String hello = helloNDK.sayHello();
Log.i("NdkSimple", "sayHello:" + hello);
````


