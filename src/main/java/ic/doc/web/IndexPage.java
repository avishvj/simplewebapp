package ic.doc.web;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class IndexPage implements Page {

    public void writeTo(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();

        // Header
        writer.println("<html>");
        writer.println("<head><title>Welcome</title></head>");
        writer.println("<body>");

        // Content
        writer.println(
                "<h1>Welcome!!</h1>" +
                        "<p>Enter your query in the box below: " +
                        "<form>" +
                        "<input type=\"text\" name=\"q\" />" +
                        "<br><br><input type=\"radio\" name=\"download\" value=\"HTML\">" +
                        "HTML <br>" +
                        "<input type=\"radio\" name=\"download\" value=\"MARKDOWN\">" +
                        "MARKDOWN <br>" +
                        "<input type=\"radio\" name=\"download\" value=\"PDF\">" +
                        "PDF <br>" +
                        "<br><br><input type=\"submit\">" +
                        "</form>" +
                        "</p>");

        // Footer
        writer.println("</body>");
        writer.println("</html>");
    }

}
