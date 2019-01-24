package ic.doc.web;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class HTMLResultPage implements Page {

    private final String query;
    private final Map<String, String> results;

    public HTMLResultPage(String query, Map<String, String> results) {
        this.query = query;
        this.results = results;
    }

    public void writeTo(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();

        writer.println("<html>");

        writer.println("<body>");
        writer.println("<h5> You searched: " + query + "</h5>");

        for (Map.Entry<String, String> entry : results.entrySet()) {

            String query = entry.getKey();
            String answer = entry.getValue();

            // Header
            writer.println("<head><title>" + query + "</title></head>");
            writer.println("<body>");

            // Content
            if (answer == null || answer.isEmpty()) {
                writer.println("<h1>Sorry</h1>");
                writer.print("<p>Sorry, we didn't understand <em>" + query + "</em></p>");
            } else {
                writer.println("<h1>" + query + "</h1>");
                writer.println("<p>" + answer.replace("\n", "<br>") + "</p>");
            }

            // Footer
            writer.println("</body>");
        }
        writer.println("<p><a href=\"/\">Back to Search Page</a></p>");

        writer.println("</html>");
    }
}
