package com.language_practice_server.server_demo.web.dto;

public class AuthResponse {
    private UserDto userInfo;
    private String accessToken;

    public AuthResponse() {
    }

    public AuthResponse(UserDto userInfo, String accessToken) {
        this.userInfo = userInfo;
        this.accessToken = accessToken;
    }

    public UserDto getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserDto userInfo) {
        this.userInfo = userInfo;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
