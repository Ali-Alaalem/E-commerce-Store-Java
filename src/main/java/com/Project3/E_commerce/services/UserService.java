package com.Project3.E_commerce.services;


import com.Project3.E_commerce.exceptions.InformationExistException;
import com.Project3.E_commerce.models.Role;
import com.Project3.E_commerce.models.User;
import com.Project3.E_commerce.models.request.LoginRequest;
import com.Project3.E_commerce.models.response.LoginResponse;
import com.Project3.E_commerce.repositorys.RoleRepository;
import com.Project3.E_commerce.repositorys.UserRepository;
import com.Project3.E_commerce.security.JWTUtils;
import com.Project3.E_commerce.security.MyUserDetails;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private MyUserDetails myUserDetails;
    private RoleRepository roleRepository;


    public UserService(RoleRepository roleRepository,UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder,
                       JWTUtils jwtUtils, @Lazy AuthenticationManager authenticationManager, @Lazy MyUserDetails myUserDetails){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtUtils=jwtUtils;
        this.authenticationManager=authenticationManager;
        this.myUserDetails=myUserDetails;
        this.roleRepository=roleRepository;

    }

    public User createUser(User objectUser){
        if(!userRepository.existsByEmail(objectUser.getEmail())){
objectUser.setPassword(passwordEncoder.encode(objectUser.getPassword()));
            Optional<Role> role=roleRepository.findByName("Customer");
            objectUser.setRole(role.get());
            return userRepository.save(objectUser);
        }else{
            throw new InformationExistException("User with email address " +objectUser.getEmail() + "already exist");
        }
    }

    public User findUserByEmail(String email){
        return userRepository.findUserByEmail(email);
    }

    public ResponseEntity<?> loginUser(LoginRequest loginRequest){

        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword());
        try {
            Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                    loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            myUserDetails=(MyUserDetails) authentication.getPrincipal();
            final String JWT =jwtUtils.generateJwtToken(myUserDetails);
            return ResponseEntity.ok(new LoginResponse(JWT));
        }catch (Exception e){
            return ResponseEntity.ok(new LoginResponse("Error :User name of password is incorrect"));
        }

    }
}
