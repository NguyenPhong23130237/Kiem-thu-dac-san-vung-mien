package vn.edu.hcmuaf.fit.webdacsanvungmienvn.model;

import java.util.Date;

public class GenKey {
    private int id;
    private int userId;
    private String publicKey;
    private String algorithm;
    private Date createdAt;
    private Date revokedAt;
    private String status;
    public GenKey() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getPublicKey() {
        return publicKey;
    }
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    public String getAlgorithm() {
        return algorithm;
    }
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Date getRevokedAt() {
        return revokedAt;
    }
    public void setRevokedAt(Date revokedAt) {
        this.revokedAt = revokedAt;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}