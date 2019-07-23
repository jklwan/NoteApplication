package com.chends.note.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author chends create on 2019/7/23.
 */
public abstract class BaseHolder<T> extends RecyclerView.ViewHolder {
    private Context context;

    public BaseHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
    }

    public abstract void onBind(T data);
}
