package com.skillnest.course.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.course.model.ClassSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ClassService {

    private static final Logger logger = LoggerFactory.getLogger(ClassService.class);
    private static final String COLLECTION_NAME = "classes";

    @Autowired(required = false)
    private Firestore firestore;

    @Autowired
    private CourseService courseService;

    // In-memory store for demo mode
    private final Map<String, ClassSession> demoClasses = new ConcurrentHashMap<>();

    private boolean isDemoMode() {
        return firestore == null;
    }

    public ClassSession createClass(ClassSession classSession) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: creating class");
            String id = "demo-class-" + UUID.randomUUID().toString().substring(0, 8);
            classSession.setId(id);
            classSession.setCreatedAt(System.currentTimeMillis());
            demoClasses.put(id, classSession);
            courseService.incrementTotalClasses(classSession.getCourseId(), 1);
            return classSession;
        }

        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        classSession.setId(docRef.getId());
        classSession.setCreatedAt(System.currentTimeMillis());
        docRef.set(classSession.toMap()).get();

        // Increment total classes count on the course
        courseService.incrementTotalClasses(classSession.getCourseId(), 1);

        return classSession;
    }

    public List<ClassSession> getClassesByCourse(String courseId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting classes for course {}", courseId);
            return demoClasses.values().stream()
                    .filter(c -> courseId.equals(c.getCourseId()))
                    .collect(Collectors.toList());
        }

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
        if (isDemoMode()) {
            logger.info("Demo mode: getting class {}", id);
            return demoClasses.get(id);
        }

        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (doc.exists()) {
            return ClassSession.fromMap(doc.getData());
        }
        return null;
    }

    public ClassSession updateClass(String id, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: updating class {}", id);
            ClassSession cs = demoClasses.get(id);
            if (cs != null) {
                if (updates.containsKey("title")) cs.setTitle((String) updates.get("title"));
                if (updates.containsKey("status")) cs.setStatus((String) updates.get("status"));
                demoClasses.put(id, cs);
            }
            return cs;
        }

        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        docRef.update(updates).get();
        DocumentSnapshot updated = docRef.get().get();
        if (updated.exists()) {
            return ClassSession.fromMap(updated.getData());
        }
        return null;
    }

    public List<ClassSession> getUpcomingClasses(String courseId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting upcoming classes for course {}", courseId);
            return demoClasses.values().stream()
                    .filter(c -> courseId.equals(c.getCourseId()) && "scheduled".equals(c.getStatus()))
                    .collect(Collectors.toList());
        }

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
        if (isDemoMode()) {
            logger.info("Demo mode: deleting class {}", id);
            ClassSession cs = demoClasses.remove(id);
            if (cs != null) {
                courseService.incrementTotalClasses(cs.getCourseId(), -1);
            }
            return;
        }

        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (doc.exists()) {
            String courseId = (String) doc.getData().get("courseId");
            firestore.collection(COLLECTION_NAME).document(id).delete().get();
            courseService.incrementTotalClasses(courseId, -1);
        }
    }
}
