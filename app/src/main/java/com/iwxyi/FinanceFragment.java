package com.iwxyi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iwxyi.BillsFragment.DummyContent;
import com.iwxyi.Utils.DateTimeUtil;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFinanceFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FinanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinanceFragment extends Fragment {

    private OnFinanceFragmentInteractionListener mListener;
    private TextView mTodayTv;
    private TextView mYesdayTv;
    private TextView mWeekTv;
    private TextView mMonthTv;

    public FinanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FinanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FinanceFragment newInstance() {
        FinanceFragment fragment = new FinanceFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_finance, container, false);
        initView(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFinanceFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFinanceFragmentInteractionListener) {
            mListener = (OnFinanceFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPredictFragmentInteractionListener");
        }
    }

    private void financialStatistics() {
        long timestamp, addTime;
        int year, month, date, hour, minute;
        double amount;
        double todayIn = 0, todayOut = 0;
        double yestodayIn = 0, yestodayOut = 0;
        double weekIn = 0, weekOut = 0;
        double monthIn = 0, monthOut = 0;
        Calendar c = Calendar.getInstance();
        int _year = c.get(Calendar.YEAR);
        int _month = c.get(Calendar.MONTH) + 1;
        int _date = c.get(Calendar.DAY_OF_MONTH);
        long todayTimestamp = DateTimeUtil.valsToTimestamp(_year, _month, _date, 0, 0, 0);
        long yestodayTimestamp = todayTimestamp-1*24*3600*1000;
        long weekTimestamp = todayTimestamp-7*24*3600*1000;
        long monthTimestamp = DateTimeUtil.valsToTimestamp(_year, _month, _date, 0, 0, 0);
        for (DummyContent.DummyItem item : DummyContent.ITEMS) {
            timestamp = item.timestamp;
            if (timestamp == 0) {
                timestamp = item.addTime;
            }
            amount = item.amount;
            if (timestamp > todayTimestamp) { // 今日
                if (amount >= 0) {
                    todayIn += amount;
                } else {
                    todayOut += amount;
                }
            } else if (timestamp > yestodayTimestamp) { // 昨日
                if (amount > 0) {
                    yestodayIn += amount;
                } else {
                    yestodayOut += amount;
                }
            }
            if (timestamp > weekTimestamp) { // 七天
                if (amount > 0) {
                    weekIn += amount;
                } else {
                    weekOut += amount;
                }
            }
            if (timestamp > monthTimestamp) { // 本月
                if (amount > 0) {
                    monthIn += amount;
                } else {
                    monthOut += amount;
                }
            }
        }
        mTodayTv.setText("今日收入：" + todayIn + "\n今日支出：" + todayOut + "\n今日统计：" + (todayIn+todayOut));
        mYesdayTv.setText("昨日收入：" + yestodayIn + "\n本月支出：" + yestodayOut + "\n昨日统计：" + (yestodayIn+yestodayOut));
        mWeekTv.setText("七天收入：" + weekIn + "\n七天支出：" + weekOut + "\n七天统计：" + (weekIn+weekOut));
        mMonthTv.setText("本月收入：" + monthIn + "\n本月支出：" + monthOut + "\n本月统计：" + (monthIn+monthOut));
    }

    private void initView(@NonNull final View itemView) {
        mTodayTv = (TextView) itemView.findViewById(R.id.tv_today);
        mYesdayTv = (TextView) itemView.findViewById(R.id.tv_yesday);
        mWeekTv = (TextView) itemView.findViewById(R.id.tv_week);
        mMonthTv = (TextView) itemView.findViewById(R.id.tv_month);
        financialStatistics();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFinanceFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFinanceFragmentInteraction(Uri uri);
    }
}
