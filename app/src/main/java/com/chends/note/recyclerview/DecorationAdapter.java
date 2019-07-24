package com.chends.note.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chends.note.base.BaseAdapter;
import com.chends.note.base.BaseHolder;
import com.chends.note.utils.CommonHandler;
import com.chends.note.utils.DisplayUtil;
import com.chends.note.utils.ItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author chends create on 2019/7/24.
 */
public class DecorationAdapter extends BaseAdapter<Integer> {
    private final int Top = 1;
    private CommonHandler handler;

    public DecorationAdapter(Context context, List<Integer> list) {
        super(context, list);
        handler = new CommonHandler(msg -> {
            if (msg != null && msg.obj instanceof TextView) {
                ((TextView) msg.obj).setText(String.format(Locale.getDefault(),
                        "width:%1$d height:%2$d", msg.arg1, msg.arg2));
            }
        });
    }

    @NonNull
    @Override
    public BaseHolder<Integer> onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        if (type == Top) {
            RecyclerView recyclerView = new RecyclerView(context);
            recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    DisplayUtil.dp2px(300)));
            recyclerView.setBackgroundColor(Color.GREEN);
            return new TopHolder(recyclerView);
        }
        TextView view = new TextView(context);
        view.setGravity(Gravity.CENTER);
        view.setBackgroundColor(Color.WHITE);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DisplayUtil.dp2px(100)));
        return new ItemHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return Top;
        }
        return super.getItemViewType(position);
    }

    private class TopHolder extends BaseHolder<Integer> {
        private RecyclerView recyclerView;
        private BaseAdapter<Integer> adapter;

        public TopHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView;
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3,
                    LinearLayoutManager.HORIZONTAL, false));
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                list.add(i);
            }
            adapter = new BaseAdapter<Integer>(context, list) {
                @NonNull
                @Override
                public BaseHolder<Integer> onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
                    TextView view = new TextView(context);
                    view.setGravity(Gravity.CENTER);
                    view.setBackgroundColor(Color.WHITE);
                    view.setLayoutParams(new ViewGroup.LayoutParams(DisplayUtil.dp2px(80),
                            ViewGroup.LayoutParams.MATCH_PARENT));
                    return new ItemHolder(view);
                }
            };

            recyclerView.addItemDecoration(new ItemDecoration
                    .Builder(3)
                    .setIncludeTB(true)
                    .setIncludeLR(true)
                    .setVertical(false)
                    .setDecoration(DisplayUtil.dp2px(10))
                    .build());
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onBind(Integer data) {
            if (recyclerView != null && recyclerView.getAdapter() == null) {
                recyclerView.setAdapter(adapter);
            }
        }
    }

    private class ItemHolder extends BaseHolder<Integer> {
        TextView view;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            this.view = (TextView) itemView;
        }

        @Override
        public void onBind(Integer data) {
            if (data != null) {
                view.setTag(data);
                view.post(() -> {
                    if (view.getTag() instanceof Integer && data.equals(Integer.valueOf(view.getTag().toString()))) {
                        Message msg = Message.obtain();
                        msg.obj = view;
                        msg.arg1 = view.getMeasuredWidth();
                        msg.arg2 = view.getMeasuredHeight();
                        handler.sendMessage(msg);
                    }
                });
            }
        }
    }
}
