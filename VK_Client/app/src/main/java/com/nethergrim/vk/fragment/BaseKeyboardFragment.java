package com.nethergrim.vk.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.devspark.robototextview.widget.RobotoEditText;
import com.nethergrim.vk.R;
import com.nethergrim.vk.activity.AbstractActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@applikeysolutions.com) on 03.10.15.
 */
public abstract class BaseKeyboardFragment extends AbstractFragment {


    @InjectView(R.id.btnLeft)
    ImageButton mBtnLeft;
    @InjectView(R.id.editText)
    RobotoEditText mEditText;
    @InjectView(R.id.btnRight)
    ImageButton mBtnRight;
    @InjectView(R.id.inputLayout)
    LinearLayout mInputLayout;
    @InjectView(R.id.divider)
    View mDivider;
    @InjectView(R.id.recycler)
    RecyclerView mRecycler;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_base_emoji_keyboard, container, false);
        ButterKnife.inject(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context ctx = view.getContext();

        initRecyclerView(mRecycler);
        preInitToolbar(mToolbar);
        initToolbar(mToolbar);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void preInitToolbar(Toolbar toolbar) {
        toolbar.setTitleTextColor(Color.WHITE);
        AbstractActivity abstractActivity = (AbstractActivity) getActivity();
        abstractActivity.setSupportActionBar(toolbar);
        abstractActivity.setTitle(getTitle());
        abstractActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public abstract void initRecyclerView(RecyclerView recycler);

    public abstract void postText(String text);

    public abstract String getTitle();

    protected abstract void initToolbar(Toolbar toolbar);
}
