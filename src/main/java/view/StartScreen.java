package view;// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.util.Calendar;

public class StartScreen {

  private JButton nextPageButton;
  private JButton hideToolWindowButton;
  private JLabel currentDate;
  private JLabel currentTime;
  private JLabel timeZone;
  private JPanel myToolWindowContent;
  private JComboBox<String> comboBox1;

  public StartScreen(ViewNavigator navigator) {
    nextPageButton.addActionListener(e -> navigator.navigateToScreen(new LoadVariants(navigator).getContent()));
    comboBox1.addItem("ddd");
    comboBox1.addItem("ddd");
  }

  public JPanel getContent() {
    return myToolWindowContent;
  }

}
