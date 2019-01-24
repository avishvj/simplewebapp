package ic.doc.web;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;

public class HTMLResultPageTest {

    @Mocked
    private HttpServletResponse response;

    @Injectable
    private PrintWriter printWriter;

    @Test
    public void setsContentTypeToHTML() throws IOException {
        new Expectations() {{
            response.setContentType("text/html");
            times = 1;
        }};

        Page page = new HTMLResultPage("", Collections.emptyMap());

        try {
            page.writeTo(response);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void acquiresResponseWriter() throws IOException {
        new Expectations() {{
            response.getWriter();
            result = printWriter;
        }};

        Page page = new HTMLResultPage("", Collections.emptyMap());

        page.writeTo(response);
    }
    @Test
    public void usesResponseWriter() throws IOException {
        new Expectations() {{
            response.getWriter();
            result = printWriter;
        }};

        Page page = new HTMLResultPage("", Collections.emptyMap());

        page.writeTo(response);

        new Verifications() {{
           printWriter.println("<html>"); times = 1;
            printWriter.println("</html>"); times = 1;
            printWriter.println(anyString); minTimes = 2;
        }};
    }
}
