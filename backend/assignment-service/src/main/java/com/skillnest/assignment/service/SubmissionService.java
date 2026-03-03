package com.skillnest.assignment.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.assignment.model.Submission;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class SubmissionService {

    private static final String COLLECTION_NAME = "submissions";

    private final Firestore firestore;
    private final AssignmentService assignmentService;

    public SubmissionService(Firestore firestore, AssignmentService assignmentService) {
        this.firestore = firestore;
        this.assignmentService = assignmentService;
    }

    public Submission createSubmission(Submission submission) throws ExecutionException, InterruptedException {
        String id = UUID.randomUUID().toString();
        submission.setId(id);
        submission.setSubmittedAt(Instant.now().toString());
        if (submission.getStatus() == null || submission.getStatus().isEmpty()) {
            submission.setStatus("submitted");
        }

        firestore.collection(COLLECTION_NAME).document(id).set(submission.toMap()).get();

        // Auto-increment assignment submission count
        if (submission.getAssignmentId() != null) {
            assignmentService.incrementSubmissionCount(submission.getAssignmentId());
        }

        return submission;
    }

    public List<Submission> getSubmissionsByAssignment(String assignmentId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("assignmentId", assignmentId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Submission> submissions = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            submissions.add(Submission.fromMap(document.getData()));
        }
        return submissions;
    }

    public List<Submission> getSubmissionsByStudent(String studentId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("studentId", studentId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();

        List<Submission> submissions = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            submissions.add(Submission.fromMap(document.getData()));
        }
        return submissions;
    }

    public Submission getSubmissionById(String id) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return Submission.fromMap(document.getData());
        }
        return null;
    }

    public Submission gradeSubmission(String id, Map<String, Object> gradeData) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (!document.exists()) {
            return null;
        }

        Submission submission = Submission.fromMap(document.getData());

        if (gradeData.containsKey("grade")) {
            submission.setGrade(((Number) gradeData.get("grade")).doubleValue());
        }
        if (gradeData.containsKey("feedback")) {
            submission.setFeedback((String) gradeData.get("feedback"));
        }
        if (gradeData.containsKey("gradedBy")) {
            submission.setGradedBy((String) gradeData.get("gradedBy"));
        }

        submission.setStatus("graded");
        submission.setGradedAt(Instant.now().toString());

        firestore.collection(COLLECTION_NAME).document(id).set(submission.toMap()).get();
        return submission;
    }
}
