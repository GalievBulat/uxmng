package view;// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import javax.swing.*;

public class LoadVariants {

  private JButton byPhotoButton;
  private JButton byExistingButton;
  private JLabel description;
  private JPanel myToolWindowContent;
  private JButton byFilterButton;

  public LoadVariants(ViewNavigator navigator) {
    byExistingButton.addActionListener(e -> navigator.getToolWindow().hide(null));
    byPhotoButton.addActionListener(e -> navigator.navigateToScreen(
            new LoadPhoto(navigator).getContent()));

  }

  public JPanel getContent() {
    return myToolWindowContent;
  }

}
