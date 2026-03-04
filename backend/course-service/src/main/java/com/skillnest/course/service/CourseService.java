package com.skillnest.course.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.skillnest.course.model.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private static final String COLLECTION_NAME = "courses";

    @Autowired(required = false)
    private Firestore firestore;

    // In-memory store for demo mode
    private final Map<String, Course> demoCourses = new ConcurrentHashMap<>();

    // Initialize demo courses
    {
        Course course1 = new Course();
        course1.setId("demo-course-001");
        course1.setTitle("Introduction to Programming");
        course1.setDescription("Learn the basics of programming with Java");
        course1.setTeacherId("demo-teacher-001");
        course1.setTeacherName("Prof. Smith");
        course1.setCategory("Programming");
        course1.setEnrolledStudents(15);
        course1.setTotalClasses(24);
        course1.setStatus("active");
        course1.setCreatedAt(System.currentTimeMillis());
        demoCourses.put(course1.getId(), course1);

        Course course2 = new Course();
        course2.setId("demo-course-002");
        course2.setTitle("Web Development Fundamentals");
        course2.setDescription("Build modern web applications");
        course2.setTeacherId("demo-teacher-001");
        course2.setTeacherName("Prof. Smith");
        course2.setCategory("Web Development");
        course2.setEnrolledStudents(20);
        course2.setTotalClasses(36);
        course2.setStatus("active");
        course2.setCreatedAt(System.currentTimeMillis());
        demoCourses.put(course2.getId(), course2);
    }

    private boolean isDemoMode() {
        return firestore == null;
    }

    public Course createCourse(Course course) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: creating course");
            String id = "demo-course-" + UUID.randomUUID().toString().substring(0, 8);
            course.setId(id);
            course.setCreatedAt(System.currentTimeMillis());
            course.setUpdatedAt(System.currentTimeMillis());
            demoCourses.put(id, course);
            return course;
        }
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document();
        course.setId(docRef.getId());
        course.setCreatedAt(System.currentTimeMillis());
        course.setUpdatedAt(System.currentTimeMillis());
        docRef.set(course.toMap()).get();
        return course;
    }

    public List<Course> getAllCourses() throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: returning demo courses");
            return new ArrayList<>(demoCourses.values());
        }
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Course> courses = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            courses.add(Course.fromMap(doc.getData()));
        }
        return courses;
    }

    public Course getCourseById(String id) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting course {}", id);
            return demoCourses.get(id);
        }
        DocumentSnapshot doc = firestore.collection(COLLECTION_NAME).document(id).get().get();
        if (doc.exists()) {
            return Course.fromMap(doc.getData());
        }
        return null;
    }

    public List<Course> getCoursesByTeacher(String teacherId) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: getting courses for teacher {}", teacherId);
            return demoCourses.values().stream()
                    .filter(c -> teacherId.equals(c.getTeacherId()))
                    .collect(Collectors.toList());
        }
        ApiFuture<QuerySnapshot> future = firestore.collection(COLLECTION_NAME)
                .whereEqualTo("teacherId", teacherId).get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        List<Course> courses = new ArrayList<>();
        for (QueryDocumentSnapshot doc : documents) {
            courses.add(Course.fromMap(doc.getData()));
        }
        return courses;
    }

    public Course updateCourse(String id, Map<String, Object> updates) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: updating course {}", id);
            Course course = demoCourses.get(id);
            if (course != null) {
                if (updates.containsKey("title")) course.setTitle((String) updates.get("title"));
                if (updates.containsKey("description")) course.setDescription((String) updates.get("description"));
                if (updates.containsKey("category")) course.setCategory((String) updates.get("category"));
                course.setUpdatedAt(System.currentTimeMillis());
                demoCourses.put(id, course);
            }
            return course;
        }
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(id);
        updates.put("updatedAt", System.currentTimeMillis());
        docRef.update(updates).get();
        DocumentSnapshot updated = docRef.get().get();
        if (updated.exists()) {
            return Course.fromMap(updated.getData());
        }
        return null;
    }

    public void deleteCourse(String id) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: deleting course {}", id);
            demoCourses.remove(id);
            return;
        }
        firestore.collection(COLLECTION_NAME).document(id).delete().get();
    }

    public void incrementEnrolledStudents(String courseId, int delta) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: incrementing enrolled students for course {}", courseId);
            Course course = demoCourses.get(courseId);
            if (course != null) {
                course.setEnrolledStudents(course.getEnrolledStudents() + delta);
                demoCourses.put(courseId, course);
            }
            return;
        }
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(courseId);
        docRef.update("enrolledStudents", FieldValue.increment(delta)).get();
    }

    public void incrementTotalClasses(String courseId, int delta) throws ExecutionException, InterruptedException {
        if (isDemoMode()) {
            logger.info("Demo mode: incrementing total classes for course {}", courseId);
            Course course = demoCourses.get(courseId);
            if (course != null) {
                course.setTotalClasses(course.getTotalClasses() + delta);
                demoCourses.put(courseId, course);
            }
            return;
        }
        DocumentReference docRef = firestore.collection(COLLECTION_NAME).document(courseId);
        docRef.update("totalClasses", FieldValue.increment(delta)).get();
    }
}
