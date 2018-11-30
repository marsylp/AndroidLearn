package com.mars.ndksimple_01;

public class HelloNDK {
    static {
        System.loadLibrary("HelloNDK1");
    }
    public native String sayHello();
}
