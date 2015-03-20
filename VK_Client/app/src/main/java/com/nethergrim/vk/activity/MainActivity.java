package com.nethergrim.vk.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.nethergrim.vk.R;
import com.nethergrim.vk.fragment.MessagesFragment;

public class MainActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        showFragment(new MessagesFragment(), false, false, R.id.fragment_container);
    }

}
