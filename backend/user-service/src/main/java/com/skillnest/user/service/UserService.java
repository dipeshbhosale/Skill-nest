package com.skillnest.user.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.user.model.StudentProfile;
import com.skillnest.user.model.TeacherProfile;
import com.skillnest.user.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private final Firestore firestore;

    private static final String USERS_COLLECTION = "users";
    private static final String TEACHER_PROFILES_COLLECTION = "teacher_profiles";
    private static final String STUDENT_PROFILES_COLLECTION = "student_profiles";

    public UserService(Firestore firestore) {
        this.firestore = firestore;
    }

    // ==================== User CRUD ====================

    public List<User> getAllUsers() throws ExecutionException, InterruptedException {
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
        DocumentSnapshot doc = firestore.collection(TEACHER_PROFILES_COLLECTION).document(userId).get().get();
        if (!doc.exists()) {
            return null;
        }
        return TeacherProfile.fromMap(doc.getData());
    }

    public TeacherProfile updateTeacherProfile(String userId, TeacherProfile profile)
            throws ExecutionException, InterruptedException {
        profile.setUserId(userId);
        firestore.collection(TEACHER_PROFILES_COLLECTION).document(userId).set(profile.toMap()).get();
        return getTeacherProfile(userId);
    }

    // ==================== Student Profile ====================

    public StudentProfile getStudentProfile(String userId) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = firestore.collection(STUDENT_PROFILES_COLLECTION).document(userId).get().get();
        if (!doc.exists()) {
            return null;
        }
        return StudentProfile.fromMap(doc.getData());
    }

    public StudentProfile updateStudentProfile(String userId, StudentProfile profile)
            throws ExecutionException, InterruptedException {
        profile.setUserId(userId);
        firestore.collection(STUDENT_PROFILES_COLLECTION).document(userId).set(profile.toMap()).get();
        return getStudentProfile(userId);
    }
}
