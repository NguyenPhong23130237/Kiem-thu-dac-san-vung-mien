package vn.edu.hcmuaf.fit.webdacsanvungmienvn.algorithm;


import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA {
    KeyPair keyPair;
    PrivateKey privateKey;
    PublicKey publicKey;

    public String getPublicKeyBase64(){
        if (publicKey == null) return null;
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public String getPrivateKeyBase64(){
        if (privateKey == null) return null;
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    public String encryptBase64(String data) throws Exception {
        return Base64.getEncoder().encodeToString(encrypt(data));
    }

    private byte[] encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte in[] = data.getBytes(StandardCharsets.UTF_8);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] out = cipher.doFinal(in);
        return out;
    }

    public String deCrypt(String base64) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        byte in[] = Base64.getDecoder().decode(base64);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] out = cipher.doFinal(in);
        return new String(out, StandardCharsets.UTF_8);
    }

    public void genKey() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        keyPair = generator.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
    }
    public void loadPrivateKey(String key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec pkcs8 = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        privateKey = kf.generatePrivate(pkcs8);
    }

    public void loadPublicKey(String key) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        publicKey = kf.generatePublic(x509);
    }

    public boolean verify(String hashOrder, String signatureBase64) throws Exception {
        if (publicKey == null) return false;
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(hashOrder.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64.trim());
        return sig.verify(signatureBytes);
    }
}
