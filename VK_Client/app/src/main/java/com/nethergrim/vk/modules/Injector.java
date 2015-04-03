package com.nethergrim.vk.modules;

import dagger.ObjectGraph;

/**
 * @author andreydrobyazko on 4/3/15.
 */
public class Injector {

    private static Injector _instance = null;
    private ObjectGraph mGraph;

    private Injector(){
        mGraph = ObjectGraph.create(new ProviderModule());
    }

    public static Injector getInstance(){
        if (_instance == null)
            _instance = new Injector();
        return _instance;
    }

    public void inject(Object o){
        mGraph.inject(o);
    }


}
