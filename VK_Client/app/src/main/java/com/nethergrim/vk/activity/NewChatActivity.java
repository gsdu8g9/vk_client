package com.nethergrim.vk.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.devspark.robototextview.widget.RobotoEditText;
import com.nethergrim.vk.MyApplication;
import com.nethergrim.vk.R;
import com.nethergrim.vk.adapter.MultiUserAdapter;
import com.nethergrim.vk.models.User;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

/**
 * @author Andrew Drobyazko (c2q9450@gmail.com) on 05.09.15.
 */
public class NewChatActivity extends AbstractActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.etSearch)
    RobotoEditText mEtSearch;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private MultiUserAdapter mAdapter;

    public static void start(Context context) {
        context.startActivity(new Intent(context, NewChatActivity.class));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);
        ButterKnife.inject(this);
        MyApplication.getInstance().getMainComponent().inject(this);
        initToolbar();
        initRecyclerView();
        mEtSearch.clearFocus();
    }

    private void initToolbar() {
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        setTitle(R.string.new_dialog);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initRecyclerView() {
        RealmResults<User> results = mRealm.where(User.class).findAllSorted("firstName");
        mAdapter = new MultiUserAdapter(results);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
    }
}
