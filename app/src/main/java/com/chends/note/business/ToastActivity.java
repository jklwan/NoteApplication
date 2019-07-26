package com.chends.note.business;

import android.view.View;

import com.chends.note.R;
import com.chends.note.base.BaseActivity;
import com.chends.note.utils.ToastUtils;

/**
 * @author chends create on 2019/7/26.
 */
public class ToastActivity extends BaseActivity {
    private int i = 0;

    public void xmlClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                ToastUtils.showShort("toast 提示" + (i++));
                break;
            case R.id.threadButton:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        ToastUtils.showShort("子线程 toast 提示" + (i++));
                    }
                }.start();
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_toast;
    }
}