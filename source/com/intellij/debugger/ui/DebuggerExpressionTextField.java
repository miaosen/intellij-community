package com.intellij.debugger.ui;

import com.intellij.debugger.engine.evaluation.TextWithImports;
import com.intellij.debugger.engine.evaluation.TextWithImportsImpl;
import com.intellij.debugger.engine.evaluation.EvaluationManagerImpl;
import com.intellij.debugger.engine.evaluation.EvaluationManager;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.ui.EditorTextField;

import javax.swing.*;
import java.awt.*;

public class DebuggerExpressionTextField extends DebuggerEditorImpl {
  private final EditorTextField myEditor;
  private final JTextField myStubField = new JTextField();
  private final JPanel myMainPanel = new JPanel(new CardLayout());
  private static final String EDITOR = "editor";
  private static final String STUB = "stub";

  public DebuggerExpressionTextField(Project project, PsiElement context, final String recentsId) {
    super(project, context, recentsId);
    myStubField.setEnabled(false);
    myEditor = new EditorTextField("", project, StdFileTypes.JAVA);
    setLayout(new BorderLayout());
    myMainPanel.add(myStubField, STUB);
    myMainPanel.add(myEditor, EDITOR);
    add(myMainPanel, BorderLayout.CENTER);
    ((CardLayout)myMainPanel.getLayout()).show(myMainPanel, isEnabled()? EDITOR : STUB);
    setText(EvaluationManager.getInstance().getEmptyExpressionFragment());
  }

  public JComponent getPreferredFocusedComponent() {
    return myEditor.getEditor().getContentComponent();
  }

  public TextWithImportsImpl getText() {
    return createItem(myEditor.getDocument(), getProject());
  }

  public void setText(TextWithImports text) {
    myEditor.setDocument(createDocument((TextWithImportsImpl)text));
  }

  public TextWithImportsImpl createText(String text, String importsString) {
    return new TextWithImportsImpl(EvaluationManagerImpl.EXPRESSION_FACTORY, text, importsString);
  }

  public void setEnabled(boolean enabled) {
    if (isEnabled() != enabled) {
      super.setEnabled(enabled);
      final TextWithImportsImpl text = getText();
      myStubField.setText(text.getText());
      ((CardLayout)myMainPanel.getLayout()).show(myMainPanel, enabled? EDITOR : STUB);
    }
  }
}
