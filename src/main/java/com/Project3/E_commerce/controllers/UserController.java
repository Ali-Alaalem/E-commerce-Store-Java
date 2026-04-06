package com.Project3.E_commerce.controllers;


import com.Project3.E_commerce.models.User;
import com.Project3.E_commerce.models.request.LoginRequest;
import com.Project3.E_commerce.models.request.PasswordChangeRequest;
import com.Project3.E_commerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/auth/users")
public class UserController {
private UserService userService;

@Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User createUser(@RequestBody User objectUser){
        System.out.println("Calling create user");
        return userService.createUser(objectUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        System.out.println("Calling loginUser ==>");
        return userService.loginUser(loginRequest);

    }


    @PostMapping("/password/reset")
    public void resetPasswordEmailSender(@RequestBody User user){
        System.out.println("Calling resetPasswordEmailSender ==>");
        userService.resetPasswordEmailSender(user);
    }

    @GetMapping("/password/reset/page")
    public ResponseEntity<String> resetPasswordPage(@RequestParam("token") String token){
        System.out.println("Calling resetPasswordPage ==>");
        return userService.resetPasswordPage(token);
    }

    @PostMapping("/password/reset/submit")
    public ResponseEntity<String> resetPasswordSubmit(@RequestParam String token, @RequestParam String newPassword) {
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok("<h3>Password reset successfully!</h3>");
    }

    @PutMapping("/change/password")
    public String ChangePassword(Authentication authentication, @RequestBody PasswordChangeRequest request){
        System.out.println("Controller calling ==> ChangePassword()");
        return userService.ChangePassword(authentication,request);
    }

    @PutMapping("/{userId}/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User softDeleteUser(Authentication authentication,@PathVariable("userId") Long userId){
        System.out.println("Controller calling ==> softDeleteUser()");
        return userService.softDeleteUser(authentication,userId);
    }

}
