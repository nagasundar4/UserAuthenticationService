package com.example.userauthenticationservice.controllers;

import com.example.userauthenticationservice.dtos.*;
import com.example.userauthenticationservice.exceptions.UserAlreadyExistsException;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.services.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    //Signup
    //Login
    //ForgetPassword
    //Logout
    //...
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignupRequestDto signupRequestDto) {
        try {
            User user = authService.signup(signupRequestDto.getEmail(), signupRequestDto.getPassword());
            if (user == null) {
                throw new UserAlreadyExistsException("Please try out with different email");
            }

            return new ResponseEntity<>(from(user), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<String> forgetPassword(@RequestBody ForgetPasswordDto forgetPasswordDto) {
        return null;
    }

    private UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody LoginRequestDto loginRequestDto) {
        User user = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        if (user == null) {
            throw new RuntimeException("BAD CREDENTIALS");
        }

        return from(user);
    }

    public ResponseEntity<Boolean> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        return null;
    }
}
