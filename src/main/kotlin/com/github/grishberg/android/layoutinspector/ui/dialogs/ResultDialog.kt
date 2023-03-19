package com.github.grishberg.android.layoutinspector.ui.dialogs

import java.awt.BorderLayout
import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import javax.swing.JDialog
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.WindowConstants


class ResultDialog(
    owner: Frame, res: Float
) : JDialog(owner, false) {

    init {
        val panel = JPanel()
        panel.layout = BorderLayout(4, 4)
        panel.border = BorderFactory.createEmptyBorder(32, 32, 32, 32)

        val label = JLabel("Result: ${(res*100)}% usability index")
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(label, BorderLayout.PAGE_START)
        setContentPane(panel)
        pack()


    }


}