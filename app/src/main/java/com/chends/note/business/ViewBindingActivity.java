package com.chends.note.business;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.chends.note.R;
import com.chends.note.base.BaseActivity;
import com.chends.note.databinding.ActivityViewBindingBinding;

/**
 * @author cds created on 2019/10/27.
 */
public class ViewBindingActivity extends BaseActivity<ActivityViewBindingBinding> {

    @Override
    protected void initViewBinding() {
        viewBinding = ActivityViewBindingBinding.inflate(LayoutInflater.from(this));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding.text.setText("view binding");
        viewBinding.image.setImageResource(R.color.black);
    }
}
