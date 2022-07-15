package com.tui.proof.dto;

public class UserInfoJwtWrapper {

    private final UserInfoResponse userInfoResponse;
    private final String jwt;

    public UserInfoJwtWrapper(UserInfoResponse userInfoResponse, String jwt) {
        this.userInfoResponse = userInfoResponse;
        this.jwt = jwt;
    }

    public UserInfoResponse getUserInfoResponse() {
        return userInfoResponse;
    }

    public String getJwt() {
        return jwt;
    }
}
