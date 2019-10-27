package com.chends.note.base;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.viewbinding.ViewBinding;

import com.chends.note.utils.ContextUtil;
import com.chends.note.utils.LanguageUtil;
import com.chends.note.utils.LogUtils;

/**
 * base
 * @author chends create on 2019/7/23.
 */
public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    private int mUiVisibility = 0;
    protected T viewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (isFullScreen()) {
            fullScreen();
        }
        super.onCreate(savedInstanceState);
        if (getLayoutRes() > 0) {
            setContentView(getLayoutRes());
        } else {
            initViewBinding();
            setContentView(viewBinding.getRoot());
        }
        ContextUtil.setOverScroll(this);
    }

    protected int getLayoutRes() {
        return 0;
    }

    protected void initViewBinding(){

    }

    private int getUiVisibility() {
        if (mUiVisibility == 0) {
            mUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_FULLSCREEN;


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mUiVisibility = mUiVisibility | View.SYSTEM_UI_FLAG_IMMERSIVE;
            }
        }
        return mUiVisibility;
    }

    /**
     * 设置全屏显示，隐藏导航栏
     */
    private void fullScreen() {
        final View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(getUiVisibility());
        decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                decorView.setSystemUiVisibility(getUiVisibility());
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFullScreen() && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(getUiVisibility());
        }
    }

    protected boolean isFullScreen() {
        return false;
    }

    public boolean isAlive() {
        return ContextUtil.isAlive(this);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    @Override
    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }

    @SuppressWarnings("RestrictedApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        if (startActivitySelfCheck(intent)) {
            // 查看源码得知 startActivity 最终也会调用 startActivityForResult
            super.startActivityForResult(intent, requestCode, options);
        }
    }

    private String mActivityJumpTag;
    private long mActivityJumpTime;

    /**
     * 检查当前 Activity 是否重复跳转了，不需要检查则重写此方法并返回 true 即可
     * @param intent 用于跳转的 Intent 对象
     * @return 检查通过返回true, 检查不通过返回false
     */
    protected boolean startActivitySelfCheck(Intent intent) {
        // 默认检查通过
        boolean result = true;
        // 标记对象
        String tag;
        if (intent.getComponent() != null) { // 显式跳转
            tag = intent.getComponent().getClassName();
        } else if (intent.getAction() != null) { // 隐式跳转
            tag = intent.getAction();
        } else {
            return true;
        }

        if (tag.equals(mActivityJumpTag) && mActivityJumpTime >= SystemClock.uptimeMillis() - 500) {
            // 检查不通过
            result = false;
        }
        // 记录启动标记和时间
        mActivityJumpTag = tag;
        mActivityJumpTime = SystemClock.uptimeMillis();
        LogUtils.d("startActivitySelfCheck " + result + ", tag:" + mActivityJumpTag + ",time:" + mActivityJumpTime);
        return result;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase));
    }
}