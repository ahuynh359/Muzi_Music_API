package com.ahuynh.muzi_music_api.controller;

import com.ahuynh.muzi_music_api.email.OnRegistrationCompleteEvent;
import com.ahuynh.muzi_music_api.exception.CustomException;
import com.ahuynh.muzi_music_api.model.User;
import com.ahuynh.muzi_music_api.model.VerificationToken;
import com.ahuynh.muzi_music_api.model.role.Role;
import com.ahuynh.muzi_music_api.model.role.RoleName;
import com.ahuynh.muzi_music_api.payload.request.LoginRequest;
import com.ahuynh.muzi_music_api.payload.request.SignUpRequest;
import com.ahuynh.muzi_music_api.payload.response.ApiResponse;
import com.ahuynh.muzi_music_api.payload.response.MessageErrorResponse;
import com.ahuynh.muzi_music_api.repository.RoleRepository;
import com.ahuynh.muzi_music_api.repository.UserRepository;
import com.ahuynh.muzi_music_api.repository.VerificationTokenRepository;
import com.ahuynh.muzi_music_api.security.JwtTokenProvider;
import com.ahuynh.muzi_music_api.service.UserService;
import com.ahuynh.muzi_music_api.service.VerificationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping()
    public String hello() {
        return "Hello World";
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest, final HttpServletRequest request) {
        //Kiểm tra tính hợp lệ dữ liê
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageErrorResponse("Email already in use"));
        }

        if (userService.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageErrorResponse("Username already in use"));
        }

        //Lưu user vào db
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        List<Role> roles = new ArrayList<>();
        if (userService.countUser() == 0) {
            roles.add(roleRepository.findByName(RoleName.ROLE_ADMIN)
                    .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "There is no role in db")));
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "There is no role in db")));

        } else {
            roles.add(roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "There is no role in db")));
        }
        User user = new User(signUpRequest.getEmail(), encodedPassword, signUpRequest.getUsername(), "", false, roles);




        User result = userService.save(user);
        //Trả về location
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{userId}")
                .buildAndExpand(result.getId())
                .toUri();
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, getCurrentUrl(request)));
        return ResponseEntity.created(location).body(new ApiResponse(HttpStatus.CREATED, "Success, please check your email"));
    }

    @GetMapping("/verifyEmail/{token}")
    public ResponseEntity<?> verifyEmail(@PathVariable("token") String token) {
        VerificationToken verificationToken = verificationTokenService.findByToken(token);
        if(verificationToken == null){
            return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST, "Invalid verification token"));
        }
        if (verificationToken.getUser().isEnabled()) {
            return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST, "This account has already been verified"));

        }
        String verificationResult = verificationTokenService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("Valid")) {
            verificationTokenService.deleteToken(verificationToken);
            return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK, "Verified successfully"));

        }
        return ResponseEntity.badRequest().body(new ApiResponse(HttpStatus.BAD_REQUEST, "Invalid verification token"));
    }

    public String getCurrentUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @PostMapping("/login")
    public ResponseEntity<?>  signIn(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserNameOrEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok().body(new ApiResponse(HttpStatus.OK,jwt));
    }


}
