// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import javafx.scene.Scene;
import org.jetbrains.annotations.NotNull;
import view.GeneratedPalette;
import view.StartScreen;
import view.ViewNavigator;

import javax.swing.plaf.PanelUI;

public class PaletteToolWindowFactory implements ToolWindowFactory {

  /**
   * Create the tool window content.
   *
   * @param project    current project
   * @param toolWindow current tool window
   */
  public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

    ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
    ViewNavigator navigator = new ViewNavigator(toolWindow, contentFactory, project);
    StartScreen startScreen = new StartScreen(navigator);
    navigator.navigateToScreen(startScreen.getContent());
  }

}
