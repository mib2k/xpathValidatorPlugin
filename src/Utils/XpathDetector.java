package Utils;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XpathDetector {
    private static final String xpathPattern = "(\"\\(*//.*?\")";

    public static List<String> detectXpathExpressions(String document) {
        Pattern r = Pattern.compile(xpathPattern);
        Matcher matcher = r.matcher(document);
        ArrayList<String> result = new ArrayList<>();
        while (matcher.find()) {

            int startIndex = matcher.start();
            int endIndex = matcher.end();
            result.add(document.substring(startIndex, endIndex).replace("\"", ""));
        }
        return result;
    }

    public static void validateExpressions(List<String> xpathCollection, HtmlPage htmlPage, List<String> broken, List<String> error) {
        xpathCollection.forEach(xpath -> {
            try {
                List<?> elements = htmlPage.getByXPath(xpath);
                if (elements.isEmpty()) {
                    broken.add(xpath);
                }
            } catch (Exception ex) {
                error.add(xpath);
            }
        });
    }
}
