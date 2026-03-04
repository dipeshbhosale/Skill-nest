package com.skillnest.user.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.user.model.StudentProfile;
import com.skillnest.user.model.TeacherProfile;
import com.skillnest.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired(required = false)
    private Firestore firestore;

    private static final String USERS_COLLECTION = "users";
    private static final String TEACHER_PROFILES_COLLECTION = "teacher_profiles";
    private static final String STUDENT_PROFILES_COLLECTION = "student_profiles";

    // In-memory stores for demo mode
    private final Map<String, User> demoUsers = new ConcurrentHashMap<>();
    private final Map<String, TeacherProfile> demoTeacherProfiles = new ConcurrentHashMap<>();
    private final Map<String, StudentProfile> demoStudentProfiles = new ConcurrentHashMap<>();

    private boolean isDemoMode() {
        return firestore == null;
    }

    // ==================== User CRUD ====================

    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: returning demo users");
            return new ArrayList<>(demoUsers.values());
        }
        ApiFuture<QuerySnapshot> future = firestore.collection(USERS_COLLECTION).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<User> users = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            User user = User.fromMap(doc.getData());
            user.setId(doc.getId());
            // Don't expose password in list
            user.setPassword(null);
            users.add(user);
        }
        return users;
    }

    public User getUserById(String userId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting user {}", userId);
            return demoUsers.get(userId);
        }
        DocumentSnapshot doc = firestore.collection(USERS_COLLECTION).document(userId).get().get();
        if (!doc.exists()) {
            return null;
        }
        User user = User.fromMap(doc.getData());
        user.setId(doc.getId());
        user.setPassword(null); // Don't expose password
        return user;
    }

    public User updateUser(String userId, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: updating user {}", userId);
            User user = demoUsers.get(userId);
            if (user != null) {
                if (updates.containsKey("name")) user.setName((String) updates.get("name"));
                if (updates.containsKey("phone")) user.setPhone((String) updates.get("phone"));
                if (updates.containsKey("status")) user.setStatus((String) updates.get("status"));
                user.setUpdatedAt(System.currentTimeMillis());
                demoUsers.put(userId, user);
            }
            return user;
        }
        DocumentReference docRef = firestore.collection(USERS_COLLECTION).document(userId);
        DocumentSnapshot doc = docRef.get().get();
        if (!doc.exists()) {
            return null;
        }

        // Add updatedAt timestamp
        updates.put("updatedAt", System.currentTimeMillis());

        // Prevent updating sensitive fields directly
        updates.remove("password");
        updates.remove("id");

        docRef.update(updates).get();

        return getUserById(userId);
    }

    public boolean deleteUser(String userId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: deleting user {}", userId);
            return demoUsers.remove(userId) != null;
        }
        DocumentReference docRef = firestore.collection(USERS_COLLECTION).document(userId);
        DocumentSnapshot doc = docRef.get().get();
        if (!doc.exists()) {
            return false;
        }

        docRef.delete().get();

        // Also delete associated profiles
        firestore.collection(TEACHER_PROFILES_COLLECTION).document(userId).delete();
        firestore.collection(STUDENT_PROFILES_COLLECTION).document(userId).delete();

        return true;
    }

    // ==================== Teacher Profile ====================

    public TeacherProfile getTeacherProfile(String userId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting teacher profile for {}", userId);
            return demoTeacherProfiles.get(userId);
        }
        DocumentSnapshot doc = firestore.collection(TEACHER_PROFILES_COLLECTION).document(userId).get().get();
        if (!doc.exists()) {
            return null;
        }
        return TeacherProfile.fromMap(doc.getData());
    }

    public TeacherProfile updateTeacherProfile(String userId, TeacherProfile profile)
            throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: updating teacher profile for {}", userId);
            profile.setUserId(userId);
            demoTeacherProfiles.put(userId, profile);
            return profile;
        }
        profile.setUserId(userId);
        firestore.collection(TEACHER_PROFILES_COLLECTION).document(userId).set(profile.toMap()).get();
        return getTeacherProfile(userId);
    }

    // ==================== Student Profile ====================

    public StudentProfile getStudentProfile(String userId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting student profile for {}", userId);
            return demoStudentProfiles.get(userId);
        }
        DocumentSnapshot doc = firestore.collection(STUDENT_PROFILES_COLLECTION).document(userId).get().get();
        if (!doc.exists()) {
            return null;
        }
        return StudentProfile.fromMap(doc.getData());
    }

    public StudentProfile updateStudentProfile(String userId, StudentProfile profile)
            throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: updating student profile for {}", userId);
            profile.setUserId(userId);
            demoStudentProfiles.put(userId, profile);
            return profile;
        }
        profile.setUserId(userId);
        firestore.collection(STUDENT_PROFILES_COLLECTION).document(userId).set(profile.toMap()).get();
        return getStudentProfile(userId);
    }
}
