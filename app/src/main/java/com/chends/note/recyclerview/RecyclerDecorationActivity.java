package com.chends.note.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chends.note.R;
import com.chends.note.base.BaseActivity;
import com.chends.note.utils.DisplayUtil;
import com.chends.note.utils.ItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * 分割线
 * @author chends create on 2019/7/23.
 */
public class RecyclerDecorationActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) return 4;
                return 1;
            }

        });
        recyclerView.setLayoutManager(layoutManager);
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 90; i++) {
            list.add(i);
        }
        recyclerView.addItemDecoration(new ItemDecoration
                .Builder(4)
                .setIncludeTB(true)
                .setPositionOffset(1)
                .setIncludeLR(true)
                .setDecoration(DisplayUtil.dp2px(10))
                .build());
        recyclerView.setAdapter(new DecorationAdapter(this, list));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_recycler_decoration;
    }
}
