package com.iwxyi;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.iwxyi.BillsFragment.DummyContent;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnPredictFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PredictFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PredictFragment extends Fragment implements View.OnClickListener {

    private final int MESSAGE_WHAT_PREDICT = 1;

    private OnPredictFragmentInteractionListener mListener;
    private Button mStartPredictBtn;
    private SweetAlertDialog pDialog;

    public PredictFragment() {
        // Required empty public constructor
    }

    private void initView(@NonNull final View itemView) {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        mStartPredictBtn = (Button) itemView.findViewById(R.id.btn_startPredict);
        mStartPredictBtn.setOnClickListener(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PredictFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PredictFragment newInstance() {
        return new PredictFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_predict, container, false);
        initView(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onPredictFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPredictFragmentInteractionListener) {
            mListener = (OnPredictFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnPredictFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_startPredict:
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("预测中");
                pDialog.setCustomImage(R.drawable.ic_dan);
                pDialog.setCancelable(true);
                pDialog.show();
                predict();
                break;
            default:
                break;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_WHAT_PREDICT) {
                pDialog.hide();

                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("预测结束...")
                        .setContentText("此功能基于大数据AI预测，您当前只有" + DummyContent.ITEMS.size() + "条数据，数据量远远不足，请过一段时间再试。")
                        .show();
            }
        }
    };

    private void predict() {
        new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = Message.obtain();
                msg.what = MESSAGE_WHAT_PREDICT;
                handler.sendMessage(msg);
            }
        }.start();
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
    public interface OnPredictFragmentInteractionListener {
        // TODO: Update argument type and name
        void onPredictFragmentInteraction(Uri uri);
    }
}
