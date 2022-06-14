import java.awt.BorderLayout
import java.awt.Color
import java.awt.event.ActionEvent
import javax.swing.*


class MainScreen {
    private val  generateButton:JButton = JButton()
    val panel1:JPanel = JPanel()
    val frame = JFrame()
init {

        frame.setBounds(100, 100, 730, 489);
        frame.setLayout(BorderLayout())
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        val testLabel = JLabel("")
    testLabel.setBounds(65, 68, 46, 14)
        testLabel.isOpaque = true
        testLabel.background = Color.RED
        testLabel.foreground = Color.BLACK
        frame.contentPane.add(testLabel)
        frame.isVisible = true
    //val lblPhone = JLabel("Phone #")
    //lblPhone.setBounds(65, 68, 46, 14)
    //frame.contentPane.add(lblPhone)

    val textField_1 = JTextField()
    textField_1.setBounds(128, 65, 86, 20)
    frame.contentPane.add(textField_1)
    textField_1.setColumns(10)

    val lblEmailId = JLabel("Email Id")
    lblEmailId.setBounds(65, 115, 46, 14)
    frame.contentPane.add(lblEmailId)

    val textField_2 = JTextField()
    textField_2.setBounds(128, 112, 247, 17)
    frame.contentPane.add(textField_2)
    textField_2.setColumns(10)

    val lblAddress = JLabel("Address")
    lblAddress.setBounds(65, 162, 46, 14)
    frame.contentPane.add(lblAddress)

    val textArea_1 = JTextArea()
    textArea_1.setBounds(126, 157, 212, 40)
    frame.contentPane.add(textArea_1)


    val btnClear = JButton("Clear")

    btnClear.setBounds(312, 387, 89, 23)
    frame.contentPane.add(btnClear)

    val lblSex = JLabel("Sex")
    lblSex.setBounds(65, 228, 46, 14)
    frame.contentPane.add(lblSex)

    val lblMale = JLabel("Male")
    lblMale.setBounds(128, 228, 46, 14)
    frame.contentPane.add(lblMale)

    val lblFemale = JLabel("Female")
    lblFemale.setBounds(292, 228, 46, 14)
    frame.contentPane.add(lblFemale)

    val radioButton = JRadioButton("")
    radioButton.setBounds(337, 224, 109, 23)
    frame.contentPane.add(radioButton)

    val radioButton_1 = JRadioButton("")
    radioButton_1.setBounds(162, 224, 109, 23)
    frame.contentPane.add(radioButton_1)

    val lblOccupation = JLabel("Occupation")
    lblOccupation.setBounds(65, 288, 67, 14)
    frame.contentPane.add(lblOccupation)



    val btnSubmit = JButton("submit")

    btnSubmit.background = Color.BLUE
    btnSubmit.foreground = Color.MAGENTA
    btnSubmit.setBounds(65, 387, 89, 23)
    frame.contentPane.add(btnSubmit)

}

}
