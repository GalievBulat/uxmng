package view;// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;

public class LoadExisting {

  private JButton refreshToolWindowButton;
  private JButton hideToolWindowButton;
  private JLabel currentDate;
  private JLabel currentTime;
  private JLabel timeZone;
  private JPanel myToolWindowContent;
  private JTextField colorArea01TextField;
  private JButton addButton;

  public LoadExisting(ToolWindow toolWindow) {
    hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
    //refreshToolWindowButton.addActionListener(e -> currentDateTime());

  }


  public JPanel getContent() {
    return myToolWindowContent;
  }

}
