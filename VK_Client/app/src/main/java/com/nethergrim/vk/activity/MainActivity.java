package com.nethergrim.vk.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.nethergrim.vk.R;
import com.nethergrim.vk.fragment.MessagesFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AbstractActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        showFragment(new MessagesFragment(), false, false, R.id.fragment_container);
    }

}
