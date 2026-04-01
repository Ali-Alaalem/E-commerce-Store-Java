package com.Project3.E_commerce.services;


import com.Project3.E_commerce.exceptions.InformationExistException;
import com.Project3.E_commerce.models.Role;
import com.Project3.E_commerce.models.User;
import com.Project3.E_commerce.models.VerificationToken;
import com.Project3.E_commerce.models.request.LoginRequest;
import com.Project3.E_commerce.models.response.LoginResponse;
import com.Project3.E_commerce.repositorys.RoleRepository;
import com.Project3.E_commerce.repositorys.UserRepository;
import com.Project3.E_commerce.repositorys.VerificationTokenRepository;
import com.Project3.E_commerce.security.JWTUtils;
import com.Project3.E_commerce.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private MyUserDetails myUserDetails;
    private RoleRepository roleRepository;


    private TokenService tokenService;
    private final VerificationTokenRepository verificationTokenRepository;
    @Value("${sender.email}")
    private String senderEmail;

    public UserService( VerificationTokenRepository verificationTokenRepository, TokenService tokenService, RoleRepository roleRepository, UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder,
                       JWTUtils jwtUtils, @Lazy AuthenticationManager authenticationManager, @Lazy MyUserDetails myUserDetails, JavaMailSender mailSender1, VerificationTokenRepository verificationTokenRepository1){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtils=jwtUtils;
        this.authenticationManager=authenticationManager;
        this.myUserDetails=myUserDetails;
        this.roleRepository=roleRepository;
        this.tokenService=tokenService;
        this.verificationTokenRepository=verificationTokenRepository;
    }


    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User createUser(User objectUser){
        if(!userRepository.existsByEmail(objectUser.getEmail())){
objectUser.setPassword(passwordEncoder.encode(objectUser.getPassword()));
            Optional<Role> role=roleRepository.findByName("Customer");
            objectUser.setRole(role.get());
            objectUser.setIsVerified(false);
            User user=userRepository.save(objectUser);


            VerificationToken verificationToken = new VerificationToken();
            verificationToken.setUser(user);
            verificationToken.setToken(UUID.randomUUID().toString());
            verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));

            verificationTokenRepository.save(verificationToken);
            tokenService.sendMail(user.getEmail(), verificationToken.getToken());

            return user;
        }else{
            throw new InformationExistException("User with email address " +objectUser.getEmail() + "already exist");
        }
    }


    public ResponseEntity<?> loginUser(LoginRequest loginRequest){

        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword());
        try {
            Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                    loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            myUserDetails=(MyUserDetails) authentication.getPrincipal();
            if(myUserDetails.getUser().getIsVerified()){
            final String JWT =jwtUtils.generateJwtToken(myUserDetails);
            return ResponseEntity.ok(new LoginResponse(JWT));
            }else{
                return ResponseEntity.ok(new LoginResponse("Your Account is not verified or deleted"));
            }
        }catch (Exception e){
            return ResponseEntity.ok(new LoginResponse("Error :User name of password is incorrect"));
        }

    }

}
