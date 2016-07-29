package com.nethergrim.vk.models.push;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 23.07.15.
 */
public class PushCall extends PushObject {

    @Override
    public PushType getPushType() {
        return PushType.Call;
    }
}
