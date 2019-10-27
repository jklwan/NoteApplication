package com.chends.note.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chends.note.R;
import com.chends.note.base.BaseActivity;
import com.chends.note.base.Constant;
import com.chends.note.business.LanguageActivity;
import com.chends.note.business.ToastActivity;
import com.chends.note.business.ViewBindingActivity;
import com.chends.note.recyclerview.RecyclerDecorationActivity;
import com.chends.note.recyclerview.RecyclerSideActivity;
import com.chends.note.recyclerview.RecyclerSideInViewPagerActivity;
import com.chends.note.utils.DisplayUtil;
import com.chends.note.utils.IntentUtil;
import com.chends.note.utils.ItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 * @author cds created on 2019/7/22.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.mainTitle);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        MainAdapter adapter = new MainAdapter(this, getData());
        adapter.setItemClickLister(bean -> {
            if (bean != null) {
                IntentUtil.startActivity(this, bean.target);
            }
        });
        recyclerView.addItemDecoration(new ItemDecoration.Builder(1)
                .setDecoration(DisplayUtil.dp2px(10))
                .build());
        recyclerView.setAdapter(adapter);
    }

    private List<MainBean> getData() {
        List<MainBean> list = new ArrayList<>();
        list.add(new MainBean("RecyclerView分割线", RecyclerDecorationActivity.class));
        list.add(new MainBean("Toast提示", ToastActivity.class));
        list.add(new MainBean("多语言", LanguageActivity.class));
        list.add(new MainBean("RecyclerView侧滑", RecyclerSideActivity.class));
        list.add(new MainBean("RecyclerView侧滑（在ViewPager中）", RecyclerSideInViewPagerActivity.class));
        list.add(new MainBean("ViewBinding", ViewBindingActivity.class));
        return list;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (getIntent().getBooleanExtra(Constant.BOOLEAN_PARAM, false)) {
            recreate();
        }
    }
}