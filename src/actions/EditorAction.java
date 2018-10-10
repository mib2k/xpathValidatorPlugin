package actions;

import Utils.DriverUtils;
import Utils.SharedData;
import Utils.XpathDetector;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.markup.HighlighterLayer;
import com.intellij.openapi.editor.markup.HighlighterTargetArea;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class EditorAction extends AnAction {
    private Project project;

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
        if (editor == null) {
            return;
        }

        Document document = editor.getDocument();
        List<String> xpathCollection = XpathDetector.detectXpathExpressions(document.getText());

        final String verifiedUrl = SharedData.getUrl();
        AtomicReference<HtmlPage> htmlPage = new AtomicReference<>();
        Callable r = () -> {
            try {
                htmlPage.set(DriverUtils.openPage(verifiedUrl));
                return "";
            } catch (Exception ex) {
                return String.format("Unable to open URL %s\n%s", verifiedUrl, ex.toString());
            }
        };
        Future future = ApplicationManager.getApplication().executeOnPooledThread(r);

        List<String> broken = new ArrayList<>();
        List<String> error = new ArrayList<>();
        TextAttributes brokenPathAttribute = new TextAttributes(Color.BLACK, new Color(146, 204, 255), null, null, 5);
        TextAttributes errorPathAttribute = new TextAttributes(Color.BLACK, new Color(255, 59, 53), null, null, 5);
        try {
            String result = future.get().toString();
            if (result != null && !result.isEmpty()) {
                Messages.showMessageDialog(project, result, "Warning",
                        Messages.getWarningIcon());
            } else {
                XpathDetector.validateExpressions(xpathCollection, htmlPage.get(), broken, error);
                Arrays.stream(editor.getMarkupModel().getAllHighlighters()).forEach(RangeMarker::dispose);
                broken.forEach(xpression -> highlight(editor, document, brokenPathAttribute, xpression));
                error.forEach(xpression -> highlight(editor, document, errorPathAttribute, xpression));
            }
        } catch (InterruptedException | ExecutionException e1) {
            e1.printStackTrace();
        }
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
            editor.getMarkupModel()
                  .addRangeHighlighter(fromIndex, toIndex, HighlighterLayer.ERROR, myAttr, HighlighterTargetArea.EXACT_RANGE);
        }
    }
}