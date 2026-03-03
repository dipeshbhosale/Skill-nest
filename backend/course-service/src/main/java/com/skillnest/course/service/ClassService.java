package com.skillnest.course.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.course.model.ClassSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ClassService {

    private static final String COLLECTION_NAME = "classes";
    private final Firestore firestore;
    private final CourseService courseService;

    public ClassService(Firestore firestore, CourseService courseService) {
        this.firestore = firestore;
        this.courseService = courseService;
    }

    public ClassSession createClass(ClassSession classSession) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        classSession.setId(docRef.getId());
        classSession.setCreatedAt(System.currentTimeMillis());
        docRef.set(classSession.toMap()).get();

        // Increment total classes count on the course
        courseService.incrementTotalClasses(classSession.getCourseId(), 1);

        return classSession;
    }

    public List<ClassSession> getClassesByCourse(String courseId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("courseId", courseId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<ClassSession> classes = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            classes.add(ClassSession.fromMap(doc.getData()));
        }
        return classes;
    }

    public ClassSession getClassById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (doc.exists()) {
            return ClassSession.fromMap(doc.getData());
        }
        return null;
    }

    public ClassSession updateClass(String id, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        docRef.update(updates).get();
        DocumentSnapshot updated = docRef.get().get();
        if (updated.exists()) {
            return ClassSession.fromMap(updated.getData());
        }
        return null;
    }

    public List<ClassSession> getUpcomingClasses(String courseId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("courseId", courseId)
                .whereEqualTo("status", "scheduled")
                .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<ClassSession> classes = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            classes.add(ClassSession.fromMap(doc.getData()));
        }
        return classes;
    }

    public void deleteClass(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (doc.exists()) {
            String courseId = (String) doc.getData().get("courseId");
            firestore.collection(COLLECTION_NAME).document(id).delete().get();
            courseService.incrementTotalClasses(courseId, -1);
        }
    }
}
