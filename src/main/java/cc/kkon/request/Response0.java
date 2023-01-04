package cc.kkon.request;

import cc.kkon.utils.IOUtils;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 对 HttpResponse 的简单封装
 * @author yui
 */
public class Response0 {


    private HttpResponse httpResponse;

    private String content;


    Response0(HttpResponse httpResponse) throws IOException {
        this.httpResponse = httpResponse;
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            InputStream in = entity.getContent();
            this.content = IOUtils.toString(in, StandardCharsets.UTF_8);
            in.close();
        }
    }

    public String getHeader(String key) {
        HeaderElement[] elements = httpResponse.getFirstHeader(key).getElements();
        if (elements != null && elements.length != 0) {
            return elements[0].getName();
        }
        return null;
    }

    public String getContent() {
        return this.content;
    }

    public HttpResponse getHttpResponse() {
        return httpResponse;
    }
}
