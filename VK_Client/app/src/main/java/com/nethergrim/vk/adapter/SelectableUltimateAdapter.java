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

    public void toggle(int dataPosition) {
        boolean isSelected = isSelected(dataPosition);
        mSelectedPositionsArray.put(getDataId(dataPosition), !isSelected);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelectedPositionsArray.clear();
        notifyDataSetChanged();
    }

    public boolean isSelected(int dataPosition) {
        Boolean result = mSelectedPositionsArray.get(getDataId(dataPosition));
        if (result == null) {
            return false;
        }
        return result;
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
