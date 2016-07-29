package com.nethergrim.vk.adapter;

import android.util.LongSparseArray;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Andrew Drobyazko - c2q9450@gmail.com - https://nethergrim.github.io on 16.10.15.
 *         All rights reserved.
 */
public abstract class SelectableUltimateAdapter extends UltimateAdapter {


    private LongSparseArray<Boolean> mSelectedPositionsArray;

    SelectableUltimateAdapter() {
        mSelectedPositionsArray = new LongSparseArray<>(50);
    }

    public void toggle(int dataPosition) {
        boolean isSelected = isSelected(dataPosition);
        mSelectedPositionsArray.put(getDataId(dataPosition), !isSelected);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelectedPositionsArray.clear();
        notifyDataSetChanged();
    }

    boolean isSelected(int dataPosition) {
        Boolean result = mSelectedPositionsArray.get(getDataId(dataPosition));
        if (result == null) {
            return false;
        }
        return result;
    }

    public Set<Long> getSelectedIds() {
        int keysSize = mSelectedPositionsArray.size();
        Set<Long> result = new HashSet<>(keysSize);
        for (int i = 0; i < keysSize; i++) {
            long key = mSelectedPositionsArray.keyAt(i);
            boolean value = mSelectedPositionsArray.get(key);
            if (value) {
                result.add(key);
            }
        }
        return result;
    }
}
