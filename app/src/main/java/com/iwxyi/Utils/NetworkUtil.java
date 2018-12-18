package com.iwxyi.Utils;

import com.iwxyi.Utils.Global;
import com.iwxyi.Utils.StreamUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class NetworkUtil {
    public static String getWebpageSource(String path) {
        URL url = null;
        try {
            url = new URL(path);
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
                return content;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
