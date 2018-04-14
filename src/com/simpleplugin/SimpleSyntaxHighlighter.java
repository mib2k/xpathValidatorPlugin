package com.simpleplugin;

import com.intellij.lang.java.lexer.JavaLexer;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.psi.tree.IElementType;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class SimpleSyntaxHighlighter extends SyntaxHighlighterBase {
    public static final TextAttributesKey WHEN_KEYWORD =
            createTextAttributesKey("When(", new TextAttributes(JBColor.GREEN, JBColor.RED, JBColor.BLACK, EffectType.BOLD_DOTTED_LINE, 5));
    public static final TextAttributesKey THEN_KEYWORD =
            createTextAttributesKey("Then(", new TextAttributes(JBColor.GREEN, JBColor.RED, JBColor.BLACK, EffectType.BOLD_DOTTED_LINE, 5));
    public static final TextAttributesKey AND_KEYWORD =
            createTextAttributesKey("And(", new TextAttributes(JBColor.GREEN, JBColor.RED, JBColor.BLACK, EffectType.BOLD_DOTTED_LINE, 5));

    private static final TextAttributesKey[] GHERKIN = new TextAttributesKey[]{WHEN_KEYWORD, THEN_KEYWORD, AND_KEYWORD};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new JavaLexer(LanguageLevel.JDK_1_7);
    }

    @NotNull
    @Override
    public TextAttributesKey[] getTokenHighlights(IElementType tokenType) {
        if (Arrays.stream(GHERKIN).anyMatch(t -> t.toString().equals(tokenType.toString()))) {
            return GHERKIN;
        }
        if (tokenType.toString().contains("WHITE")) {
            return GHERKIN;
        } else {
            return EMPTY_KEYS;
        }
    }
}