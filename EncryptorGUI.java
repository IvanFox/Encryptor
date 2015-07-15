import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Ivan on 03/06/2015.
 */
public class EncryptorGUI extends JFrame implements EncryptorConstants {



    private JPanel mainPanel;
    private JPanel matrixPanel;
    private JTextField[] jTextFields;

    private JTextArea textArea;
    private JScrollPane scrollPane;

    private JButton btnEncrypt,btnDecrypt;

    private JMenuBar menuBar;
    private JMenuItem matrixItem, openFileItem, exitItem;
    private JMenu menu;

    private JFileChooser fileChooser;
    protected boolean isMatrixSet = false;

    private int[] encryptionMatrix = new int[MATRIX_SIZE];
    private EncryptorLogic encryptorLogic = new EncryptorLogic();

    public EncryptorGUI(){
        setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        initMenu();
        initMainPanel();
        initEventHandlers();
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void initMenu(){
        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        matrixItem = new JMenuItem("Set Matrix");
        openFileItem = new JMenuItem("Open File");
        exitItem = new JMenuItem("Exit");
        menu.add(matrixItem); menu.add(openFileItem); menu.add(exitItem);
        menuBar.add(menu);
        menuBar.setVisible(true);
        setResizable(false);
        setJMenuBar(menuBar);
        matrixItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMatrix();
            }
        });
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        openFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initFileChooser();
            }
        });

    }

    private void initMainPanel(){
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        textArea = new JTextArea(10,20);
        textArea.setPreferredSize(new Dimension(FRAME_WIDTH-10, FRAME_WIDTH-100));
        scrollPane = new JScrollPane(textArea);

        btnEncrypt = new JButton("Encrypt");
        btnDecrypt = new JButton("Decrypt");
        btnEncrypt.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
        btnDecrypt.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));

        mainPanel.add(scrollPane); mainPanel.add(btnEncrypt); mainPanel.add(btnDecrypt);
        setContentPane(mainPanel);
    }

    private void initFileChooser(){
        if (fileChooser == null){
            fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Text Files", "txt", "dat", "asm");
            fileChooser.setFileFilter(filter);
        }
        int returnVal = fileChooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    fileChooser.getSelectedFile().getName());
            try{
                textArea.setText(encryptorLogic.openChosenFile(fileChooser.getSelectedFile().getAbsolutePath()));
            }
            catch (IOException e){
                System.out.println(e.fillInStackTrace());
            }
        }
    }

    private void initPopUpMatrixPanel(){
        matrixPanel = new JPanel(new GridLayout(2,2));
        jTextFields = new JTextField[MATRIX_SIZE];

        for (int i = 0; i < jTextFields.length ; i++) {
            jTextFields[i] = new JTextField();
            jTextFields[i].setHorizontalAlignment(SwingConstants.RIGHT);
            jTextFields[i].setPreferredSize(new Dimension(25, 25));
            matrixPanel.add(jTextFields[i]);
        }
    }


    private void displayMatrixSetError(String message){
        JOptionPane.showMessageDialog(this, message, "Matrix error!", JOptionPane.ERROR_MESSAGE);
    }

    private void setMatrix() {
        if(matrixPanel == null){
            initPopUpMatrixPanel();
            displayDialog();
        }else {
            displayDialog();
        }
    }

    private void displayDialog(){
        int result = JOptionPane.showConfirmDialog(null, matrixPanel, "Set the Matrix",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            for (int i = 0; i < jTextFields.length; i++) {
                encryptionMatrix[i] = Integer.parseInt(jTextFields[i].getText());
            }
            if (!encryptorLogic.checkMatrix(encryptionMatrix)){
                displayMatrixSetError("Matrix is not valid\nPlease try again!");
                displayDialog();
                isMatrixSet = false;

            }else{
                isMatrixSet = true;
                System.out.println("Valid");
                System.out.println("Determinant is: " + encryptorLogic.determinant + "\n");
                System.out.println("Inverter is: "  + encryptorLogic.inverseDeterminant);

                for (int i = 0; i < jTextFields.length; i++) {
                    System.out.print(i + ": " + encryptionMatrix[i] + "  ");
                }
            }
        } else {
            System.out.println("Cancelled");
        }
    }

    private void encryptText(){
        if (isMatrixSet){
            System.out.println(textArea.getText());
            if (textArea.getText().length() % 2 == 0)
                textArea.setText(encryptorLogic.performEncryptDecrypt(textArea.getText(), true));
            else{
                textArea.append(" ");
                textArea.setText(encryptorLogic.performEncryptDecrypt(textArea.getText(), true));
            }
            disableBtn();
        }
        else{
            displayMatrixSetError("Matrix is not set!\nPlease set the matrix first!");
            setMatrix();
        }
    }

    private void decryptText(){
        if (isMatrixSet){
            textArea.setText(encryptorLogic.performEncryptDecrypt (textArea.getText(), false));
            disableBtn();
        }
        else{
            displayMatrixSetError("Matrix is not set!\nPlease set the matrix first!");
            setMatrix();
        }
    }

    private void initEventHandlers(){
        btnEncrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                encryptText();

            }
        });
        btnDecrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decryptText();
            }
        });
    }

    private void disableBtn(){
        if(btnEncrypt.isEnabled()){
            btnEncrypt.setEnabled(false);
            btnDecrypt.setEnabled(true);
        } else{
            btnEncrypt.setEnabled(true);
            btnDecrypt.setEnabled(false);
        }
    }
}
