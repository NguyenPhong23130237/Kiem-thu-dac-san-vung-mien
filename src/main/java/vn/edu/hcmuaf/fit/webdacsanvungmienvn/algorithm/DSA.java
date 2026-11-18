package vn.edu.hcmuaf.fit.webdacsanvungmienvn.algorithm;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DSA {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private KeyPair keyPair;
    private SecureRandom random;
    private KeyPairGenerator generator;
    private Signature signature;

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

    public String sign(String hashOrder) throws Exception{
        signature = Signature.getInstance("SHA256withDSA", "SUN");
        signature.initSign(privateKey);
        signature.update(hashOrder.getBytes());
        byte[] signatureBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }

    public boolean verify(String hashOrder, String signatureBase64) throws Exception {
        if (publicKey == null) return false;
        Signature sig = Signature.getInstance("SHA256withDSA", "SUN");
        sig.initVerify(publicKey);
        sig.update(hashOrder.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64.trim());
        return sig.verify(signatureBytes);
    }

    public String getPublicKeyBase64() {
        if (publicKey == null) return null;
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public String getPrivateKeyBase64() {
        if (privateKey == null) return null;
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    public boolean isValidPublicKey(String key) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(key);
            X509EncodedKeySpec x509 = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("DSA");
            kf.generatePublic(x509);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}