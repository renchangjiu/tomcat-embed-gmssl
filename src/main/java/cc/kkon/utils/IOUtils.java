package cc.kkon.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class IOUtils {

    public static String toString(InputStream in, Charset cs) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int len;
            byte[] bytes = new byte[8 * 1024];
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
                out.flush();
            }
            String ret = out.toString(cs.name());
            in.close();
            out.close();
            return ret;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
