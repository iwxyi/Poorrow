package com.iwxyi;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUtil {

    /**
     * 寻找程序数据存储的外部文件夹
     * @return 文件夹路径（带"/"）
     */
    public static String getFolder() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getPath() + "/Poorrow/";
        }
        else {
            return "/storage/emulated/0/Poorrow/";
        }
    }

    /**
     * 写入设置到对应的文件
     * @param fileName 文件名
     * @param text 文件内容
     * @return 写入文件是否成功
     */
    public static boolean writeTextVals(String fileName, String text) {
        return writeTextFile(getFolder()+fileName, text);
    }

    public static String readTextVals(String fileName) throws IOException {
        return readTextFile(getFolder()+fileName);
    }

    public static boolean writeTextFile(String filePath, String text) {
        try{
            // 创建 File类 指定数据存储的位置
            File file = new File(filePath);
            // 创建一个文件输出流
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.close();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String readTextFile(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        BufferedReader bufr = new BufferedReader(new InputStreamReader(fis));
        StringBuilder sb = new StringBuilder();
        String line;
        while ( (line = bufr.readLine()) != null ) {
            sb.append(line);
        }
        return sb.toString();
    }

}
