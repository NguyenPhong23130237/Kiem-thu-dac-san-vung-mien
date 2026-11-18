package vn.edu.hcmuaf.fit.webdacsanvungmienvn.tool.model;

import java.security.PrivateKey;
import java.security.Signature;
import java.util.Base64;

public class RSA {
    private PrivateKey privateKey;
    private Signature signature;

    public RSA(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String sign(String text) throws Exception {
        signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(text.getBytes());
        byte[] signatureBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);
    }
}
