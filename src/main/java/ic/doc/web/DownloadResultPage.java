package ic.doc.web;

import ic.doc.FileType;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

public class DownloadResultPage implements Page {

    private final String query;
    private final Map<String, String> answer;
    private final FileType fileType;
    private final String fileName;

    public DownloadResultPage(String query, Map<String, String> answer, FileType fileType) {
        this.query = query;
        this.answer = answer;
        this.fileType = fileType;
        this.fileName =  "query_" + (query.isEmpty() ? "empty" : query);
    }

    public void writeTo(HttpServletResponse resp) throws IOException {
        switch (fileType) {
            case MARKDOWN:
                createMarkdown(resp);
                break;
            case PDF:
                createPDF(resp);
                break;
            default:
                throw new IllegalArgumentException("File Type: " + fileType.name() + " not recognised");
        }
    }

    private void createMarkdown(HttpServletResponse resp) throws IOException {
        File tempFile = writeMarkDownFile();

        resp.setContentType("text/markdown");
        resp.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".md");

        InputStream fileStream = new FileInputStream(tempFile.getAbsoluteFile());
        resp.getOutputStream().write(fileStream.readAllBytes());
        fileStream.close();

        tempFile.deleteOnExit();

    }

    private File writeMarkDownFile() throws IOException {
        File tempFile = new File(fileName + ".md");
        OutputStream outputStream = new FileOutputStream(tempFile);

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

        if (answer.isEmpty()) {
            bufferedWriter.write("**Sorry**\n");
            bufferedWriter.write("Sorry, we didn't understand " + query + "\n");
        } else {
            // Header
            bufferedWriter.write("---\n");
            bufferedWriter.write("# " + query + "\n");
            bufferedWriter.write("---\n");

            for (Map.Entry<String, String> entry : answer.entrySet()) {

                String query = entry.getKey();
                String answer = entry.getValue();

                // Content
                if (answer == null || answer.isEmpty()) {
                    bufferedWriter.write("**Sorry**\n");
                    bufferedWriter.write("Sorry, we didn't understand " + query + "\n");
                } else {
                    bufferedWriter.write("### " + query + "\n");
                    bufferedWriter.write(answer);
                    bufferedWriter.write('\n');
                }
            }
        }

        bufferedWriter.close();

        return tempFile;
    }

    private void createPDF(HttpServletResponse resp) throws IOException {
        resp.setContentType("text/pdf");
        resp.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".pdf");

        File tempFile = writeMarkDownFile();

        try {
            new ProcessBuilder("pandoc", "-s", "-o", fileName + ".pdf", fileName + ".md")
                .start().waitFor();
        } catch (InterruptedException e) {
            System.out.println("The pdf creation failed. Please try again.");
        }

        File query = new File(fileName + ".pdf");

        InputStream fileStream = new FileInputStream(query.getAbsoluteFile());
        OutputStream respStream = resp.getOutputStream();

        respStream.write(fileStream.readAllBytes());

        tempFile.deleteOnExit();

    }
}
