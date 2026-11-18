package vn.edu.hcmuaf.fit.webdacsanvungmienvn.model;

import java.util.Date;

public class User {
    private int id;
    private String email;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String gender;
    private Date dateOfBirth;
    private String role;
    private String provider;
    private String googleId;
    private boolean isVerified;

    public User() {
    }

    public User(int id, String email, String password, String fullName, String phoneNumber, String gender, Date dateOfBirth, String role, String provider, String googleId, boolean isVerified) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.provider = provider;
        this.googleId = googleId;
        this.isVerified = isVerified;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getRole() {
        return role;
    }

    public String getProvider() {
        return provider;
    }

    public String getGoogleId() {
        return googleId;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
