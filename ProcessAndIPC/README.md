## 认识进程、线程与IPC架构

1.什么是IPC通信  
IPC通信是跨越两个不同进程之间的通信。一般而言，一个Android应用程序里的各组件（如Activity、Service）都在同一个进程里执行。这种在同一个进程内的通信，又称为短程通信，意味着，两个Activity在同一个进程里执行。相对地，远程通信的意思是：两个组件分别在不同的进程里执行；两者之间是IPC通信，又称远程通信。  
* IPC通信的效率  
	当我们启动某一个应用程序时，Android系统里的Zzygote服务分配一个新的进程给它，然后将它加载到这个新诞生的进程里。基于Linux的安全机制，以及进程的基本特性（例如，不同进程的地址空间是独立的）如果两个类在同一个进程里执行，两者沟通方便也快速。但是，当它们分别在不同的进程里执行时，两者沟通就属于IPC进程沟通了，不如前者高效。

2.Android的进程概念  
一个进程是一个独立的执行空间，不会被正在其它进程里的程序所侵犯。在Android的进程里，有一个虚拟机的对象，可执行Java代码，也引导JNI本地程序的执行，实现Java与C/C++之间的沟通。  
每一个进程在诞生时，都会诞生一个主线程（Main Thread），以及诞生一个Looper类的对象和一个MQ（Message Queue）数据结构。每当主线程执行完实践，就会去执行Looper类。此时，不断的观察MQ的动态。  
主线程最主要的工作就是处理UI画面的事件（Event），每当UI事件发生时，Android框架会丢信息（Message）到MQ里。主线程看到MQ有新的信息时，就取出信息，然后依据信息内容而去执行特定的函数。执行完毕，就再继续执行Looper类，不断的观察MQ的动态（进程是系统进行资源分配的独立单元）。

3.设定IPC通信--使用AndroidManifest.xml文件  
在Android框架里，一个应用程序（Application Package）通常包含有多个Java类，这些类可以在同一个进程里执行，可以在不同的进程里执行。通常，一个进程只会有一个APP，但是一个APP可以占用多个进程。
例如在App的AndroidManifest.xml文件内容中的<activity>或<service>根属性中添加android:process=“:xxxx”即可。 
 
4.IPC的IBinder接口--定义与实现  
Android架构的IPC沟通依赖单一的IBinder接口。此时Client端调用IBinder接口的transact()函数，透过IPC机制而调用到远方的onTransact()函数。在Android的源代码里，Java层的IBinder接口是定义于IBinder.java代码文档里。  
IBinder接口定义了一些函数，可以让Client程序可以进行跨进程的调用，其中，最主要的一个函数就是transact()函数。在Android的框架里，也撰写了Binder基类和BinderProxy类来实现IBinder接口。  
Binder基类的很重要的目的就是支撑跨进程调用Service，也就是让远程的Client可以跨进程调用某个Service。其主要函数是：  
·transact()   
    — 用来实现IBinder的transact()函数接口。  
·execTransact()   
    — 与transact()函数是相同的，只是这是用来让C/C++本地程序来调用的。  
·onTransact()   
    — 这是一个抽象函数，让子类来覆写的，上述的transact()和execTransact()都是调用onTransact()函数来实现反向调用的。  
·init()  
    — 这是一个本地函数，让JNI模块来实现这个函数。Binder()构造函数会调用这个init()本地函数。  
UML图形表示：  
 ![](https://github.com/marsylp/AndroidLearn/blob/master/ProcessAndIPC/imags/IBinder1.png)  
BinderProxy基类  
这个类也是定义在Binder.java里面的。由于跨进程通信时，并不是从Java层直接沟通，而是通过底层的BinderDriver驱动来沟通，所以Client端的Java类别，必须通过BinderProxy分身的IBinder接口，转而调用JNI本地模块来链接到底层BInderDriver驱动服务，进而调用到正在另一个进程里执行的Service。当Client端通过IBinder接口而调用到BinderProxy的transact()函数，就调用到其JNI本地模块的transact()函数，就能连接到底层BinderDriver驱动服务里。
  
5.使用IBinder接口  
![](https://github.com/marsylp/AndroidLearn/blob/master/ProcessAndIPC/imags/IBinder2.png)  

6.IPC通信的步骤  
·调用bindService()  
·调用IBinder接口的transact()  
Activity调用IBinder接口的transact()函数，通过底层BinderDriver驱动而间接调用到Binder基类的execTransact()函数，转而调用继承Binder类的onTransact()函数。 
 
7.短程通信VS.远程通信  

8.AIDL  
AIDL（Android接口定义语言），它定义了客户端与服务使用进程间通信(IPC)时都认可的编程接口。在Android上，一个进程通常无法访问另一个进程的内存，所Android提供了AIDL来处理这个繁琐的工作。  
注：只有充许不同应用的客户端使用IPC方式访问服务，并且想要在服务中处理多线程时，才有必要使用AIDL。如果你不需要执行跨越不同应用的并发IPC，就应该通过实现一个Binder创建接口；或者你想执行IPC，但根本不需要处理多线程，则使用Messager来实现接口。无论如何，在实现AIDL之前，请务必理解绑定服务。

9.线程
线程是CPU调度的基本单元。
线程之间的通信:  

*   a.主线程丢信息给自己：  
	 
	 ```
public class ac01 extends Activity implements OnClickListener { private Handler h;
public void onCreate(Bundle icicle) {
//........
h = new Handler(){
public void handleMessage(Message msg){ setTitle((String)msg.obj);
}}; 
}
public void onClick(View v) {
switch (v.getId()) { 
case 101:
	h.removeMessages(0);
	Message m = h.obtainMessage(1, 1, 1, "this is my 	message.");
	h.sendMessage(m);
	// 将Message送入MQ里
	break;
case 102:
	finish();
	break; 
}
}
}
	 ```
*   b.子线程丢信息给主线程： 

    ```
public class ac01 extends Activity implements OnClickListener { 
private Handler h;
private Timer timer = new Timer();
private int k=0;
public void onCreate(Bundle icicle) { super.onCreate(icicle);
//.........
h = new Handler(){
public void handleMessage(Message msg) { setTitle((String)msg.obj);
}}; 
}
public void onClick(View v) {
switch (v.getId()) {
case 101:
TimerTask task = new TimerTask(){
@Override 
public void run() {
h.removeMessages(0);
Message m = h.obtainMessage(1, 1, 1,
}};
Thread.currentThread().getName() + " : "+String.valueOf(k++));
h.sendMessage(m);
timer.schedule(task, 500, 1500);
case 102:
break;
finish();
break;
}
}
}
    ```
*   c.主线程与子线程通信  
	c-Step-1:主线程执行onCreate()函数时，创建一个子线程；
	c-Step-2:在子线程丢run函数中创建Looper和Hander：
	```Looper.prepare(); mHandler = new Handler{//...} Looper.loop(); ```
	c-Step-3:主线程中就可以使用在子线程中创建的Handler对象来发送消息。

	```
public class ac01 extends Activity implements OnClickListener {
private Thread t;
private Handler h;
private String str;
public void onCreate(Bundle icicle) {
//........
t = new Thread(new Task()); t.start(); 
}
public void onClick(View v) { 
switch(v.getId()){
case 101:
Message m = h.obtainMessage(1, 33, 1, null);
h.sendMessage(m); 
break;
case 102: 
setTitle(str); 
break;
case 103: 
h.getLooper().quit(); 
finish(); 
break;
}}
	```

## Binder的使用
1.一般用法  
![](https://github.com/marsylp/AndroidLearn/blob/master/ProcessAndIPC/MySimple/imags/BinderDifficulty_01.png)  
2.Proxy_Stub用法  
![](https://github.com/marsylp/AndroidLearn/blob/master/ProcessAndIPC/MySimple/imags/BinderDifficulty_02.png)  
3.AIDL用法  
![](https://github.com/marsylp/AndroidLearn/blob/master/ProcessAndIPC/MySimple/imags/BinderDifficulty_03.png)  
4.Messenger  
![](https://github.com/marsylp/AndroidLearn/blob/master/ProcessAndIPC/MySimple/imags/messenger_1.png)

