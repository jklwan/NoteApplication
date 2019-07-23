package com.chends.note.utils;


import android.os.Handler;
import android.os.Message;

import com.chends.note.base.BaseListener;

/**
 * @author chends create on 2019/7/23.
 */
public class CommonHandler extends Handler {
    private BaseListener<Message> listener;

    public CommonHandler(BaseListener<Message> listener) {
        this.listener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        if (listener != null) {
            listener.onFinish(msg);
        }
    }
}
