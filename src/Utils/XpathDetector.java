package Utils;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    public static List<String> validateExpressions(List<String> xpathCollection, HtmlPage htmlPage) {
        return xpathCollection.stream().filter(xpath -> {
            List<?> elements = htmlPage.getByXPath(xpath);
            return elements.size() == 0;
        }).collect(Collectors.toList());
    }
}
