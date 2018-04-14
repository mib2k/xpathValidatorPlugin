package actions;

import Utils.DriverUtils;
import Utils.XpathDetector;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.JBColor;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EditorAction extends AnAction {
    Project project;

    @Override
    public void update(AnActionEvent e) {
        //Get required data keys
        project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        //Set visibility only in case of existing project and editor
        e.getPresentation().setEnabled(project != null && editor != null);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        Arrays.stream(editor.getMarkupModel().getAllHighlighters()).forEach(RangeMarker::dispose);
        Document document = editor.getDocument();
        String url = Messages.showInputDialog(project, "Enter valid URL", "Enter valid URL", Messages.getQuestionIcon());
        String urlToCheck = null;
        try {
            assert url != null;
            if (!url.startsWith("http")) {
                urlToCheck = "http://" + url;
            } else urlToCheck = url;

            new URL(urlToCheck);
        } catch (MalformedURLException e1) {
            Messages.showMessageDialog(project, String.format("URL %s is invalid", urlToCheck), "Warning",
                    Messages.getWarningIcon());
            return;
        }

        final String verifiedUrl = urlToCheck;
        List<String> xpathCollection = XpathDetector.detectXpathExpressions(document.getText());

        TextAttributes myAttr = new TextAttributes(JBColor.BLACK, JBColor.PINK, null, null, 5);
        Runnable r = () -> {
            try {
                validateExpressions(xpathCollection, verifiedUrl).forEach(xpression -> highlight(editor, document, myAttr, xpression));
            } catch (Exception ex) {
                Messages.showMessageDialog(project, String.format("Unable to open URL %s", verifiedUrl), "Warning",
                        Messages.getWarningIcon());
            }
        };
        r.run();

    }

    private List<String> validateExpressions(List<String> xpathCollection, String urlToCheck) {
        HtmlPage page;
        try {
            page = DriverUtils.getHtmlUnitClient().getPage(urlToCheck);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        HtmlPage finalPage = page;
        return xpathCollection.stream().filter(xpath -> {
            List<?> elements = finalPage.getByXPath(xpath);
            return elements.size() == 0;
        }).collect(Collectors.toList());


    }

    private void highlight(Editor editor, Document document, TextAttributes myAttr, String xpression) {
        int startOffset = 0;
        while (startOffset > -1) {
            int fromIndex = document.getCharsSequence().toString().indexOf(xpression, startOffset);
            if (fromIndex < 0) {
                startOffset = fromIndex;
                continue;
            }
            int toIndex = fromIndex + xpression.length();
            startOffset = toIndex;
            editor.getMarkupModel().addRangeHighlighter(fromIndex, toIndex, HighlighterLayer.ERROR, myAttr, HighlighterTargetArea.EXACT_RANGE);
        }
    }
}