package com.chends.note.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chends.note.R;
import com.chends.note.base.BaseAdapter;
import com.chends.note.base.BaseHolder;

import java.util.List;

/**
 * @author chends create on 2019/7/23.
 */
public class MainAdapter extends BaseAdapter<MainBean> {

    public MainAdapter(Context context, List<MainBean> list) {
        super(context, list);
    }

    @NonNull
    @Override
    public BaseHolder<MainBean> onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        return new MainHolder(inflate(R.layout.item_main, parent));
    }

    public class MainHolder extends BaseHolder<MainBean> {
        private TextView title;

        public MainHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }

        @Override
        public void onBind(MainBean data) {
            title.setText(data.message);
        }
    }
}
