package com.chends.note;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chends.note.base.BaseActivity;
import com.chends.note.main.MainActivity;
import com.chends.note.utils.CommonHandler;
import com.chends.note.utils.IntentUtil;

/**
 * 启动页
 * @author chends create on 2019/7/23.
 */
public class SplashActivity extends BaseActivity {
    private CommonHandler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new CommonHandler(message -> {
            if (isAlive()) {
                IntentUtil.startActivity(this, MainActivity.class);
            }
        });
        handler.sendEmptyMessageDelayed(0, 3000);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @Override
    protected boolean isFullScreen() {
        return true;
    }

}