/*
 * Copyright 2014 Ankush Sachdeva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.ankushsachdeva.emojicon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import github.ankushsachdeva.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import github.ankushsachdeva.emojicon.emoji.Emojicon;


/**
 * @author Ankush Sachdeva (sankush@yahoo.co.in)
 */
class EmojiAdapter extends ArrayAdapter<Emojicon> implements View.OnClickListener {

    private OnEmojiconClickedListener mClickedListener;
    private LayoutInflater mLayoutInflater;

    public EmojiAdapter(Context context, Emojicon[] data) {
        super(context, R.layout.emojicon_item, data);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setClickedListener(OnEmojiconClickedListener listener) {
        this.mClickedListener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder vh;
        if (v == null) {
            v = mLayoutInflater.inflate(R.layout.emojicon_item, parent, false);
            vh = new ViewHolder(v, position);
            v.setTag(vh);
        } else {
            vh = (ViewHolder) v.getTag();
        }
        Emojicon emoji = getItem(position);
        vh.icon.setText(emoji.getEmoji());
        vh.icon.setOnClickListener(this);
        vh.position = position;
        return v;
    }

    @Override
    public void onClick(View v) {
        ViewHolder vh = (ViewHolder) v.getTag();
        mClickedListener.onEmojiconClicked(getItem(vh.position));
    }

    private static class ViewHolder {

        TextView icon;
        int position;

        public ViewHolder(View v, int position) {
            icon = (TextView) v.findViewById(R.id.emojicon_icon);
            this.position = position;
        }
    }
}