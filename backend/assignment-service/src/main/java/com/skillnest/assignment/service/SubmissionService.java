package com.skillnest.assignment.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.assignment.model.Submission;
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
public class SubmissionService {

    private static final Logger logger = LoggerFactory.getLogger(SubmissionService.class);
    private static final String COLLECTION_NAME = "submissions";

    @Autowired(required = false)
    private Firestore firestore;

    @Autowired
    private AssignmentService assignmentService;

    // In-memory store for demo mode
    private final Map<String, Submission> demoSubmissions = new ConcurrentHashMap<>();

    private boolean isDemoMode() {
        return firestore == null;
    }

    public Submission createSubmission(Submission submission) throws ExecutionException, InterruptedException {
        String id = UUID.randomUUID().toString();
        submission.setId(id);
        submission.setSubmittedAt(Instant.now().toString());
        if (submission.getStatus() == null || submission.getStatus().isEmpty()) {
            submission.setStatus("submitted");
        }

        if (isDemoMode()) {
            logger.info("Demo mode: creating submission");
            demoSubmissions.put(id, submission);
            if (submission.getAssignmentId() != null) {
                assignmentService.incrementSubmissionCount(submission.getAssignmentId());
            }
            return submission;
        }

        firestore.collection(COLLECTION_NAME).document(id).set(submission.toMap()).get();

        // Auto-increment assignment submission count
        if (submission.getAssignmentId() != null) {
            assignmentService.incrementSubmissionCount(submission.getAssignmentId());
        }

        return submission;
    }

    public List<Submission> getSubmissionsByAssignment(String assignmentId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting submissions for assignment {}", assignmentId);
            return demoSubmissions.values().stream()
                    .filter(s -> assignmentId.equals(s.getAssignmentId()))
                    .collect(Collectors.toList());
        }

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
        if (isDemoMode()) {
            logger.info("Demo mode: getting submissions for student {}", studentId);
            return demoSubmissions.values().stream()
                    .filter(s -> studentId.equals(s.getStudentId()))
                    .collect(Collectors.toList());
        }

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
        if (isDemoMode()) {
            logger.info("Demo mode: getting submission {}", id);
            return demoSubmissions.get(id);
        }

        DocumentSnapshot document = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (document.exists()) {
            return Submission.fromMap(document.getData());
        }
        return null;
    }

    public Submission gradeSubmission(String id, Map<String, Object> gradeData) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: grading submission {}", id);
            Submission submission = demoSubmissions.get(id);
            if (submission == null) return null;

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
            demoSubmissions.put(id, submission);
            return submission;
        }

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
