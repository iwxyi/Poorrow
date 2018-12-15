package com.iwxyi.Fragments.BillsList;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iwxyi.Fragments.BillsList.BillsFragment.OnListFragmentInteractionListener;
import com.iwxyi.Fragments.BillsList.DummyContent.DummyItem;
import com.iwxyi.R;
import com.iwxyi.Utils.DateTimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBillRecyclerViewAdapter extends RecyclerView.Adapter<MyBillRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyBillRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bill, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTvSource.setText(mValues.get(position).source);
        holder.mTvAmount.setText(mValues.get(position).amount+"");
        if ("".equals(mValues.get(position).note)) {
            holder.mTvNote.setVisibility(View.GONE);
        } else {
            holder.mTvNote.setVisibility(View.VISIBLE);
            holder.mTvNote.setText(mValues.get(position).note);
        }
        if (mValues.get(position).timestamp != 0) { // 如果为空，就不显示了
            try {
                holder.mTvTime.setText(DateTimeUtil.longToString(mValues.get(position).timestamp, "MM-dd HH:mm"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else { // 如果时间戳是空的，那么就改成添加的时间
            try {
                holder.mTvTime.setText(DateTimeUtil.longToString(mValues.get(position).addTime, "MM-dd HH:mm"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTvSource;
        public final TextView mTvAmount;
        public final TextView mTvNote;
        public final TextView mTvTime;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTvSource = (TextView) view.findViewById(R.id.tv_source);
            mTvAmount = (TextView) view.findViewById(R.id.tv_amount);
            mTvNote = (TextView) view.findViewById(R.id.tv_note);
            mTvTime = (TextView) view.findViewById(R.id.tv_time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTvSource.getText() + "'";
        }
    }
}
