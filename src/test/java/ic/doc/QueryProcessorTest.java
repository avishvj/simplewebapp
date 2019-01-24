package ic.doc;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class QueryProcessorTest {

    QueryProcessor queryProcessor = new QueryProcessor();

    private void testSearch(String search, String result) {
        Map<String, String> searchResult = queryProcessor.process(search);
        assertThat(searchResult.get(search), containsString(result));
    }

    @Test
    public void returnsEmptyStringIfCannotProcessQuery() {
        testSearch("test", "");
    }

    @Test
    public void knowsAboutShakespeare() {
        testSearch("Shakespeare", "playwright");
    }

    @Test
    public void knowsAboutAsimov() {
        testSearch("Asimov", "science fiction");
    }

    @Test
    public void knowsAboutKohdai() {
        testSearch("Kohdai", "Eagles");
    }

    @Test
    public void knowsAboutAndy() {
        testSearch("Andy", "hero");
    }

    @Test
    public void knowsAboutDavid() {
        testSearch("David", "Devonshire");
    }

    @Test
    public void knowsAboutAli() {
        testSearch("Ali", "Rangers");
    }

    @Test
    public void isNotCaseSensitive() {
        String search = "sHaKeSpEaRe";
        String shouldSearch = "Shakespeare";
        String result = "playwright";

        Map<String, String> searchResult = queryProcessor.process(search);
        assertThat(searchResult.get(shouldSearch), containsString(result));
    }

    private void searchTwo(String part1, String part2, String result1, String result2) {
        String search = part1 + part2;

        Map<String, String> searchResult = queryProcessor.process(search);
        assertThat(searchResult.get(part1), containsString(result1));
        assertThat(searchResult.get(part2), containsString(result2));
    }

    @Test
    public void canHaveMultipleResults() {
        searchTwo("Shakespeare", "Asimov", "playwright", "science fiction");
    }

    @Test
    public void allResultsAreUnique() {
        String search = "Shakespeare";
        String search2 = search + search;

        Map<String, String> searchResult = queryProcessor.process(search2);
        assertThat(searchResult.get(search), containsString("playwright"));
        assertThat(searchResult.size(), is(1));
    }
}
