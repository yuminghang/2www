package com.delin.dgclient.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.delin.dgclient.R;

import java.util.ArrayList;


public class ListPopupWindowAdapter extends BaseAdapter {
    private ArrayList strings;
    private Context mContext;
    public ListPopupWindowAdapter(ArrayList arrayList, Context context) {
        super();
        this.strings = arrayList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (strings == null) {
            return 0;
        } else {
            return strings.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (strings == null) {
            return null;
        } else {
            return strings.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_spinner, parent, false);
            holder.itemTextView = (TextView) convertView.findViewById(R.id.orderTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (this.strings != null) {
            final String itemName = (String) this.strings.get(position);
            if (holder.itemTextView != null) {
                holder.itemTextView.setText(itemName);
            }
        }

        return convertView;

    }

    private class ViewHolder {
        TextView itemTextView;
    }

}
