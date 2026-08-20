package com.rbacaudit.service;

import com.rbacaudit.dto.AuthResponse;
import com.rbacaudit.dto.LoginRequest;
import com.rbacaudit.dto.RegisterRequest;
import com.rbacaudit.model.Role;
import com.rbacaudit.model.User;
import com.rbacaudit.repository.RoleRepository;
import com.rbacaudit.repository.UserRepository;
import com.rbacaudit.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService{
     private final UserRepository userRepository;
     private final RoleRepository roleRepository;
     private final PasswordEncoder passwordEncoder;
     private final AuthenticationManager authenticationManager;
     private final JwtService jwtService;

     public AuthService(
             UserRepository userRepository,
             RoleRepository roleRepository,
             PasswordEncoder passwordEncoder,
             AuthenticationManager authenticationManager,
             JwtService jwtService
     )  {
         this.userRepository = userRepository;
         this.roleRepository = roleRepository;
         this.passwordEncoder = passwordEncoder;
         this.authenticationManager = authenticationManager;
         this.jwtService = jwtService;
     }

     public AuthResponse register(RegisterRequest request) {
         if (userRepository.existsByUsername(request.getUsername())) {
             throw new IllegalArgumentException("Username taken");
         }

         if (userRepository.existsByEmail(request.getEmail())) {
             throw new IllegalArgumentException("E-mail taken");
         }

         String hashed = passwordEncoder.encode(request.getPassword());

         User user = new User(request.getUsername(), request.getEmail(), hashed);

         Role viewerRole = roleRepository.findByName("VIEWER")
                 .orElseThrow(() -> new IllegalStateException("VIEWER role not found - was DataSeeder run?"));
         user.setRoles(Set.of(viewerRole));

         userRepository.save(user);

         return buildAuthResponse(user);
     }

     public AuthResponse login(LoginRequest request) {
         try {
             authenticationManager.authenticate(
                     new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
             );
         } catch (BadCredentialsException e) {
             throw new BadCredentialsException("Invalid username or password");
         }

         User user = userRepository.findByUsername(request.getUsername())
                 .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

         return buildAuthResponse(user);
     }

    private AuthResponse buildAuthResponse(User user) {
        String token = jwtService.generateToken(user.getUsername(), user.getAuthorities().stream().toList());
        Set<String> roleNames = user.getRoleNames();
        return new AuthResponse(token, user.getUsername(), roleNames);
    }

}