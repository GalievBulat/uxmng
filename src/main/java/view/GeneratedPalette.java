package view;// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import com.bgaliev.occult_color_scheme.core.ImageProcessing;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

public class GeneratedPalette {

  private JLabel description;
  private JPanel myToolWindowContent;
  private JButton activeColorButton;
  private JPanel colorPalettes;
  private JButton backButton;
  private final ViewNavigator navigator;

  public GeneratedPalette(ViewNavigator navigator) {
    backButton.addActionListener(e-> {
      navigator.navigateToScreen(new LoadVariants(navigator).getContent());
            });
    activeColorButton.addActionListener(e-> {
      if (navigator.getCurPalette() != null)
        navigator.navigateToScreen(new LayoutListValidation(navigator).getContent());
      else {
        navigator.showMessage("No Palette Selected", "Please select Palette");
      }
    });

    colorPalettes.add(loadingPanel());
    this.navigator = navigator;
    ApplicationManager.getApplication().invokeLater(()->{
      colorPalettes.removeAll();
      GridLayout experimentLayout = new GridLayout(navigator.getPalettesCount(),0);
      colorPalettes.setLayout(experimentLayout);
      try{
         generatePalettes(navigator.getImagePalettes());
      } catch ( IllegalArgumentException e){
        navigator.showMessage("Error ", "this image doesn't meet the requirements, try using another one");
        navigator.navigateToScreen(new LayoutListValidation(navigator).getContent());
      }
      colorPalettes.updateUI();
    });
  }
  private void generatePalettes(List<ViewNavigator.ImagePalette> palettes){

    GridBagConstraints cn =new GridBagConstraints();
    for (ViewNavigator.ImagePalette palette : palettes) {
      JPanel j = new JPanel();
      //j.setPreferredSize(new Dimension(300,25));
      GridBagConstraints c =new GridBagConstraints();
      c.insets.set(0,0,0,0);
      c.gridx = 0;
      c.gridy = 0;
      List<ImageProcessing.RGBA> colors = palette.getColors();
      List<Double> areas = palette.getAreas();
      for (int i = 0; i < colors.size(); i++) {
        j.add(getColor(areas.get(i), colors.get(i), palette), c);
        c.gridy++;
      }
      j.addMouseListener(new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
          j.setBorder(BorderFactory.createEtchedBorder());
          j.updateUI();
          navigator.setCurPalette(palette);

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
      });

      colorPalettes.add(j, cn.gridx++);
      colorPalettes.add(new JLabel(navigator.getAssoc(palette)), cn.gridx++);

    }
  }
  public JLabel getColor(double val, ImageProcessing.RGBA rgb, ViewNavigator.ImagePalette palette){
    JLabel testLabel = new JLabel(String.format("%,.2f", val*100) + "%");

    testLabel.setBorder(JBUI.Borders.empty(5));
    testLabel.setFont(new Font(testLabel.getFont().getName(), Font.PLAIN, 10));
    //testLabel.setPreferredSize(new Dimension((int) (val*300),25));
    testLabel.setOpaque(true);
    testLabel.setBackground(new Color(rgb.getR(), rgb.getG(), rgb.getB()));

    return testLabel;
  }


  public JPanel getContent() {
    return myToolWindowContent;
  }
  private JPanel loadingPanel() {
    JPanel panel = new JPanel();
    BoxLayout layoutMgr = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
    panel.setLayout(layoutMgr);

    ClassLoader cldr = this.getClass().getClassLoader();
    java.net.URL imageURL   = cldr.getResource("resources\\loading.gif");
    ImageIcon imageIcon = null;
    if (imageURL != null) {
      imageIcon = new ImageIcon(imageURL);
    }
    JLabel iconLabel = new JLabel();
    iconLabel.setIcon(imageIcon);
    if (imageIcon != null)
      imageIcon.setImageObserver(iconLabel);

    JLabel label = new JLabel("Loading...");
    panel.add(iconLabel);
    panel.add(label);
    return panel;
  }

}
