package com.chends.note.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.chends.note.widget.CustomViewPager;
import com.chends.note.widget.SideSlipRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cds created on 2019/8/18.
 */
public class RecyclerSideInViewPagerActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new ViewPagerAdapter());
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_recycler_view_side_viewpager;
    }

    private class ViewPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return 2;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view  = getLayoutInflater().inflate(R.layout.activity_recycler_view_side, container,false);;
            SideSlipRecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.addItemDecoration(new ItemDecoration
                    .Builder(1)
                    .setDecoration(DisplayUtil.dp2px(10))
                    .build());
            recyclerView.setLayoutManager(new LinearLayoutManager(RecyclerSideInViewPagerActivity.this));
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                list.add(i);
            }
            recyclerView.setAdapter(new Adapter(RecyclerSideInViewPagerActivity.this, list));
            container.addView(recyclerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
    }

    private class Adapter extends BaseAdapter<Integer> {
        private SideSlipRecyclerView recyclerView;
        public Adapter(Context context, List<Integer> list) {
            super(context, list);
        }

        @NonNull
        @Override
        public BaseHolder<Integer> onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ItemHolder(inflate(R.layout.item_recycler_side, viewGroup));
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            this.recyclerView = (SideSlipRecyclerView)recyclerView;
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
