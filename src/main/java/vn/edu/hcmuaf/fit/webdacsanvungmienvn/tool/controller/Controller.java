package vn.edu.hcmuaf.fit.webdacsanvungmienvn.tool.controller;

import vn.edu.hcmuaf.fit.webdacsanvungmienvn.tool.model.DSA;
import vn.edu.hcmuaf.fit.webdacsanvungmienvn.tool.view.View;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class Controller {
    private View view;
    private final DSA dsa = new DSA();

    public Controller(View view) {
        this.view = view;

        addButtonAction();
    }

    private void addButtonAction() {
        view.getBtnLoadPriKey().addActionListener(e -> {
            try {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(view);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedKeyFile = fileChooser.getSelectedFile();
                    String keyContent = new String(Files.readAllBytes(selectedKeyFile.toPath())).trim();
                    view.getPrivateKey().setText(keyContent);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
        view.getBtnLoadPubKey().addActionListener(e -> {
            try {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(view);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File selectedKeyFile = fileChooser.getSelectedFile();
                    String keyContent = new String(Files.readAllBytes(selectedKeyFile.toPath())).trim();
                    view.getPublicKey().setText(keyContent);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
        view.getBtnGenKey().addActionListener(e -> {
            try {
                dsa.genKey();
                String priKey = Base64.getEncoder().encodeToString(dsa.getPrivateKey().getEncoded());
                String pubKey = Base64.getEncoder().encodeToString(dsa.getPublicKey().getEncoded());
                view.getPrivateKey().setText(priKey);
                view.getPublicKey().setText(pubKey);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
        view.getBtnSavePriKey().addActionListener(e -> {
            try {
                dsa.savePrivateKey();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
        view.getBtnSavePubKey().addActionListener(e -> {
            try {
                dsa.savePublicKey();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
        view.getBtnSign().addActionListener(e -> {
            try {
                String privateKeyBase64 = view.getPrivateKey().getText().trim();
                String input = view.getInput().getText().trim();

                if (privateKeyBase64.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Private key không được để trống!");
                    return;
                }
                if (input.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Nội dung cần ký không được để trống!");
                    return;
                }

                byte[] keyBytes = Base64.getDecoder().decode(privateKeyBase64);
                PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
                KeyFactory keyFactory = KeyFactory.getInstance("DSA");
                PrivateKey privateKey = keyFactory.generatePrivate(spec);

                DSA dsa = new DSA(privateKey);
                String output = dsa.sign(input);

                view.getOutput().setText(output);
                JOptionPane.showMessageDialog(null, "Ký thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });
        view.getBtnCopy().addActionListener(e -> {
            view.getOutput().selectAll();
            view.getOutput().copy();
            JOptionPane.showMessageDialog(null, "Đã sao chép chữ ký vào bộ nhớ tạm!");
        });
    }
}