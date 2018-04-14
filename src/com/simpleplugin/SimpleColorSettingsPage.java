package com.simpleplugin;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class SimpleColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("When(", SimpleSyntaxHighlighter.WHEN_KEYWORD),
            new AttributesDescriptor("And(", SimpleSyntaxHighlighter.AND_KEYWORD),
            new AttributesDescriptor("Then(", SimpleSyntaxHighlighter.THEN_KEYWORD),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return null;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new SimpleSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return "public MyStepdefs() {\n" +
                "        Given(\"^Test started$\", () -> {\n" +
                "            System.out.println(\"Test is going to start\");\n" +
                "\n" +
                "        });\n" +
                "        When(\"^Test is going to finish$\", () -> {\n" +
                "            System.out.println(\"Test is going to finish\");\n" +
                "\n" +
                "        });\n" +
                "        Then(\"^After test report is generated$\", () -> {\n" +
                "            System.out.println(\"Report is going to be generated\");\n" +
                "        });\n" +
                "        And(\"^This step will fail$\", () -> {\n" +
                "            assertion();\n" +
                "        });\n" +
                "        And(\"^Failing at (.+)$\", (Integer att) -> {\n" +
                "            Assert.fail(\"Failing at \" + att + \" time\");\n" +
                "        });\n" +
                "    }";
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @NotNull
    @Override
    public AttributesDescriptor[] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @NotNull
    @Override
    public ColorDescriptor[] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "JAVA";
    }
}