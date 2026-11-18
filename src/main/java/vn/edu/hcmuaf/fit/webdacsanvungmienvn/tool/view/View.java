package vn.edu.hcmuaf.fit.webdacsanvungmienvn.tool.view;

import javax.swing.*;
import java.awt.*;

public class View extends JFrame {
    private JTextField privateKey, publicKey;
    private JTextArea input,  output;
    private JButton btnLoadPriKey, btnSavePriKey, btnLoadPubKey, btnSavePubKey, btnGenKey, btnSign, btnCopy;

    public View(){
        setTitle("Ứng dụng ký chữ ký điện tử");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        JPanel privateKeyPanel = new JPanel(new FlowLayout());
        privateKey = new JTextField(20);
        btnLoadPriKey = new JButton("Load Private Key");
        topPanel.add(new JLabel("Giải thuật: DSA"));
        privateKeyPanel.add(new JLabel("Private key:"));
        privateKeyPanel.add(privateKey);
        privateKeyPanel.add(btnLoadPriKey);

        JPanel publicKeyPanel = new JPanel(new FlowLayout());
        publicKey = new JTextField(20);
        btnLoadPubKey = new JButton("Load Public Key");
        publicKeyPanel.add(new JLabel("Public key:"));
        publicKeyPanel.add(publicKey);
        publicKeyPanel.add(btnLoadPubKey);

        JPanel btnPanel = new JPanel(new FlowLayout());
        btnGenKey = new JButton("Gen Key");
        btnSavePriKey = new JButton("Lưu Private Key");
        btnSavePubKey = new JButton("Lưu Public Key");
        btnPanel.add(btnGenKey);
        btnPanel.add(btnSavePubKey);
        btnPanel.add(btnSavePriKey);

        topPanel.add(privateKeyPanel);
        topPanel.add(publicKeyPanel);
        topPanel.add(btnPanel);

        JPanel centerPanel = new JPanel(new GridLayout(2,1,5,5));
        input  = new JTextArea();
        output  = new JTextArea();
        output.setEditable(false);
        JScrollPane inputPane = new JScrollPane(input);
        inputPane.setBorder(BorderFactory.createTitledBorder("Nhập nội dung"));
        JScrollPane outputPane = new JScrollPane(output);
        outputPane.setBorder(BorderFactory.createTitledBorder("Chữ ký"));
        centerPanel.add(inputPane);
        centerPanel.add(outputPane);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        btnSign = new JButton("Ký");
        btnCopy = new JButton("Sao chép");
        bottomPanel.add(btnSign);
        bottomPanel.add(btnCopy);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }


    public JTextField getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(JTextField privateKey) {
        this.privateKey = privateKey;
    }

    public JTextArea getInput() {
        return input;
    }

    public void setInput(JTextArea input) {
        this.input = input;
    }

    public JTextArea getOutput() {
        return output;
    }

    public void setOutput(JTextArea output) {
        this.output = output;
    }

    public JButton getBtnLoadPriKey() {
        return btnLoadPriKey;
    }

    public void setBtnLoadPriKey(JButton btnLoadPriKey) {
        this.btnLoadPriKey = btnLoadPriKey;
    }

    public JButton getBtnSign() {
        return btnSign;
    }

    public void setBtnSign(JButton btnSign) {
        this.btnSign = btnSign;
    }

    public JButton getBtnCopy() {
        return btnCopy;
    }

    public void setBtnCopy(JButton btnCopy) {
        this.btnCopy = btnCopy;
    }

    public JTextField getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(JTextField publicKey) {
        this.publicKey = publicKey;
    }

    public JButton getBtnSavePriKey() {
        return btnSavePriKey;
    }

    public void setBtnSavePriKey(JButton btnSavePriKey) {
        this.btnSavePriKey = btnSavePriKey;
    }

    public JButton getBtnLoadPubKey() {
        return btnLoadPubKey;
    }

    public void setBtnLoadPubKey(JButton btnLoadPubKey) {
        this.btnLoadPubKey = btnLoadPubKey;
    }

    public JButton getBtnSavePubKey() {
        return btnSavePubKey;
    }

    public void setBtnSavePubKey(JButton btnSavePubKey) {
        this.btnSavePubKey = btnSavePubKey;
    }

    public JButton getBtnGenKey() {
        return btnGenKey;
    }

    public void setBtnGenKey(JButton btnGenKey) {
        this.btnGenKey = btnGenKey;
    }
}
