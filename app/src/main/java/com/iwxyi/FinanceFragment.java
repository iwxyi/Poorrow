package com.iwxyi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iwxyi.BillsFragment.DummyContent;


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
        financialStatistics();
    }

    private void financialStatistics() {
        double todayIn, todayOut;
        double yestodayIn, yestodayOut;
        double weekIn, weekOut;
        double monthIn, monthOut;
        for (DummyContent.DummyItem item : DummyContent.ITEMS) {
            ;
        }
    }

    private void initView(@NonNull final View itemView) {
        mTodayTv = (TextView) itemView.findViewById(R.id.tv_today);
        mYesdayTv = (TextView) itemView.findViewById(R.id.tv_yesday);
        mWeekTv = (TextView) itemView.findViewById(R.id.tv_week);
        mMonthTv = (TextView) itemView.findViewById(R.id.tv_month);
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
