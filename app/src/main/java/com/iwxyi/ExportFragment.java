package com.iwxyi;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.iwxyi.BillsFragment.DummyContent;
import com.iwxyi.Utils.FileUtil;
import com.iwxyi.Utils.Global;
import com.iwxyi.Utils.SqlUtil;
import com.iwxyi.Utils.StreamUtil;
import com.iwxyi.Utils.StringUtil;
import com.iwxyi.Utils.UserInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnExportFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExportFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final int SYNC_RESULT = 1;
    private final int SYNC_PROCESS = 2;
    private final int SYNC_WRONG = 3;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SweetAlertDialog pDialog;

    private OnExportFragmentInteractionListener mListener;
    private Button mToSqlBtn;
    private Button mFromSqlBtn;
    private Button mToCloudBtn;
    private Button mFromCloudBtn;

    public ExportFragment() {
        // Required empty public constructor
    }

    private void initView(@NonNull final View itemView) {
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        mToSqlBtn = (Button) itemView.findViewById(R.id.btn_toSql);
        mToSqlBtn.setOnClickListener(this);
        mFromSqlBtn = (Button) itemView.findViewById(R.id.btn_fromSql);
        mFromSqlBtn.setOnClickListener(this);
        mToCloudBtn = (Button) itemView.findViewById(R.id.btn_toCloud);
        mToCloudBtn.setOnClickListener(this);
        mFromCloudBtn = (Button) itemView.findViewById(R.id.btn_fromCloud);
        mFromCloudBtn.setOnClickListener(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExportFragment newInstance(String param1, String param2) {
        ExportFragment fragment = new ExportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_export, container, false);
        initView(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onExportFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExportFragmentInteractionListener) {
            mListener = (OnExportFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnExportFragmentInteractionListener");
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
            case R.id.btn_toSql:
                backupToSql();
                Toast.makeText(getContext(), "备份到数据库成功\n可使用钛备份等工具统一保存", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_fromSql:
                resotreFromSql();
                Toast.makeText(getContext(), "从数据库还原成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_toCloud:
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("上传中");
                pDialog.setCustomImage(R.drawable.ic_dan);
                pDialog.setCancelable(false);
                pDialog.show();
                uploadAll();
                break;
            case R.id.btn_fromCloud:
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("下载中 0/5 ...");
                pDialog.setCustomImage(R.drawable.ic_dan);
                pDialog.setCancelable(false);
                pDialog.show();
                downloadAll();
                break;
            default:
                break;
        }
    }

    private void backupToSql() {
        SqlUtil.backupToSql();
    }

    private void resotreFromSql() {
        SqlUtil.restoreFromSql();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == SYNC_RESULT) {
                String s = (String) msg.obj;
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                pDialog.hide();
            } else if (msg.what == SYNC_PROCESS) {
                int process = msg.arg1;
                //Toast.makeText(getContext(), ""+process, Toast.LENGTH_SHORT).show();
                pDialog.setTitle("下载中 " + (process+1) + "/5 ...");
            } else if (msg.what == SYNC_WRONG) {
                Toast.makeText(getContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void uploadAll() {
        if (UserInfo.userID.equals("")) {
            Toast.makeText(getContext(), "请先点击左上角进行注册/登录", Toast.LENGTH_SHORT).show();
            return ;
        }
        new Thread() {
            public void run() {
                String path = Global.URL_DOMAIN + "uploadcontent.php";
                path += "?userID=" + UserInfo.userID + "&password=" + UserInfo.password;

                path += "&bills=" + URLEncoder.encode(FileUtil.readTextVals("bills.txt"));
                path += "&cards=" + URLEncoder.encode(FileUtil.readTextVals("cards.txt"));
                path += "&kinds_spending=" + URLEncoder.encode(FileUtil.readTextVals("kinds_spending.txt"));
                path += "&kinds_income=" + URLEncoder.encode(FileUtil.readTextVals("kinds_income.txt"));
                path += "&kinds_borrowing=" + URLEncoder.encode(FileUtil.readTextVals("kinds_borrowing.txt"));
                URL url = null;
                try {
                    url = new URL(path);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setConnectTimeout(5000);
                    int code = urlConnection.getResponseCode();
                    if (code == 200) {
                        InputStream in = urlConnection.getInputStream();
                        String content = StreamUtil.readStream(in);
                        Message msg = Message.obtain();
                        msg.what = SYNC_RESULT;
                        if (!"OK".equals(StringUtil.getXml(content, "STATE"))) {
                            msg.obj = StringUtil.getXml(content, "REASON");
                        } else {
                            msg.obj = "上传成功";
                        }
                        handler.sendMessage(msg);
                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void downloadAll() {
        if (UserInfo.userID.equals("")) {
            Toast.makeText(getContext(), "请先点击左上角进行注册/登录", Toast.LENGTH_SHORT).show();
            return ;
        }
        String path = Global.URL_DOMAIN + "downloadcontent.php";
        path += "?userID=" + UserInfo.userID + "&password=" + UserInfo.password;

        final String[] fields = new String[]{ "bills", "cards", "kinds_spending", "kinds_income", "kinds_borrowing" };
        final String finalPath = path;
        new Thread() {
            public void run() {
                for (int i = 0; i < fields.length; i++) {
                    String tPath = finalPath + "&field=" + fields[i];
                    URL url = null;
                    try {
                        url = new URL(tPath);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    try {
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setConnectTimeout(5000);
                        int code = urlConnection.getResponseCode();
                        if (code == 200) {
                            InputStream in = urlConnection.getInputStream();
                            String content = StreamUtil.readStream(in);
                            Message msg = Message.obtain();
                            if ("OK".equals(StringUtil.getXml(content, "STATE"))) {
                                msg.what = SYNC_PROCESS;
                                msg.arg1 = i+1;
                                if (!"".equals(content)) {
                                    content = StringUtil.getXml(content, "CONTENT");
                                    FileUtil.writeTextVals(fields[i]+".txt", content);
                                    Log.i("====write download content " + fields[i], content);
                                }
                            } else {
                                msg.what = SYNC_WRONG;
                                msg.obj = "出现了问题：" + StringUtil.getXml(content, "REASON");
                                Log.i("====download:", tPath);
                            }
                            handler.sendMessage(msg);
                        }
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Message msg = Message.obtain();
                msg.what = SYNC_RESULT;
                msg.obj = "下载完毕";
                handler.sendMessage(msg);
                DummyContent.readFromFile();
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
    public interface OnExportFragmentInteractionListener {
        // TODO: Update argument type and name
        void onExportFragmentInteraction(Uri uri);
    }
}
