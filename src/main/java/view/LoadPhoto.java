package view;// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import com.bgaliev.occult_color_scheme.core.ImageProcessing;
import com.bgaliev.occult_color_scheme.presenter.ToolbarPresenter;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.images.fileTypes.impl.ImageFileType;

import javax.swing.*;

public class LoadPhoto {

  private JButton generateButton;
  private JLabel description;
  private JPanel myToolWindowContent;
  private JButton loadPictureButton;
  private JComboBox<String> colorSpacingBox;
  private JComboBox<String> imageQualityBox;
  private JComboBox<String> colorsAreasTypeBox;

  private ToolbarPresenter navigator;

  public LoadPhoto(ToolbarPresenter navigator) {
    this.navigator = navigator;

    colorSpacingBox.addItem("Complementary");
    colorSpacingBox.addItem("Monochromatic");
    colorSpacingBox.addItem("No color spacing");
    imageQualityBox.addItem("Medium");
    imageQualityBox.addItem("High");
    imageQualityBox.addItem("Low");
    colorsAreasTypeBox.addItem("Background + Secondary + Accent + Text");
    colorsAreasTypeBox.addItem("Background + Accent + Text");
    colorsAreasTypeBox.addItem("Background + Secondary + Accent");

    colorSpacingBox.addItemListener(e -> navigator.setHueSpacing(ImageProcessing.HUE_CONTROL.values()[colorSpacingBox.getSelectedIndex()]));
    imageQualityBox.addItemListener(e ->
            navigator.setQuality(ToolbarPresenter.Quality.values()[imageQualityBox.getSelectedIndex()]));
    colorsAreasTypeBox.addItemListener(e ->
            navigator.setAreaType(ToolbarPresenter.AreaType.values()[colorsAreasTypeBox.getSelectedIndex()]));
    generateButton.addActionListener(e -> navigator.navigateToScreen(new GeneratedPalette(navigator).getContent()));
    loadPictureButton.addActionListener(e -> loadPhoto(navigator.getProject()));
  }


  public void loadPhoto(Project project){
    FileChooserDescriptor chooserDescriptor = new FileChooserDescriptor(true,false, false,false, false, false);
    VirtualFile virtualFile = FileChooser.chooseFile(chooserDescriptor, project, null);

    if(virtualFile != null && virtualFile.getFileType() == ImageFileType.INSTANCE
      && (navigator.loadImage(virtualFile.getPath()))) {
      Messages.showMessageDialog(virtualFile.getName(), "File Has Been Loaded", Messages.getInformationIcon());
    } else {
      Messages.showMessageDialog("Your file may have incorrect type", "Error", Messages.getInformationIcon());
    }

  }

  public JPanel getContent() {
    return myToolWindowContent;
  }

}
