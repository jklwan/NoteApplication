package com.chends.note.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.chends.note.R;
import com.chends.note.base.BaseAdapter;
import com.chends.note.base.BaseHolder;
import com.chends.note.model.LanguageBean;

import java.util.List;

/**
 * @author cds created on 2019/8/4.
 */
public class LanguageAdapter extends BaseAdapter<LanguageBean> {
    private LanguageBean select;

    public LanguageAdapter(Context context, List<LanguageBean> list, String selectKey) {
        super(context, list);
        if (list != null) {
            for (LanguageBean item : list) {
                if (TextUtils.equals(item.key, selectKey)) {
                    select = item;
                }
            }
        }
    }

    @NonNull
    @Override
    public BaseHolder<LanguageBean> onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemHolder(inflate(R.layout.item_language, viewGroup));
    }


    /**
     * 切换选择
     */
    public void switchSelect(LanguageBean bean) {
        if (bean != null) {
            select = bean;
            notifyDataSetChanged();
        }
    }

    /**
     * 返回选择项
     * @return select
     */
    public LanguageBean getSelect() {
        return select;
    }

    public class ItemHolder extends BaseHolder<LanguageBean> {
        CheckedTextView check;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.check);
        }

        @Override
        public void onBind(LanguageBean data) {
            if (data != null) {
                check.setText(data.name);
                if (select != null && TextUtils.equals(select.key, data.key)) {
                    check.setChecked(true);
                } else {
                    check.setChecked(false);
                }
            }
        }
    }
}
