package com.skillnest.auth.service;

import com.google.cloud.firestore.*;
import com.skillnest.auth.dto.AuthResponse;
import com.skillnest.auth.dto.LoginRequest;
import com.skillnest.auth.dto.RegisterRequest;
import com.skillnest.auth.model.User;
import com.skillnest.auth.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired(required = false)
    private Firestore firestore;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final String USERS_COLLECTION = "users";
    private static final String TEACHER_PROFILES_COLLECTION = "teacher_profiles";
    private static final String STUDENT_PROFILES_COLLECTION = "student_profiles";

    // In-memory store for demo mode
    private final Map<String, Map<String, Object>> demoUsers = new ConcurrentHashMap<>();

    // Initialize demo users
    {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Admin
        Map<String, Object> admin = new HashMap<>();
        admin.put("id", "demo-admin-001");
        admin.put("name", "Admin User");
        admin.put("email", "admin@skillnest.com");
        admin.put("password", encoder.encode("admin123"));
        admin.put("role", "admin");
        admin.put("status", "active");
        admin.put("phone", "");
        admin.put("createdAt", System.currentTimeMillis());
        admin.put("updatedAt", System.currentTimeMillis());
        demoUsers.put("admin@skillnest.com", admin);

        // Teacher
        Map<String, Object> teacher = new HashMap<>();
        teacher.put("id", "demo-teacher-001");
        teacher.put("name", "Prof. Smith");
        teacher.put("email", "teacher@skillnest.com");
        teacher.put("password", encoder.encode("teacher123"));
        teacher.put("role", "teacher");
        teacher.put("status", "active");
        teacher.put("phone", "");
        teacher.put("createdAt", System.currentTimeMillis());
        teacher.put("updatedAt", System.currentTimeMillis());
        demoUsers.put("teacher@skillnest.com", teacher);

        // Student
        Map<String, Object> student = new HashMap<>();
        student.put("id", "demo-student-001");
        student.put("name", "John Doe");
        student.put("email", "student@skillnest.com");
        student.put("password", encoder.encode("student123"));
        student.put("role", "student");
        student.put("status", "active");
        student.put("phone", "");
        student.put("createdAt", System.currentTimeMillis());
        student.put("updatedAt", System.currentTimeMillis());
        demoUsers.put("student@skillnest.com", student);
    }

    /**
     * Register a new user
     * As per ER Design: Creates User + TeacherProfile or StudentProfile
     */
    public AuthResponse register(RegisterRequest request) throws ExecutionException, InterruptedException {
        if (firestore != null) {
            // Firebase mode
            QuerySnapshot existingUsers = firestore.collection(USERS_COLLECTION)
                    .whereEqualTo("email", request.getEmail())
                    .get().get();

            if (!existingUsers.isEmpty()) {
                throw new RuntimeException("Email already registered");
            }

            String userId = UUID.randomUUID().toString();
            User user = new User(
                    userId,
                    request.getName(),
                    request.getEmail(),
                    passwordEncoder.encode(request.getPassword()),
                    request.getRole()
            );

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

            String token = jwtTokenProvider.generateToken(userId, request.getEmail(), request.getRole());

            Map<String, Object> userResponse = Map.of(
                    "id", userId,
                    "name", request.getName(),
                    "email", request.getEmail(),
                    "role", request.getRole()
            );

            return new AuthResponse(token, userResponse, "Registration successful");
        } else {
            // Demo mode
            logger.info("Demo mode: Registering user {}", request.getEmail());
            if (demoUsers.containsKey(request.getEmail())) {
                throw new RuntimeException("Email already registered");
            }

            String userId = "demo-" + UUID.randomUUID().toString().substring(0, 8);
            Map<String, Object> userData = new HashMap<>();
            userData.put("id", userId);
            userData.put("name", request.getName());
            userData.put("email", request.getEmail());
            userData.put("password", passwordEncoder.encode(request.getPassword()));
            userData.put("role", request.getRole());
            userData.put("status", "active");
            userData.put("phone", "");
            userData.put("createdAt", System.currentTimeMillis());
            userData.put("updatedAt", System.currentTimeMillis());
            demoUsers.put(request.getEmail(), userData);

            String token = jwtTokenProvider.generateToken(userId, request.getEmail(), request.getRole());

            Map<String, Object> userResponse = new HashMap<>();
            userResponse.put("id", userId);
            userResponse.put("name", request.getName());
            userResponse.put("email", request.getEmail());
            userResponse.put("role", request.getRole());

            return new AuthResponse(token, userResponse, "Registration successful (demo mode)");
        }
    }

    /**
     * Login Flow:
     * 1. User submits email and password
     * 2. Backend verifies credentials
     * 3. If valid, generate JWT token
     * 4. Token returned to client
     */
    public AuthResponse login(LoginRequest request) throws ExecutionException, InterruptedException {
        if (firestore != null) {
            // Firebase mode
            QuerySnapshot querySnapshot = firestore.collection(USERS_COLLECTION)
                    .whereEqualTo("email", request.getEmail())
                    .get().get();

            if (querySnapshot.isEmpty()) {
                throw new RuntimeException("Invalid credentials");
            }

            DocumentSnapshot doc = querySnapshot.getDocuments().get(0);
            User user = User.fromMap(doc.getData());

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid credentials");
            }

            if (!"active".equals(user.getStatus())) {
                throw new RuntimeException("Account is deactivated");
            }

            String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail(), user.getRole());

            Map<String, Object> userResponse = Map.of(
                    "id", user.getId(),
                    "name", user.getName(),
                    "email", user.getEmail(),
                    "role", user.getRole()
            );

            return new AuthResponse(token, userResponse, "Login successful");
        } else {
            // Demo mode
            logger.info("Demo mode: Login attempt for {}", request.getEmail());
            Map<String, Object> userData = demoUsers.get(request.getEmail());
            if (userData == null) {
                throw new RuntimeException("Invalid credentials");
            }

            if (!passwordEncoder.matches(request.getPassword(), (String) userData.get("password"))) {
                throw new RuntimeException("Invalid credentials");
            }

            String userId = (String) userData.get("id");
            String email = (String) userData.get("email");
            String role = (String) userData.get("role");
            String name = (String) userData.get("name");
            String token = jwtTokenProvider.generateToken(userId, email, role);

            Map<String, Object> userResponse = new HashMap<>();
            userResponse.put("id", userId);
            userResponse.put("name", name);
            userResponse.put("email", email);
            userResponse.put("role", role);

            return new AuthResponse(token, userResponse, "Login successful (demo mode)");
        }
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
