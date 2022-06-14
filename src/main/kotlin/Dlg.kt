import javax.swing.*;
import java.awt.event.*;

class Dlg: JDialog() {
    var contentPane: JPanel= JPanel()
    var buttonOK: JButton= JButton()
    var buttonCancel: JButton= JButton()

    init {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        //buttonOK.addActionListener(new ActionListener() {
        //    public void actionPerformed(ActionEvent e) {
        //        onOK();
        //    }
        //});

        //buttonCancel.addActionListener(new ActionListener() {
        //    public void actionPerformed(ActionEvent e) {
        //        onCancel();
        //    }
        //});

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        //addWindowListener(new WindowAdapter() {
        //    public void windowClosing(WindowEvent e) {
        //        onCancel();
        //    }
            //});

        // call onCancel() on ESCAPE
        //contentPane.registerKeyboardAction(new ActionListener() {
        //    public void actionPerformed(ActionEvent e) {
        //        onCancel();
        //    }
        //}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    fun onOK() {
        // add your code here
        dispose();
    }

    fun onCancel() {
        // add your code here if necessary
        dispose();
    }

    fun main() {

    }
}
