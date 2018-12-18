package com.iwxyi.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
    public static String readStream(InputStream in) throws IOException {
        int len = -1;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ( (len = in.read(buffer)) != -1 ) {
            baos.write(buffer, 0, len);
        }
        in.close();
        String content = new String(baos.toByteArray());
        return content;
    }
}
