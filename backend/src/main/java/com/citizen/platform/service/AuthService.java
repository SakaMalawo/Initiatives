package com.citizen.platform.service;

import com.citizen.platform.dto.*;
import com.citizen.platform.entity.Role;
import com.citizen.platform.entity.User;
import com.citizen.platform.repository.RoleRepository;
import com.citizen.platform.repository.UserRepository;
import com.citizen.platform.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhone())
                .city(request.getCity())
                .postalCode(request.getPostalCode())
                .isActive(true)
                .build();

        Role citizenRole = roleRepository.findByName("CITOYEN")
                .orElseThrow(() -> new RuntimeException("Rôle CITOYEN non trouvé"));
        user.setRoles(Set.of(citizenRole));

        User savedUser = userRepository.save(user);

        String token = jwtTokenProvider.generateToken(savedUser);

        return AuthResponse.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .email(savedUser.getEmail())
                .roles(savedUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .token(token)
                .build();
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

        return AuthResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .token(token)
                .build();
    }

    public UserProfile getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return UserProfile.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .city(user.getCity())
                .postalCode(user.getPostalCode())
                .roles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()))
                .active(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .build();
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
