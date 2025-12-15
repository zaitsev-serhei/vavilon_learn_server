package com.language_practice_server.server_demo.user_service.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfoResponse {
    private String subject;
    private String fullName;
    private String email;
    private String picture;
    @JsonProperty("email_verified")
    private boolean emailVerified;

    public UserInfoResponse(String subject, String fullName, String email, String picture, boolean emailVerified) {
        this.subject = subject;
        this.fullName = fullName;
        this.email = email;
        this.picture = picture;
        this.emailVerified = emailVerified;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
}
