package view;// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import com.bgaliev.occult_color_scheme.core.ImageProcessing;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class LayoutDetails {

  private JLabel description;
  private JPanel myToolWindowContent;
  private JButton backButton;
  private JPanel colorPalettes;
  private ViewNavigator navigator;
  public JLabel getColor(double val, ImageProcessing.RGBA rgb){
    JLabel testLabel = new JLabel(String.format("%,.2f", val*100) + "%");
    //testLabel.setBorder(JBUI.Borders.empty(5));
    testLabel.setFont(new Font(testLabel.getFont().getName(), Font.PLAIN, 10));

    //testLabel.setPreferredSize(new Dimension((int) (val*300),45));
    testLabel.setOpaque(true);
    testLabel.setBackground(new Color(rgb.getR(), rgb.getG(), rgb.getB()));

    return testLabel;
  }

  private void showPalette(ViewNavigator.ImagePalette palette, GridBagConstraints constraints){

    JPanel j = new JPanel();
    //j.setPreferredSize(new Dimension(300,25));

   //c.insets.set(0,0,0,0);
   //c.gridx = 0;
   //c.gridy = 0;
    List<ImageProcessing.RGBA> colors = palette.getColors();
    List<Double> areas = palette.getAreas();
    for (int i = 0; i < colors.size(); i++) {
      j.add(getColor(areas.get(i), colors.get(i)));
      //c.gridx++;
    }


    colorPalettes.add(j, constraints);

  }

  public LayoutDetails(ViewNavigator navigator, BufferedImage image) {
    this.navigator = navigator;
    backButton.addActionListener((e)-> {
      navigator.navigateToScreen(new LayoutListValidation(navigator).getContent());
    });
    ViewNavigator.ImagePalette palette = navigator.getPaletteByImage(image, 10);
    ApplicationManager.getApplication().invokeLater(()->{
      colorPalettes.add(loadingPanel());
      this.navigator = navigator;
      ApplicationManager.getApplication().invokeLater(()->{
        colorPalettes.removeAll();
        GridBagConstraints cn =new GridBagConstraints();
        cn.gridx = 0;
        cn.gridy = 0;
        colorPalettes.add(getLayout(image), cn);
        cn.gridy++;
        GridBagLayout experimentLayout = new GridBagLayout();
        colorPalettes.setLayout(experimentLayout);
        showPalette(palette, cn);
        cn.gridy++;
        showPalette(navigator.getCurPalette(), cn);
        cn.gridy++;
        colorPalettes.updateUI();
      });

      colorPalettes.updateUI();
    });
  }


  public JLabel getLayout(BufferedImage image) {

    ImageIcon icon = new ImageIcon(image.getScaledInstance(-1, 300, Image.SCALE_REPLICATE));
    //BufferedImage resizedImage = new BufferedImage(100, 100, Image.SCALE_FAST);
    //Graphics2D g = resizedImage.createGraphics();
    //g.drawImage(layout, 0, 0, 100, 100, null);
    //g.dispose();
    //ImageIcon icon = new ImageIcon(resizedImage);
    return new JLabel(icon);
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
