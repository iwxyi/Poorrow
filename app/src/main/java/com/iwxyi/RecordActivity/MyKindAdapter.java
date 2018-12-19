package com.iwxyi.RecordActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iwxyi.R;

import java.util.ArrayList;

public class MyKindAdapter extends BaseAdapter {

    private LayoutInflater mInflater = null;
    private Context mContext = null;
    private ArrayList<KindBean> mData = null;

    public MyKindAdapter(Context context, ArrayList<KindBean> arrayList) {
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
        KindBean data = (KindBean)getItem(position);
        String name = data.name;
        int img = data.img;
        boolean ch = data.choose;
        if (convertView == null) {
            convertView = mInflater.from(mContext).inflate(R.layout.item_kind, parent, false);
            holder = new ViewHolder();
            holder.ll = (LinearLayout) convertView.findViewById(R.id.ll_all);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_name);

            holder.tv.setText(name);
            if (ch) {
                holder.ll.setBackgroundResource(R.drawable.orange_border);
            } else {
                holder.ll.setBackgroundResource(0);
            }
            holder.iv.setImageResource(img);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // holder.iv.setImage...
        //holder.iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_eye));
        //holder.tv.setText(name); // 放在这里会出null错的诶？
        return convertView; // 每次都忘记修改null，真气人
    }

    class ViewHolder {
        ImageView iv;
        TextView tv;
        LinearLayout ll;
    }
}
