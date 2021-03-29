package com.kmadrugstore.controller;

import com.kmadrugstore.dto.*;
import com.kmadrugstore.entity.Order;
import com.kmadrugstore.entity.User;
import com.kmadrugstore.jwt.JwtTokenUtil;
import com.kmadrugstore.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.HashMap;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final IUserService userService;

    @PreAuthorize("permitAll()")
    @PostMapping("/authenticate")
    public ResponseEntity<HashMap<String, String>> createAuthenticationToken(
            @RequestBody final LoginDTO credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.getUsername(), credentials.getPassword()));
        final User user = userService.findByEmail(credentials.getUsername());
        final String token = jwtTokenUtil.generateToken(user);
        final HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("role", user.getRole().getRole());
        return ok(map);
    }

    @PreAuthorize("permitAll()")
    @PostMapping(value = "/user/register")
    public ResponseEntity register(@RequestBody final RegistrationDTO user) {
        userService.registerAsUser(user);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(), user.getPassword()));
        final User savedUser = userService.findByEmail(user.getEmail());
        final String token = jwtTokenUtil.generateToken(savedUser);
        final HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        map.put("role", savedUser.getRole().getRole());
        return ok(map);
//        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority(T(com.kmadrugstore.utils.Constants)" +
            ".USER_ROLE_STRING)" + "|| hasAuthority(T(com.kmadrugstore.utils.Constants)" +
            ".EMPLOYEE_ROLE_STRING)")
    @GetMapping(value = "/user")
    public ResponseEntity<UserDTO> getUser(
            final Principal principal ) {
        User user = userService.findByEmail(principal.getName());

        return ok(userService.userToUserDTO(user));
    }

    @PostMapping("/user/reset-password")
    public ResponseEntity resetPassword(final HttpServletRequest request,
                                        @RequestBody final String email) {
        userService.resetPassword(email, getAppUrl(request));
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/user/password-reset-token/validate")
    public ResponseEntity<Boolean> validatePasswordResetToken(
            @RequestParam("token") final String token) {
        return ok(userService.passwordResetTokenIsValid(token));
    }

    @PutMapping("/user/change-password")
    public ResponseEntity changePassword(
            @RequestBody final ChangePasswordDTO newPasswordInfo) {
        userService.changePassword(newPasswordInfo);
        return new ResponseEntity((HttpStatus.OK));
    }

    private String getAppUrl(final HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int portNumber = request.getServerPort();
        String contextPath = request.getContextPath();
        String port = "";
        if (portNumber != 80 && portNumber != 443 && portNumber != -1) {
            port = ":" + portNumber;
        }
        return scheme + "://" + serverName + port + contextPath;
    }





}
