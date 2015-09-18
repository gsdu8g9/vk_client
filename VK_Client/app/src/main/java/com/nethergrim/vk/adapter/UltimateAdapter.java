package com.nethergrim.vk.adapter;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Andrew Drobyazko (andrey.drobyazko@applikeysolutions.com) on 06.09.15.
 */
public abstract class UltimateAdapter
        extends RecyclerView.Adapter<UltimateAdapter.DataVH> {


    public static final int HEADER_TYPE_ID = -1;
    public static final int FOOTER_TYPE_ID = -2;
    protected LayoutInflater mLayoutInflater;
    private int mFooterVisibility;
    private int mHeaderVisibility;

    public interface HeaderInterface {

        HeaderVH getHeaderVH(View v);

        @LayoutRes
        int getHeaderViewResId();

        void bindHeaderVH(HeaderVH vh);
    }

    public interface FooterInterface {

        FooterVH getFooterVH(View v);

        @LayoutRes
        int getFooterViewResId();

        void bindFooterVH(FooterVH vh);
    }

    public UltimateAdapter() {
        setHasStableIds(true);
        // check footer
    }

    @Override
    public final DataVH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        if (viewType == HEADER_TYPE_ID) {
            HeaderInterface headerInterface = getThisHeader();
            return headerInterface.getHeaderVH(
                    getViewById(headerInterface.getHeaderViewResId(), parent));
        } else if (viewType == FOOTER_TYPE_ID) {
            FooterInterface footerInterface = getThisFooter();
            return footerInterface.getFooterVH(
                    getViewById(footerInterface.getFooterViewResId(), parent));
        } else {
            View v = getViewById(getDataViewResId(viewType), parent);
            return getDataViewHolder(v, viewType);
        }
    }

    @Override
    public final void onBindViewHolder(DataVH holder, int position) {
        if (position == 0 && withHeader()) {
            // bind header
            getThisHeader().bindHeaderVH((HeaderVH) holder);
            ((HeaderVH) holder).itemView.setVisibility(mHeaderVisibility);
        } else if (position == getFooterPosition() && withFooter()) {
            // bind footer
            getThisFooter().bindFooterVH((FooterVH) holder);
            ((FooterVH) holder).itemView.setVisibility(mFooterVisibility);
        } else {
            // bind data
            if (withHeader()) {
                position -= 1;
            }
            bindDataVH(holder, position);
        }
    }

    @Override
    public final int getItemViewType(int absolutePosition) {
        if (absolutePosition == 0 && withHeader()) {
            return HEADER_TYPE_ID;
        } else if (withFooter() && absolutePosition == getFooterPosition()) {
            return FOOTER_TYPE_ID;
        } else {
            return getDataViewType(convertAbsolutePositionToData(absolutePosition));
        }
    }

    @Override
    public final long getItemId(int position) {
        if (position == 0 && withHeader()) {
            return -111l;
        } else if (withFooter() && position == getFooterPosition()) {
            return -222l;
        } else {
            return getDataId(position);
        }
    }

    @Override
    public final int getItemCount() {
        int result = 0;
        if (withFooter()) {
            result++;
        }
        if (withHeader()) {
            result++;
        }
        result += getDataSize();
        return result;
    }

    public boolean withFooter() {
        return this instanceof FooterInterface;
    }

    public boolean withHeader() {
        return this instanceof HeaderInterface;
    }

    public abstract int getDataSize();

    public abstract int getDataViewResId(int viewType);

    public abstract long getDataId(int dataPosition);

    public abstract int getDataViewType(int dataPosition);

    public abstract DataVH getDataViewHolder(View v, int dataViewType);

    public abstract void bindDataVH(DataVH vh, int dataPosition);

    public void setFooterVisibility(int visibility) {
        mFooterVisibility = visibility;
        notifyDataSetChanged();
    }

    public void setHeaderVisibility(int visibility) {
        mHeaderVisibility = visibility;
        notifyDataSetChanged();
    }

    protected int convertAbsolutePositionToData(int absolutePosition) {
        int result = absolutePosition;
        if (withHeader()) {
            result--;
        }
        return result;
    }

    protected int getFooterPosition() {
        int result = 0;
        if (withHeader()) {
            result++;
        }
        result += getDataSize();
        return result;
    }

    private View getViewById(@LayoutRes int id, ViewGroup parent) {
        return mLayoutInflater.inflate(id, parent, false);
    }

    private FooterInterface getThisFooter() {
        return (FooterInterface) this;
    }

    private HeaderInterface getThisHeader() {
        return (HeaderInterface) this;
    }

    public static class DataVH extends RecyclerView.ViewHolder {

        public DataVH(View itemView) {
            super(itemView);
        }
    }

    public static class HeaderVH extends DataVH {

        public HeaderVH(View itemView) {
            super(itemView);
        }
    }

    public static class FooterVH extends DataVH {

        public FooterVH(View itemView) {
            super(itemView);
        }
    }
}
