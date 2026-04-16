package com.citizen.platform.service;

import com.citizen.platform.dto.*;
import com.citizen.platform.entity.Role;
import com.citizen.platform.entity.User;
import com.citizen.platform.repository.RoleRepository;
import com.citizen.platform.repository.UserRepository;
import com.citizen.platform.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhone());
        user.setCity(request.getCity());
        user.setPostalCode(request.getPostalCode());
        user.setIsActive(true);

        Role citizenRole = roleRepository.findByName("CITOYEN")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("CITOYEN");
                    return roleRepository.save(newRole);
                });
        user.setRoles(Set.of(citizenRole));

        User savedUser = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(savedUser);

        AuthResponse response = new AuthResponse();
        response.setId(savedUser.getId());
        response.setFirstName(savedUser.getFirstName());
        response.setLastName(savedUser.getLastName());
        response.setEmail(savedUser.getEmail());
        response.setRoles(savedUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        response.setToken(token);
        return response;
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("Compte désactivé");
        }

        String token = jwtTokenProvider.generateToken(user);

        AuthResponse response = new AuthResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        response.setToken(token);
        return response;
    }

    public UserProfile getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        UserProfile profile = new UserProfile();
        profile.setId(user.getId());
        profile.setFirstName(user.getFirstName());
        profile.setLastName(user.getLastName());
        profile.setEmail(user.getEmail());
        profile.setPhoneNumber(user.getPhoneNumber());
        profile.setCity(user.getCity());
        profile.setPostalCode(user.getPostalCode());
        profile.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        profile.setActive(user.getIsActive());
        profile.setCreatedAt(user.getCreatedAt());
        return profile;
    }

    @Transactional
    public UserProfile updateProfile(Long userId, RegisterRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhone());
        user.setCity(request.getCity());
        user.setPostalCode(request.getPostalCode());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        User saved = userRepository.save(user);

        return getProfile(saved.getId());
    }
}
