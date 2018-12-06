package com.mars.mysimple.binder_aidl;

import android.content.Context;
import android.media.MediaPlayer;

import com.mars.mysimple.IMyPlayer;
import com.mars.mysimple.R;

public class MyBinder extends IMyPlayer.Stub {
    private Context mContext;
    private MediaPlayer mPlayer;
    private String mState;

    public MyBinder(Context context) {
        mContext = context;
    }

    @Override
    public void play() {
        mState = "正在播放音乐";
        if (mPlayer == null) {
            mPlayer = MediaPlayer.create(mContext, R.raw.text);
        }
        mPlayer.start();
    }

    @Override
    public void stop() {
        mState = "停止播放";
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public String getState() {
        return mState;
    }
}
