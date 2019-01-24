package ic.doc.web;


import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class IndexPageTest {

    @Mocked
    private HttpServletResponse response;

    @Injectable
    private PrintWriter printWriter;

    private final Page indexPage = new IndexPage();

    @Test
    public void writeToSetsContentTypeToHTML() {
        try {
            indexPage.writeTo(response);
        } catch (IOException e) {
            /* Expected exception when using mocked response */
        } catch (NullPointerException e) {
            /* The responses writer will be null */
        }

        new Verifications() {{
            response.setContentType("text/html"); times = 1;
        }};
    }

    @Test
    public void writeToGetsTheResponsesWriter() throws IOException {

        new Expectations() {{
            response.getWriter(); result = printWriter;
        }};

        try {
            indexPage.writeTo(response);
        } catch (NullPointerException e) {
            /* The responses writer will be null */
        }

        new Verifications() {{
            response.setContentType(anyString); times = 1;
            response.getWriter(); times = 1;
        }};
    }

    @Test
    public void writeToUsesTheResponsesWriter() throws IOException {

        new Expectations() {{
            response.getWriter(); result = printWriter;
        }};

        indexPage.writeTo(response);

        new Verifications() {{
            response.setContentType(anyString); times = 1;
            response.getWriter(); times = 1;
            printWriter.println(anyString); minTimes = 1;
        }};
    }
}
