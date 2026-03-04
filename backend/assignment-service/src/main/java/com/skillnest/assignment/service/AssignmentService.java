package com.skillnest.assignment.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.assignment.model.Assignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private static final Logger logger = LoggerFactory.getLogger(AssignmentService.class);
    private static final String COLLECTION_NAME = "assignments";

    @Autowired(required = false)
    private Firestore firestore;

    // In-memory store for demo mode
    private final Map<String, Assignment> demoAssignments = new ConcurrentHashMap<>();

    private boolean isDemoMode() {
        return firestore == null;
    }

    public Assignment createAssignment(Assignment assignment) throws ExecutionException, InterruptedException {
        String id = UUID.randomUUID().toString();
        assignment.setId(id);
        assignment.setStatus("active");
        assignment.setSubmissionCount(0);
        assignment.setCreatedAt(Instant.now().toString());
        assignment.setUpdatedAt(Instant.now().toString());

        if (isDemoMode()) {
            logger.info("Demo mode: creating assignment");
            demoAssignments.put(id, assignment);
            return assignment;
        }

        firestore.collection(COLLECTION_NAME).document(id).set(assignment.toMap()).get();
        return assignment;
    }

    public List<Assignment> getAllAssignments() throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: returning demo assignments");
            return new ArrayList<>(demoAssignments.values());
        }

        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Assignment> assignments = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            assignments.add(Assignment.fromMap(document.getData()));
        }
        return assignments;
    }

    public Assignment getAssignmentById(String id) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting assignment {}", id);
            return demoAssignments.get(id);
        }

        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return Assignment.fromMap(document.getData());
        }
        return null;
    }

    public List<Assignment> getAssignmentsByCourse(String courseId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting assignments for course {}", courseId);
            return demoAssignments.values().stream()
                    .filter(a -> courseId.equals(a.getCourseId()))
                    .collect(Collectors.toList());
        }

        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("courseId", courseId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Assignment> assignments = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            assignments.add(Assignment.fromMap(document.getData()));
        }
        return assignments;
    }

    public List<Assignment> getAssignmentsByTeacher(String teacherId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting assignments for teacher {}", teacherId);
            return demoAssignments.values().stream()
                    .filter(a -> teacherId.equals(a.getCreatedBy()))
                    .collect(Collectors.toList());
        }

        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("createdBy", teacherId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Assignment> assignments = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            assignments.add(Assignment.fromMap(document.getData()));
        }
        return assignments;
    }

    public Assignment updateAssignment(String id, Assignment assignment) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: updating assignment {}", id);
            Assignment existing = demoAssignments.get(id);
            if (existing == null) return null;

            assignment.setId(id);
            assignment.setUpdatedAt(Instant.now().toString());
            assignment.setCreatedAt(existing.getCreatedAt());
            assignment.setCreatedBy(existing.getCreatedBy());
            assignment.setSubmissionCount(existing.getSubmissionCount());
            demoAssignments.put(id, assignment);
            return assignment;
        }

        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (!document.exists()) {
            return null;
        }

        assignment.setId(id);
        assignment.setUpdatedAt(Instant.now().toString());

        // Preserve original createdAt and createdBy
        Assignment existing = Assignment.fromMap(document.getData());
        assignment.setCreatedAt(existing.getCreatedAt());
        assignment.setCreatedBy(existing.getCreatedBy());
        assignment.setSubmissionCount(existing.getSubmissionCount());

        firestore.collection(COLLECTION_NAME).document(id).set(assignment.toMap()).get();
        return assignment;
    }

    public boolean deleteAssignment(String id) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: deleting assignment {}", id);
            return demoAssignments.remove(id) != null;
        }

        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (!document.exists()) {
            return false;
        }
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
        return true;
    }

    public void incrementSubmissionCount(String assignmentId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: incrementing submission count for assignment {}", assignmentId);
            Assignment assignment = demoAssignments.get(assignmentId);
            if (assignment != null) {
                assignment.setSubmissionCount(assignment.getSubmissionCount() + 1);
                assignment.setUpdatedAt(Instant.now().toString());
                demoAssignments.put(assignmentId, assignment);
            }
            return;
        }

        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(assignmentId);
        DocumentSnapshot document = docRef.get().get();
        if (document.exists()) {
            Assignment assignment = Assignment.fromMap(document.getData());
            assignment.setSubmissionCount(assignment.getSubmissionCount() + 1);
            assignment.setUpdatedAt(Instant.now().toString());
            docRef.set(assignment.toMap()).get();
        }
    }
}
