package com.skillnest.course.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.course.model.Enrollment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class EnrollmentService {

    private static final String COLLECTION_NAME = "enrollments";
    private final Firestore firestore;
    private final CourseService courseService;

    public EnrollmentService(Firestore firestore, CourseService courseService) {
        this.firestore = firestore;
        this.courseService = courseService;
    }

    public Enrollment enrollStudent(Enrollment enrollment) throws ExecutionException, InterruptedException {
        // Check if already enrolled
        ApiFuture<QuerySnapshot> check = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("courseId", enrollment.getCourseId())
                .whereEqualTo("studentId", enrollment.getStudentId())
                .get();
        if (!check.get().isEmpty()) {
            throw new RuntimeException("Student is already enrolled in this course");
        }

        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        enrollment.setId(docRef.getId());
        enrollment.setEnrolledAt(System.currentTimeMillis());
        enrollment.setProgress(0.0);
        enrollment.setStatus("active");
        docRef.set(enrollment.toMap()).get();

        // Increment enrolled students count on the course
        courseService.incrementEnrolledStudents(enrollment.getCourseId(), 1);

        return enrollment;
    }

    public List<Enrollment> getEnrollmentsByCourse(String courseId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("courseId", courseId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Enrollment> enrollments = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            enrollments.add(Enrollment.fromMap(doc.getData()));
        }
        return enrollments;
    }

    public List<Enrollment> getEnrollmentsByStudent(String studentId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("studentId", studentId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Enrollment> enrollments = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            enrollments.add(Enrollment.fromMap(doc.getData()));
        }
        return enrollments;
    }

    public void unenrollStudent(String courseId, String studentId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("courseId", courseId)
                .whereEqualTo("studentId", studentId)
                .get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            doc.getReference().delete().get();
        }

        // Decrement enrolled students count on the course
        courseService.incrementEnrolledStudents(courseId, -1);
    }
}
