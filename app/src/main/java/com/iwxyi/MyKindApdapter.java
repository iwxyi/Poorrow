package com.iwxyi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyKindApdapter extends BaseAdapter {

    private LayoutInflater mInflater = null;
    private Context mContext = null;
    private ArrayList<String> mData = null;

    public MyKindApdapter(Context context, ArrayList<String> arrayList) {
        mContext = context;
        mData = arrayList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        String name = (String)getItem(position);
        if (convertView == null) {
            convertView = mInflater.from(mContext).inflate(R.layout.item_kind, parent, false);
            holder = new ViewHolder();
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv.setText(name);
            holder.iv.setImageResource(nameToImage(name));
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // holder.iv.setImage...
        //holder.iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_eye));
        //holder.tv.setText(name); // 放在这里会出null错的诶？
        return convertView; // 每次都忘记修改null，真气人
    }

    /**
     * 种类名字转换成图片路径名
     * @param name 种类名字
     * @return
     */
    private int nameToImage(String name) {
        return R.drawable.ic_card;
    }

    class ViewHolder {
        ImageView iv;
        TextView tv;
    }
}
