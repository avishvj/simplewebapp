package ic.doc;

import java.util.HashMap;
import java.util.Map;

public class QueryProcessor {

    public  Map<String, String> process(String query) {
        Map<String, String> results = new HashMap<>();

        if (query.toLowerCase().contains("shakespeare")) {
            results.put("Shakespeare", "William Shakespeare (26 April 1564 - 23 April 1616) was an\n"
                            + "English poet, playwright, and actor, widely regarded as the greatest\n"
                            + "writer in the English language and the world's pre-eminent dramatist. \n");
        }

        if (query.toLowerCase().contains("asimov")) {
            results.put("Asimov", "Isaac Asimov (2 January 1920 - 6 April 1992) was an\n"
                    + "American writer and professor of Biochemistry, famous for\n"
                    + "his works of hard science fiction and popular science. \n");
        }

        if (query.toLowerCase().contains("kohdai")) {
            results.put("Kohdai", "Kohdai (24 March 1999 - Present) is a \n"
                    + "Japanese NFL fan that supports the Philadelphia Eagles.\n" );
        }

        if (query.toLowerCase().contains("andy")) {
            results.put("Andy", "Andy Murray is a Scottish tennis player and hero to all British people.\n");
        }

        if (query.toLowerCase().contains("david")) {
            results.put("David", "David Davies born July 20th 1999, not to be confused with \n"
                    + "the former brexit minister, is a Devonshire farmer \n");
        }

        if (query.toLowerCase().contains("ali")) {
            results.put("Ali", "Ali Colver (8th April 1999 - Present) is a Scottish football fan \n"
                    + "that follows the most successful team in world football - Glasgow Rangers.\n");
        }

        if (results.isEmpty()) {
            results.put(query, "");
        }

        return results;
    }
}
