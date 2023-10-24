package cc.kkon;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author yui
 */
@WebServlet("/")
public class EchoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cipherSuite = (String) req.getAttribute("javax.servlet.request.cipher_suite");
        System.out.println("cipherSuite = " + cipherSuite);

        String q = req.getParameter("q");
        if (q == null || q.equals("")) {
            q = "Hello!";
        }
        ServletOutputStream out = resp.getOutputStream();
        out.write(q.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }
}
