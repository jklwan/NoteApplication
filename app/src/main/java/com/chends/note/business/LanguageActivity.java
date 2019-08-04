package com.chends.note.business;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chends.note.AppPageDispatch;
import com.chends.note.R;
import com.chends.note.adapter.LanguageAdapter;
import com.chends.note.base.BaseActivity;
import com.chends.note.model.LanguageBean;
import com.chends.note.utils.DisplayUtil;
import com.chends.note.utils.ItemDecoration;
import com.chends.note.utils.LanguageUtil;

/**
 * @author cds created on 2019/8/3.
 */
public class LanguageActivity extends BaseActivity {

    private LanguageAdapter adapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_language;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.language);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new LanguageAdapter(this, LanguageUtil.getLanguage(), LanguageUtil.getLanguageSelect());
        adapter.setItemClickLister(data -> {
            if (data != null){
                adapter.switchSelect(data);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new ItemDecoration
                .Builder(1)
                .setDecoration(DisplayUtil.dp2px(5))
                .build());
    }

    public void xmlClick(View view) {
        LanguageBean select = adapter.getSelect();
        if (select != null) {
            if (LanguageUtil.updateLanguage(this, select)) {
                // 跳转到首页并重启首页
                AppPageDispatch.startMainPageRecreate(this);
            } else {
                finish();
            }
        }
    }

}
