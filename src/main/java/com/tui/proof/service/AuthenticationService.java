package com.tui.proof.service;

import com.tui.proof.config.security.jwt.JwtUtils;
import com.tui.proof.config.security.services.UserDetailsImpl;
import com.tui.proof.dto.LoginRequest;
import com.tui.proof.dto.SignupRequest;
import com.tui.proof.dto.UserInfoJwtWrapper;
import com.tui.proof.dto.UserInfoResponse;
import com.tui.proof.exception.UniquenessValidationException;
import com.tui.proof.model.Role;
import com.tui.proof.model.RoleEnum;
import com.tui.proof.model.User;
import com.tui.proof.repository.RoleRepository;
import com.tui.proof.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ClientService clientService;

    public AuthenticationService(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                                 JwtUtils jwtUtils, UserRepository userRepository, RoleRepository roleRepository,
                                 ClientService clientService) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.clientService = clientService;
    }

    public UserInfoJwtWrapper authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        UserInfoResponse userInfoResponse = new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
        return new UserInfoJwtWrapper(userInfoResponse, jwtCookie.toString());
    }

    public String registerUser(SignupRequest signupRequest) {
        validateUniquenessOfUsernameAndEmail(signupRequest);
        User user = new User(signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();
        if (CollectionUtils.isEmpty(strRoles)) {
            Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new EntityNotFoundException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if (RoleEnum.ROLE_ADMIN.name().equals(role)) {
                    Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                            .orElseThrow(() -> new EntityNotFoundException("Error: Role is not found."));
                    roles.add(adminRole);
                } else if (RoleEnum.ROLE_USER.name().equals(role)) {
                    Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                            .orElseThrow(() -> new EntityNotFoundException("Error: Role is not found."));
                    roles.add(userRole);
                }
            });
        }
        user.setRoles(roles);
        userRepository.save(user);
        if (!StringUtils.isEmpty(signupRequest.getName()) && !StringUtils.isEmpty(signupRequest.getSurname())) {
            clientService.createNewClient(user, signupRequest.getName(), signupRequest.getSurname(), signupRequest.getPhone());
            return "Client registered successfully!";
        }
        return "User registered successfully!";
    }

    public UserInfoJwtWrapper logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return new UserInfoJwtWrapper(null, cookie.toString());
    }

    private void validateUniquenessOfUsernameAndEmail(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new UniquenessValidationException("Username is already taken!");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new UniquenessValidationException("Email is already taken!");
        }
    }
}
