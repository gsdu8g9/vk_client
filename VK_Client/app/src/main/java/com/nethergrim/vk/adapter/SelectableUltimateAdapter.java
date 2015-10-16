package com.nethergrim.vk.adapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Andrew Drobyazko (c2q9450@gmail.com) on 16.10.15.
 *         All rights reserved.
 */
public abstract class SelectableUltimateAdapter extends UltimateAdapter {

    private Map<Long, Boolean> mSelectedPositionsArray;


    public SelectableUltimateAdapter() {
        mSelectedPositionsArray = new HashMap<>(50);
    }

    public void toggle(int position) {
        boolean isSelected = isSelected(position);
        mSelectedPositionsArray.put(getDataId(position), !isSelected);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelectedPositionsArray.clear();
        notifyDataSetChanged();
    }

    public boolean isSelected(int position) {
        return mSelectedPositionsArray.get(getDataId(position));
    }

    public Set<Long> getSelectedIds() {
        Set<Long> result = new HashSet<>(mSelectedPositionsArray.size());
        for (Long id : mSelectedPositionsArray.keySet()) {
            if (mSelectedPositionsArray.get(id)) {
                result.add(id);
            }
        }
        return result;
    }
}
