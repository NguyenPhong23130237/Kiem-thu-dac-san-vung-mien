package vn.edu.hcmuaf.fit.webdacsanvungmienvn.tool.model;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DSA {
    private PrivateKey privateKey;
    private Signature signature;
    private KeyPairGenerator generator;
    private SecureRandom random;
    private PublicKey publicKey;
    private KeyPair keyPair;

    public DSA(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public DSA(){}

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public Signature getSignature() {
        return signature;
    }

    public void setSignature(Signature signature) {
        this.signature = signature;
    }

    public KeyPairGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(KeyPairGenerator generator) {
        this.generator = generator;
    }

    public SecureRandom getRandom() {
        return random;
    }

    public void setRandom(SecureRandom random) {
        this.random = random;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public String sign(String text) throws Exception {
        signature = Signature.getInstance("SHA256withDSA", "SUN");
        signature.initSign(privateKey);
        signature.update(text.getBytes());
        byte[] signatureBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    public void genKey() throws Exception {
        generator = KeyPairGenerator.getInstance("DSA");
        random = SecureRandom.getInstance("SHA1PRNG");
        generator.initialize(2048, random);
        keyPair = generator.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }

    public void loadPrivateKey(String key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("DSA");
        privateKey = kf.generatePrivate(pkcs8);
    }

    public void loadPublicKey(String key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("DSA");
        publicKey = kf.generatePublic(x509);
    }

    public void savePrivateKey() throws Exception {
        if (privateKey == null) {
            throw new Exception("Private Key chưa được tạo");
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int res = chooser.showSaveDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            File folder = chooser.getSelectedFile();
            File keyFile = new File(folder, "private_key.pem");
            String pem = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            try (FileWriter fw = new FileWriter(keyFile)) {
                fw.write(pem);
            }
        }
    }

    public void savePublicKey() throws Exception {
        if (publicKey == null) {
            throw new Exception("Public Key chưa được tạo");
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int res = chooser.showSaveDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            File folder = chooser.getSelectedFile();
            File keyFile = new File(folder, "public_key.pem");
            String pem = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            try (FileWriter fw = new FileWriter(keyFile)) {
                fw.write(pem);
            }
        }
    }
}
