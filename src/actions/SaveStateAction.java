package actions;

import Utils.SharedData;
import Utils.UrlValidator;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class SaveStateAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        String url = Messages.showInputDialog(project, "Set WebPage", "Enter valid URL",
                Messages.getQuestionIcon(), SharedData.getUrl(), new UrlValidator());

        String urlToCheck;
        if (!url.startsWith("http")) {
            urlToCheck = "http://" + url;
        } else {
            urlToCheck = url;
        }

        SharedData.setUrl(urlToCheck);
    }
}
