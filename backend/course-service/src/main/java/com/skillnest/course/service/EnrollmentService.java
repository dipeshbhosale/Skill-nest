package com.skillnest.course.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.course.model.Enrollment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);
    private static final String COLLECTION_NAME = "enrollments";

    @Autowired(required = false)
    private Firestore firestore;

    @Autowired
    private CourseService courseService;

    // In-memory store for demo mode
    private final Map<String, Enrollment> demoEnrollments = new ConcurrentHashMap<>();

    // Initialize demo enrollments
    {
        Enrollment e1 = new Enrollment();
        e1.setId("demo-enrollment-001");
        e1.setCourseId("demo-course-001");
        e1.setStudentId("demo-student-001");
        e1.setEnrolledAt(System.currentTimeMillis());
        e1.setProgress(25.0);
        e1.setStatus("active");
        demoEnrollments.put(e1.getId(), e1);
    }

    private boolean isDemoMode() {
        return firestore == null;
    }

    public Enrollment enrollStudent(Enrollment enrollment) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: enrolling student");
            // Check if already enrolled
            boolean alreadyEnrolled = demoEnrollments.values().stream()
                    .anyMatch(e -> e.getCourseId().equals(enrollment.getCourseId()) 
                            && e.getStudentId().equals(enrollment.getStudentId()));
            if (alreadyEnrolled) {
                throw new RuntimeException("Student is already enrolled in this course");
            }
            String id = "demo-enrollment-" + UUID.randomUUID().toString().substring(0, 8);
            enrollment.setId(id);
            enrollment.setEnrolledAt(System.currentTimeMillis());
            enrollment.setProgress(0.0);
            enrollment.setStatus("active");
            demoEnrollments.put(id, enrollment);
            courseService.incrementEnrolledStudents(enrollment.getCourseId(), 1);
            return enrollment;
        }

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
        if (isDemoMode()) {
            logger.info("Demo mode: getting enrollments for course {}", courseId);
            return demoEnrollments.values().stream()
                    .filter(e -> courseId.equals(e.getCourseId()))
                    .collect(Collectors.toList());
        }

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
        if (isDemoMode()) {
            logger.info("Demo mode: getting enrollments for student {}", studentId);
            return demoEnrollments.values().stream()
                    .filter(e -> studentId.equals(e.getStudentId()))
                    .collect(Collectors.toList());
        }

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
        if (isDemoMode()) {
            logger.info("Demo mode: unenrolling student {} from course {}", studentId, courseId);
            demoEnrollments.entrySet().removeIf(entry -> 
                    courseId.equals(entry.getValue().getCourseId()) 
                    && studentId.equals(entry.getValue().getStudentId()));
            courseService.incrementEnrolledStudents(courseId, -1);
            return;
        }

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
