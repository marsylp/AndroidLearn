package com.mars.ndksimple_01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HelloNDK helloNDK = new HelloNDK();
        String hello = helloNDK.sayHello();
        Log.i("NdkSimple", "sayHello:" + hello);
        setTitle(hello);
    }
}
