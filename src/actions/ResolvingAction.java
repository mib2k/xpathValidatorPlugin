package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiFile;
import com.intellij.ui.JBColor;

import java.util.Arrays;

public class ResolvingAction extends AnAction {
    public ResolvingAction() {
        super("Resolving_Action");
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        String txt = Messages.showInputDialog(project, "Some text", "Some title", Messages.getQuestionIcon());
        Messages.showMessageDialog(project, txt, "Info", Messages.getInformationIcon());


        final Editor editor = event.getData(CommonDataKeys.EDITOR);

        PsiFile psiFile = event.getRequiredData(CommonDataKeys.PSI_FILE);


        Arrays.stream(editor.getMarkupModel().getAllHighlighters()).forEach(RangeMarker::dispose);

        Document document = editor.getDocument();
        int lineCount = document.getLineCount();


        //editor.getMarkupModel().addLineHighlighter()


        TextAttributesKey key = TextAttributesKey.createTextAttributesKey("When", DefaultLanguageHighlighterColors.KEYWORD);
        EditorColorsScheme colorsScheme = editor.getColorsScheme();
        editor.getColorsScheme().setAttributes(key, new TextAttributes());
        TextAttributes myAttr = new TextAttributes(JBColor.GREEN, JBColor.RED, JBColor.BLACK, EffectType.BOLD_DOTTED_LINE, 5);
        colorsScheme.setAttributes(key, myAttr);
    }
}
