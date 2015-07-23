package com.nethergrim.vk.models.push;

/**
 * @author andrej on 23.07.15.
 */
public class PushCall extends PushObject {

    @Override
    public PushType getPushType() {
        return PushType.Call;
    }
}
