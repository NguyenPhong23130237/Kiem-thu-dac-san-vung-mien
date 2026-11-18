package vn.edu.hcmuaf.fit.webdacsanvungmienvn.model;

import java.util.Date;

public class PasswordResetToken {
    private int id;
    private int userId;
    private String token;
    private Date expiryTime;

    public PasswordResetToken() {
    }

    public PasswordResetToken(int id, int userId, String token, Date expiryTime) {
        this.id = id;
        this.userId = userId;
        this.token = token;
        this.expiryTime = expiryTime;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }
}
