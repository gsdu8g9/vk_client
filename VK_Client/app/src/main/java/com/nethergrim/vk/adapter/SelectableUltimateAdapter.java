package com.nethergrim.vk.adapter;

import android.util.SparseBooleanArray;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@applikeysolutions.com) on 16.10.15.
 */
public abstract class SelectableUltimateAdapter extends UltimateAdapter {

    private SparseBooleanArray mSelectedPositionsArray;

    public SelectableUltimateAdapter() {
        mSelectedPositionsArray = new SparseBooleanArray(50);
    }

    public void toggle(int position) {
        boolean isSelected = isSelected(position);
        mSelectedPositionsArray.put(position, !isSelected);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelectedPositionsArray.clear();
        notifyDataSetChanged();
    }

    public boolean isSelected(int position) {
        return mSelectedPositionsArray.get(position);
    }

    public Set<Long> getSelectedIds() {
        Set<Long> result = new HashSet<>(mSelectedPositionsArray.size());
        for (int i = 0, size = mSelectedPositionsArray.size(); i < size; i++) {
            int position = mSelectedPositionsArray.keyAt(i);
            if (mSelectedPositionsArray.get(position)) {
                result.add(getDataId(position));
            }
        }
        return result;
    }
}
