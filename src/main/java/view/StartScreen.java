package view;// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import com.bgaliev.occult_color_scheme.presenter.ToolbarPresenter;

import javax.swing.*;

public class StartScreen {

  private JButton nextPageButton;
  private JLabel timeZone;
  private JPanel myToolWindowContent;

  public StartScreen(ToolbarPresenter navigator) {
    nextPageButton.addActionListener(e -> navigator.navigateToScreen(new LoadVariants(navigator).getContent()));

  }

  public JPanel getContent() {
    return myToolWindowContent;
  }

}
