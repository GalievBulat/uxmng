package view;// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import com.bgaliev.occult_color_scheme.presenter.ToolbarPresenter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class LayoutListValidation {

  private JLabel description;
  private JPanel myToolWindowContent;
  private JPanel colorPalettes;
  private JButton changePaletteButton;
  private JButton refreshLayoutsButton;
  private ToolbarPresenter navigator;


  public LayoutListValidation(ToolbarPresenter navigator) {
    this.navigator = navigator;
    refreshLayoutsButton.addActionListener((e)-> {
      navigator.navigateToScreen(new LayoutListValidation(navigator).getContent());
    });
    changePaletteButton.addActionListener((e)-> {
      navigator.navigateToScreen(new LoadVariants(navigator).getContent());
    });
    colorPalettes.add(loadingPanel());
    ApplicationManager.getApplication().invokeLater(()->{
      colorPalettes.removeAll();
      //GridLayout experimentLayout = new GridLayout(navigator.getPalettesCount(),0);
      //colorPalettes.setLayout(experimentLayout);
      List<CompletableFuture<BufferedImage>> layouts = navigator.loadLayoutPreview();
      try {
        showLayouts(layouts);
      } catch (ExecutionException | InterruptedException e) {
        throw new RuntimeException(e);
      }
      colorPalettes.updateUI();
    });
  }
  private void showLayouts(List<CompletableFuture<BufferedImage>> layouts) throws ExecutionException, InterruptedException {
    GridBagConstraints cn =new GridBagConstraints();
    JBScrollPane j =  new JBScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    JPanel panel = new JPanel();
    GridLayout experimentLayout = new GridLayout(layouts.size(),0);
    panel.setLayout(experimentLayout);
    GridBagConstraints c =new GridBagConstraints();
    c.insets.set(10,0,10,0);
    c.gridx = 0;
    c.gridy = 0;
    for (CompletableFuture<BufferedImage> render: layouts) {
      //j.add(getLayout(render), c);
      JLabel imageView = getImage(render);
      panel.add(imageView, c);
      ToolbarPresenter.ImagePalette palette = navigator.getPaletteByImage(render.get(), navigator.getCurPalette().getAreas().size());
      boolean palCheck  = navigator.comparePalettes(3, 0.1, palette, navigator.getCurPalette());

      panel.add(new JLabel(palCheck ? "fits palette" : "doesn't fit palette"), c);
      imageView.addMouseListener(new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
          imageView.setBorder(BorderFactory.createEtchedBorder());
          imageView.updateUI();
          try {
            navigator.navigateToScreen(new LayoutDetails(navigator, render.get()).getContent());
          } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException(ex);
          }

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
      c.gridy++;
    }
    j.setViewportView(panel);
    colorPalettes.add(j, cn.gridx++);

  }

  public JLabel getImage(CompletableFuture<BufferedImage> image) throws ExecutionException, InterruptedException {
    BufferedImage layout = image.get();
    ImageIcon icon = new ImageIcon(layout.getScaledInstance(-1, 100, Image.SCALE_DEFAULT));
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
