package com.skillnest.auth.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.auth.dto.AuthResponse;
import com.skillnest.auth.dto.LoginRequest;
import com.skillnest.auth.dto.RegisterRequest;
import com.skillnest.auth.model.User;
import com.skillnest.auth.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class AuthService {

    private final Firestore firestore;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String USERS_COLLECTION = "users";
    private static final String TEACHER_PROFILES_COLLECTION = "teacher_profiles";
    private static final String STUDENT_PROFILES_COLLECTION = "student_profiles";

    public AuthService(Firestore firestore, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.firestore = firestore;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Register a new user
     * As per ER Design: Creates User + TeacherProfile or StudentProfile
     */
    public AuthResponse register(RegisterRequest request) throws ExecutionException, InterruptedException {
        // Check if email already exists
        QuerySnapshot existingUsers = firestore.collection(USERS_COLLECTION)
                .whereEqualTo("email", request.getEmail())
                .get().get();

        if (!existingUsers.isEmpty()) {
            throw new RuntimeException("Email already registered");
        }

        // Create user with hashed password (BCrypt as per Security Measures)
        String userId = UUID.randomUUID().toString();
        User user = new User(
                userId,
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole()
        );

        // Save to Firebase
        firestore.collection(USERS_COLLECTION).document(userId).set(user.toMap()).get();

        // Create role-specific profile
        if ("teacher".equals(request.getRole())) {
            Map<String, Object> teacherProfile = Map.of(
                    "userId", userId,
                    "qualifications", "",
                    "bio", "",
                    "perClassRate", 0,
                    "createdAt", System.currentTimeMillis()
            );
            firestore.collection(TEACHER_PROFILES_COLLECTION).document(userId).set(teacherProfile).get();
        } else if ("student".equals(request.getRole())) {
            Map<String, Object> studentProfile = Map.of(
                    "userId", userId,
                    "enrolledCourses", List.of(),
                    "createdAt", System.currentTimeMillis()
            );
            firestore.collection(STUDENT_PROFILES_COLLECTION).document(userId).set(studentProfile).get();
        }

        // Generate JWT token with payload: user_id, role, email
        String token = jwtTokenProvider.generateToken(userId, request.getEmail(), request.getRole());

        Map<String, Object> userResponse = Map.of(
                "id", userId,
                "name", request.getName(),
                "email", request.getEmail(),
                "role", request.getRole()
        );

        return new AuthResponse(token, userResponse, "Registration successful");
    }

    /**
     * Login Flow as per 4.2:
     * 1. User submits email and password
     * 2. Backend verifies credentials
     * 3. If valid, generate JWT token
     * 4. Token returned to client
     */
    public AuthResponse login(LoginRequest request) throws ExecutionException, InterruptedException {
        // Find user by email
        QuerySnapshot querySnapshot = firestore.collection(USERS_COLLECTION)
                .whereEqualTo("email", request.getEmail())
                .get().get();

        if (querySnapshot.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }

        DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
        User user = User.fromMap(doc.getData());

        // Verify password with BCrypt
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Check if user is active
        if (!"active".equals(user.getStatus())) {
            throw new RuntimeException("Account is deactivated");
        }

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());

        Map<String, Object> userResponse = Map.of(
                "id", user.getId(),
                "name", user.getName(),
                "email", user.getEmail(),
                "role", user.getRole()
        );

        return new AuthResponse(token, userResponse, "Login successful");
    }

    /**
     * Validate an existing JWT token
     */
    public Map<String, Object> validateToken(String token) {
        if (jwtTokenProvider.validateToken(token)) {
            String userId = jwtTokenProvider.getUserIdFromToken(token);
            String email = jwtTokenProvider.getEmailFromToken(token);
            String role = jwtTokenProvider.getRoleFromToken(token);
            return Map.of(
                    "valid", true,
                    "userId", userId,
                    "email", email,
                    "role", role
            );
        }
        throw new RuntimeException("Invalid token");
    }
}
