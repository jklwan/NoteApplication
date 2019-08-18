package com.chends.note.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chends.note.R;
import com.chends.note.base.BaseActivity;
import com.chends.note.base.BaseAdapter;
import com.chends.note.base.BaseHolder;
import com.chends.note.utils.DisplayUtil;
import com.chends.note.utils.ItemDecoration;
import com.chends.note.utils.ToastUtils;
import com.chends.note.widget.SideSlipRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cds created on 2019/8/18.
 */
public class RecyclerSideActivity extends BaseActivity {
    private SideSlipRecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new ItemDecoration
                .Builder(1)
                .setDecoration(DisplayUtil.dp2px(10))
                .build());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(i);
        }
        recyclerView.setAdapter(new Adapter(this, list));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_recycler_view_side;
    }


    private class Adapter extends BaseAdapter<Integer> {
        public Adapter(Context context, List<Integer> list) {
            super(context, list);
        }

        @NonNull
        @Override
        public BaseHolder<Integer> onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ItemHolder(inflate(R.layout.item_recycler_side, viewGroup));
        }

        /**
         * 删除
         */
        public void remove(int position) {
            if (position >= 0 && position < getItemCount()) {
                mList.remove(position);
                notifyItemRemoved(position);
            }
        }

        private class ItemHolder extends BaseHolder<Integer> {
            TextView title, delete, cancel;

            public ItemHolder(@NonNull View itemView) {
                super(itemView);
                this.title = itemView.findViewById(R.id.title);
                this.delete = itemView.findViewById(R.id.delete);
                this.cancel = itemView.findViewById(R.id.cancel);
            }

            @Override
            public void onBind(Integer data) {
                if (data != null) {
                    boolean has = data % 2 == 0;
                    cancel.setVisibility(has ? View.VISIBLE : View.GONE);
                    title.setText(String.format("item %1$d" + (has ? "有" : "无") + "取消选项", data));
                } else {
                    title.setText("无数据");
                    cancel.setVisibility(View.GONE);
                }
                delete.setOnClickListener(new Click());
                cancel.setOnClickListener(new Click());
            }

            private class Click implements View.OnClickListener {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.delete:
                            recyclerView.turnNormal();
                            ToastUtils.showShort("删除");
                            remove(getAdapterPosition());
                            break;
                        case R.id.cancel:
                            recyclerView.turnNormal();
                            ToastUtils.showShort("取消");
                            break;
                    }
                }
            }
        }

    }
}
