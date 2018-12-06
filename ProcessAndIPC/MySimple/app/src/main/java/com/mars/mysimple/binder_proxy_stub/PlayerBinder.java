package com.mars.mysimple.binder_proxy_stub;

import android.content.Context;
import android.media.MediaPlayer;

import com.mars.mysimple.R;

public class PlayerBinder extends BaseBinderStub {
    private String mState;
    private MediaPlayer mPlayer;
    private Context mContext;

    public PlayerBinder(Context context) {
        mContext = context;
    }

    @Override
    public void play() {
        mState = "点击了播放按钮";
        if (mPlayer == null){
            mPlayer = MediaPlayer.create(mContext, R.raw.text);
            mPlayer.start();
        }
    }

    @Override
    public void stop() {
        if (mPlayer != null){
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        mState = "点击了停止按钮";
    }

    @Override
    public String getState() {
        return mState;
    }
}
