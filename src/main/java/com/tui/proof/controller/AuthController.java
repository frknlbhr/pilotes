package com.tui.proof.controller;

import com.tui.proof.dto.LoginRequest;
import com.tui.proof.dto.SignupRequest;
import com.tui.proof.dto.UserInfoJwtWrapper;
import com.tui.proof.dto.UserInfoResponse;
import com.tui.proof.service.AuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signin")
    public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        UserInfoJwtWrapper userInfoJwtWrapper = authenticationService.authenticateUser(loginRequest);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, userInfoJwtWrapper.getJwt())
                .body(userInfoJwtWrapper.getUserInfoResponse());
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok().body(authenticationService.registerUser(signupRequest));
    }

    @PostMapping("/signout")
    public ResponseEntity<String> logoutUser() {
        UserInfoJwtWrapper userInfoJwtWrapper = authenticationService.logoutUser();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, userInfoJwtWrapper.getJwt())
                .body("You've been signed out!");
    }
}
