package com.example.userauthenticationservice.controllers;

import com.example.userauthenticationservice.dtos.*;
import com.example.userauthenticationservice.exceptions.InvalidTokenException;
import com.example.userauthenticationservice.exceptions.UserAlreadyExistsException;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.services.IAuthService;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        Pair<User, MultiValueMap<String,String>> userWithHeaders = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        User user = userWithHeaders.a;
        if (user == null) {
            throw new RuntimeException("BAD CREDENTIALS");
        }

//        return from(user);
        return new ResponseEntity<>(from(user),userWithHeaders.b,HttpStatus.OK);
    }

    public ResponseEntity<Boolean> logout(@RequestBody LogoutRequestDto logoutRequestDto) {
        return null;
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto) {
        try {
            System.out.println(validateTokenRequestDto.getToken());
            Boolean response = authService.validateToken(validateTokenRequestDto.getToken(), validateTokenRequestDto.getUserId());
            if (response == false) {
                throw new InvalidTokenException("Either Token is stale or invalid");
            }
            return new ResponseEntity<>(response,HttpStatus.OK);

        }catch(InvalidTokenException exception) {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }
}
