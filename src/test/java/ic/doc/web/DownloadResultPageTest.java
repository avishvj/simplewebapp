package ic.doc.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ic.doc.FileType;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Test;

public class DownloadResultPageTest {

    @Mocked
    private HttpServletResponse response;

    @Test
    public void allFileTypesAvailableAreRecognised() {
        for (FileType type : FileType.values()) {
            DownloadResultPage downloadResultPage =
                    new DownloadResultPage("", Collections.emptyMap(), type);

            new Expectations() {{
                response.setContentType(anyString);
                times = 1;
                response.setHeader(anyString, anyString);
                times = 1;
            }};

            try {
                downloadResultPage.writeTo(response);
            } catch (IOException e) {
                /* To be dealt with in other tests */
            }
        }
    }

    @Test
    public void pdfIsRecognised() {
        DownloadResultPage downloadResultPage =
                new DownloadResultPage("", Collections.emptyMap(), FileType.PDF);

        new Expectations() {{
            response.setContentType("text/pdf");
            times = 1;
        }};

        try {
            downloadResultPage.writeTo(response);
        } catch (IOException e) {
            /* Expected with local */
        }
    }

    @Test
    public void markdownIsRecognised() {
        DownloadResultPage downloadResultPage =
                new DownloadResultPage("", Collections.emptyMap(), FileType.MARKDOWN);

        new Expectations() {{
            response.setContentType("text/markdown");
            times = 1;
        }};

        try {
            downloadResultPage.writeTo(response);
        } catch (IOException e) {
            /* So the problem is with pandoc, nothing else */
            assertTrue(e.getMessage().contains("pandoc"));
        }
    }

    private File getMarkDownFile(DownloadResultPage downloadResultPage) {
        Method method;

        try {
            method = downloadResultPage.getClass().getDeclaredMethod("writeMarkDownFile", null);
            method.setAccessible(true);
            File file = (File) method.invoke(downloadResultPage);
            method.setAccessible(false);
            return file;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new AssertionError("Couldn't access private method.");
        }
    }

    @Test
    public void writeMarkDownFileCreatesAMarkDownFile() {
        DownloadResultPage downloadResultPage =
                new DownloadResultPage("", Collections.emptyMap(), FileType.MARKDOWN);

        assertTrue(getMarkDownFile(downloadResultPage).getName().contains(".md"));
    }

    private String fileAsString(File file) {
        try {
            BufferedReader reader = Files.newBufferedReader(file.toPath(), Charset.defaultCharset());

            StringBuilder stringBuilder = new StringBuilder();
            reader.lines().forEach(c -> stringBuilder.append(c + '\n'));
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("Couldn't convert file to string");
        }
    }

    @Test
    public void emptyAnswerImpliesNoResultCouldBeFoundMD() {
        DownloadResultPage downloadResultPage =
                new DownloadResultPage("", Collections.emptyMap(), FileType.MARKDOWN);

        assertTrue(fileAsString(getMarkDownFile(downloadResultPage)).contains("Sorry"));
    }

    @Test
    public void markdownContainsSingleQueryAnswerPair() {
        Map<String, String> queryAnswerPairs = new HashMap<>();
        queryAnswerPairs.put("query", "answer");

        DownloadResultPage downloadResultPage =
                new DownloadResultPage("test", queryAnswerPairs, FileType.MARKDOWN);

        assertTrue(fileAsString(getMarkDownFile(downloadResultPage)).contains("answer"));
    }

    @Test
    public void markdownContainsMultipleQueryAnswerPairs() {
        Map<String, String> queryAnswerPairs = new HashMap<>();
        int testCount = 10;

        for (int i = 0; i < testCount; i++) {
            queryAnswerPairs.put("query" + i, "answer" + i);
        }

        DownloadResultPage downloadResultPage =
                new DownloadResultPage("test", queryAnswerPairs, FileType.MARKDOWN);

        for (int i = 0; i < testCount; i++) {
            assertTrue(fileAsString(getMarkDownFile(downloadResultPage)).contains("answer" + i));
        }
    }

    @Test
    public void markDownFileIsAddedToOutputStream() throws IOException {
        List<Integer> testList = new LinkedList<>();

        ServletOutputStream testStream = new ServletOutputStream() {
            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setWriteListener(WriteListener writeListener) {
            }

            @Override
            public void write(int b) throws IOException {
                testList.add(b);
            }
        };

        new Expectations() {{
            response.getOutputStream();
            result = testStream;
        }};

        DownloadResultPage downloadResultPage =
                new DownloadResultPage("", Collections.emptyMap(), FileType.MARKDOWN);

        downloadResultPage.writeTo(response);

        StringBuilder resultBuilder = new StringBuilder();
        testList.stream().map(n -> (char) (int) n).collect(Collectors.toList()).forEach(resultBuilder::append);
        String result = resultBuilder.toString();

        String expected = fileAsString(getMarkDownFile(downloadResultPage));

        assertEquals(expected, result);

        new Verifications() {{
            response.setContentType("text/markdown");
            times = 1;
            response.setHeader("Content-Disposition", anyString);
            times = 1;
        }};
    }
}
