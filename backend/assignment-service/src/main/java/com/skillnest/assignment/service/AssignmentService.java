package com.skillnest.assignment.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.assignment.model.Assignment;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class AssignmentService {

    private static final String COLLECTION_NAME = "assignments";

    private final Firestore firestore;

    public AssignmentService(Firestore firestore) {
        this.firestore = firestore;
    }

    public Assignment createAssignment(Assignment assignment) throws ExecutionException, InterruptedException {
        String id = UUID.randomUUID().toString();
        assignment.setId(id);
        assignment.setStatus("active");
        assignment.setSubmissionCount(0);
        assignment.setCreatedAt(Instant.now().toString());
        assignment.setUpdatedAt(Instant.now().toString());

        firestore.collection(COLLECTION_NAME).document(id).set(assignment.toMap()).get();
        return assignment;
    }

    public List<Assignment> getAllAssignments() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Assignment> assignments = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            assignments.add(Assignment.fromMap(document.getData()));
        }
        return assignments;
    }

    public Assignment getAssignmentById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return Assignment.fromMap(document.getData());
        }
        return null;
    }

    public List<Assignment> getAssignmentsByCourse(String courseId) throws ExecutionException, InterruptedException {
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
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (!document.exists()) {
            return false;
        }
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
        return true;
    }

    public void incrementSubmissionCount(String assignmentId) throws ExecutionException, InterruptedException {
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
