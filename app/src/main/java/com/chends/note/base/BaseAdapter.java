package com.chends.note.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chends create on 2019/7/23.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> {
    protected Context context;
    private List<T> mList = new ArrayList<>();
    private BaseListener<T> itemClickLister;

    public BaseAdapter(Context context) {
        this.context = context;
    }

    public BaseAdapter(Context context, List<T> list) {
        this.context = context;
        if (list != null) {
            mList.addAll(list);
        }
    }

    public void setItemClickLister(BaseListener<T> itemClickLister) {
        this.itemClickLister = itemClickLister;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder<T> holder, int position) {
        if (itemClickLister != null) {
            holder.itemView.setOnClickListener(v -> {
                itemClickLister.onFinish(getItem(holder.getAdapterPosition()));
            });
        }
        holder.onBind(getItem(holder.getAdapterPosition()));
    }

    public View inflate(int layoutId, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(layoutId, parent, false);
    }

    @Nullable
    public T getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
